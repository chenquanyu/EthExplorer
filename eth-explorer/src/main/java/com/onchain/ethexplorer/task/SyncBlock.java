package com.onchain.ethexplorer.task;

import com.alibaba.fastjson.JSON;
import com.onchain.ethexplorer.constant.Constant;
import com.onchain.ethexplorer.mapper.BlockMapper;
import com.onchain.ethexplorer.model.Account;
import com.onchain.ethexplorer.model.Block;
import com.onchain.ethexplorer.model.Transaction;
import com.onchain.ethexplorer.model.TxLog;
import com.onchain.ethexplorer.service.BlockService;
import com.onchain.ethexplorer.utils.ConverterFunctionUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameter;
import org.web3j.protocol.core.methods.response.EthBlock;
import org.web3j.protocol.core.methods.response.Log;
import org.web3j.protocol.core.methods.response.TransactionReceipt;

import java.math.BigInteger;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

@Component
@RequiredArgsConstructor
@Slf4j
public class SyncBlock {

    private final Web3j web3j;
    private final BlockMapper blockMapper;
    private final BlockService blockService;

    @Scheduled(fixedRate = 5000)
    public void run() throws Exception {
        Long latest = blockMapper.latestNumber();
        long ethLatest = web3j.ethBlockNumber().send().getBlockNumber().longValue();
        if (null == latest) {
            latest = 0L;
        }

        for (long i = latest + 1; i < ethLatest + 1; i++) {
            handleBlock(i);
        }
    }

    public void handleBlock(Long blockNumber) throws Exception {
        EthBlock.Block blk = web3j.ethGetBlockByNumber(
                DefaultBlockParameter.valueOf(BigInteger.valueOf(blockNumber)), true)
                .send().getBlock();

        Block block = ConverterFunctionUtil.toDbBlock.apply(blk);
        List<Transaction> transactions = ConverterFunctionUtil.toTransactions.apply(blk);
        List<Account> accounts = ConverterFunctionUtil.toAccounts.apply(blk);

        Map<String, TransactionReceipt> transactionReceiptMap = updateTransactions(transactions);

//        log.info(JSON.toJSONString(transactionReceiptMap));
        List<Account> allAccounts = getAllAddresses(accounts, transactionReceiptMap, block);
        List<TxLog> logs = getAllLogs(block, transactionReceiptMap);
        updateAccounts(allAccounts);

        blockService.storeBlock(block, transactions, allAccounts, logs);
        log.info("block {} sync succeeded", blockNumber);
    }

    private Map<String, TransactionReceipt> updateTransactions(List<Transaction> transactions) throws Exception {
        Map<String, TransactionReceipt> result = new ConcurrentHashMap<>();
        List<CompletableFuture> futures = new ArrayList<>();
        for (Transaction tx : transactions) {
            futures.add(web3j.ethGetTransactionReceipt(tx.getTxHash()).sendAsync()
                    .thenApply(receipt -> {
                        TransactionReceipt r = receipt.getTransactionReceipt().get();
                        tx.setTxType(null == r.getContractAddress() ? 0 : 1);
                        tx.setGasUsed(r.getGasUsed().intValue());
                        tx.setTxStatus(r.getStatus());

                        result.putIfAbsent(tx.getTxHash(), r);
                        return receipt;
                    })
            );
        }
        CompletableFuture.allOf(futures.toArray(new CompletableFuture[futures.size()])).get();
        return result;
    }

    private List<Account> getAllAddresses(List<Account> accounts, Map<String, TransactionReceipt> transactionReceiptMap, Block block) {
        Set<Account> result = new HashSet<>();

        for (TransactionReceipt receipt : transactionReceiptMap.values()) {
            if (null != receipt.getContractAddress()) {
                result.add(Account.builder()
                        .address(receipt.getContractAddress())
                        .type(1)
                        .blockTime(block.getBlockTime())
                        .build());
            }
        }
        result.addAll(accounts);

        return new ArrayList<>(result);
    }

    private List<TxLog> getAllLogs(Block block, Map<String, TransactionReceipt> transactionReceiptMap) {
        Set<TxLog> result = new HashSet<>();

        for (TransactionReceipt receipt : transactionReceiptMap.values()) {
            if (receipt.getLogs() != null && !receipt.getLogs().isEmpty()) {
                for (Log item : receipt.getLogs()) {
                    result.add(TxLog.builder()
                            .txHash(item.getTransactionHash())
                            .txType(null == receipt.getContractAddress() ? 0 : 1)
                            .blockTime(block.getBlockTime())
                            .blockHash(item.getBlockHash())
                            .blockNumber(item.getBlockNumber().longValue())
                            .logIndex(item.getLogIndex().intValue())
                            .txIndex(receipt.getTransactionIndex().intValue())
                            .address(item.getAddress())
                            .data(item.getData())
                            .type(item.getType() == null ? "" : item.getType())
                            .topics(JSON.toJSONString(item.getTopics()))
                            .build());
                }
            }
        }
        return new ArrayList<>(result);
    }

    private void updateAccounts(List<Account> accounts) throws Exception {
        List<CompletableFuture> futures = new ArrayList<>();

        for (Account account : accounts) {
            futures.add(web3j
                    .ethGetBalance(account.getAddress(), DefaultBlockParameter.valueOf(Constant.LatestBlockNumberKey))
                    .sendAsync()
                    .thenApply(balance -> {
                        account.setBalance(balance.getBalance().divide(Constant.GWeiFactor).longValue());
                        return balance;
                    })
            );
            futures.add(web3j
                    .ethGetTransactionCount(account.getAddress(), DefaultBlockParameter.valueOf(Constant.LatestBlockNumberKey))
                    .sendAsync()
                    .thenApply(count -> {
                        account.setNonce(count.getTransactionCount().intValue());
                        return count;
                    })
            );
        }

        CompletableFuture.allOf(futures.toArray(new CompletableFuture[futures.size()])).get();
    }

}
