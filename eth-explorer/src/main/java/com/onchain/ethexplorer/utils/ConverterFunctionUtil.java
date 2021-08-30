package com.onchain.ethexplorer.utils;

import com.onchain.ethexplorer.constant.Constant;
import com.onchain.ethexplorer.model.Account;
import com.onchain.ethexplorer.model.Block;
import com.onchain.ethexplorer.model.Transaction;
import org.web3j.protocol.core.methods.response.EthBlock;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public interface ConverterFunctionUtil {
    Function<EthBlock.Block, Block> toDbBlock
            = blk -> Block.builder()
            .blockNumber(blk.getNumber().longValue())
            .blockHash(blk.getHash())
            .blockTime(new Date(blk.getTimestamp().longValue() * Constant.TimestampFactor))
            .miner(blk.getMiner())
            .difficulty(blk.getDifficulty().longValue())
            .totalDifficulty(blk.getTotalDifficulty().longValue())
            .size(blk.getSize().intValue())
            .gasUsed(blk.getGasUsed().intValue())
            .gasLimit(blk.getGasLimit().intValue())
            .nonce(blk.getNonceRaw())
            .extraData(blk.getExtraData())
            .uncleHash(blk.getSha3Uncles())
            .parentHash(blk.getParentHash())
            .stateRoot(blk.getStateRoot())
            .receiptsRoot(blk.getReceiptsRoot())
            .transactionsRoot(blk.getTransactionsRoot())
            .txCount(blk.getTransactions().size())
            .build();

    Function<EthBlock.Block, List<Transaction>> toTransactions
            = blk -> blk.getTransactions().stream()
            .map(txResult -> {
                EthBlock.TransactionObject tx = (EthBlock.TransactionObject) txResult;
                return Transaction.builder()
                        .txHash(tx.getHash())
                        .blockHash(tx.getBlockHash())
                        .blockNumber(tx.getBlockNumber().longValue())
                        .from(tx.getFrom())
                        .to(tx.getTo() == null ? "" : tx.getTo())
                        .value(tx.getValue().divide(Constant.GWeiFactor).longValue())
                        .blockTime(new Date(blk.getTimestamp().longValue() * Constant.TimestampFactor))
                        .nonce(tx.getNonce().intValue())
                        .txIndex(tx.getTransactionIndex().intValue())
                        .data(tx.getInput())
                        .gasPrice(tx.getGasPrice().longValue())
                        .gasLimit(tx.getGas().intValue())
                        .build();
            })
            .collect(Collectors.toList());

    Function<EthBlock.Block, List<Account>> toAccounts
            = blk -> {
        Set<Account> accountSet = new HashSet<>();
        accountSet.add(Account.builder().address(blk.getMiner()).type(0).blockTime(new Date(blk.getTimestamp().longValue())).build());
        for (EthBlock.TransactionResult txResult : blk.getTransactions()) {
            EthBlock.TransactionObject tx = (EthBlock.TransactionObject) txResult;
            if (null != tx.getTo()) {
                accountSet.add(Account.builder().address(tx.getTo()).type(0).blockTime(new Date(blk.getTimestamp().longValue())).build());
            }
        }
        return new ArrayList<>(accountSet);
    };

    Function<List<org.web3j.protocol.core.methods.response.Transaction>, List<Transaction>> toPendingTransaction
            = txs -> txs.stream()
            .map(tx -> Transaction.builder()
                    .txHash(tx.getHash())
                    .blockHash(tx.getBlockHash())
                    .blockNumber(tx.getBlockNumber().longValue())
                    .from(tx.getFrom())
                    .to(tx.getTo())
                    .value(tx.getValue().divide(Constant.GWeiFactor).longValue())
                    .nonce(tx.getNonce().intValue())
                    .txIndex(tx.getTransactionIndex().intValue())
                    .data(tx.getInput())
                    .gasPrice(tx.getGasPrice().longValue())
                    .gasLimit(tx.getGas().intValue())
                    .build())
            .collect(Collectors.toList());
}
