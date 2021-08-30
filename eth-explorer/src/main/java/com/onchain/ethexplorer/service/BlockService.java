package com.onchain.ethexplorer.service;

import com.onchain.ethexplorer.mapper.AccountMapper;
import com.onchain.ethexplorer.mapper.BlockMapper;
import com.onchain.ethexplorer.mapper.TransactionMapper;
import com.onchain.ethexplorer.mapper.TxLogMapper;
import com.onchain.ethexplorer.model.Account;
import com.onchain.ethexplorer.model.Block;
import com.onchain.ethexplorer.model.Transaction;
import com.onchain.ethexplorer.model.TxLog;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@AllArgsConstructor
public class BlockService {

    private final BlockMapper blockMapper;
    private final TransactionMapper transactionMapper;
    private final AccountMapper accountMapper;
    private final TxLogMapper txLogMapper;

    @Transactional(rollbackFor = Exception.class)
    public void storeBlock(Block block, List<Transaction> transactions, List<Account> allAccounts, List<TxLog> logs) {
        log.info("####### storeBlock start.");
        if (!allAccounts.isEmpty()) {
            accountMapper.update(allAccounts);
        }
        if (!transactions.isEmpty()) {
            transactionMapper.insert(transactions);
        }
        if (!logs.isEmpty()) {
            txLogMapper.insert(logs);
        }
//        test roll back
//        if (block.getBlockNumber() == 2) {
//            block.setBlockNumber(null);
//        }
        blockMapper.insert(block);
    }

}
