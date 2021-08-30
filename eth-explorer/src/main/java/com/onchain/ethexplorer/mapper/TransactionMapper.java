package com.onchain.ethexplorer.mapper;

import com.onchain.ethexplorer.model.Transaction;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface TransactionMapper {
//
//    List<Transaction> listByBlock(@Param("blockNumber") long blockNumber);
//
//    List<Transaction> listByBlocks(@Param("blockNumbers") List<Long> blockNumbers);
//
//    Integer countInBlock(@Param("blockNumber") long blockNumber);
//
////    List<BlockTransactionCount> countInBlocks(@Param("blocks") List<Long> blockNumbers);
//
//    Transaction load(@Param("hash") String hash);

    @Insert("<script>" +
            "insert into tbl_transaction (tx_hash, block_hash, block_number, `from`, `to`, value, tx_status, block_time, " +
            "nonce, tx_index, tx_type, data, gas_price, gas_limit, gas_used) " +
            "values " +
            "<foreach collection='list' item='item' separator=','> " +
            "(#{item.txHash}, #{item.blockHash}, #{item.blockNumber}, #{item.from}, #{item.to}, #{item.value}, #{item.txStatus}, #{item.blockTime},  " +
            "#{item.nonce}, #{item.txIndex}, #{item.txType}, #{item.data}, #{item.gasPrice}, #{item.gasLimit}, #{item.gasUsed}) " +
            "</foreach>" +
            "</script>")
    Integer insert(@Param("list") List<Transaction> transactions);
//
//    List<Transaction> listByAddress(@Param("address") String address);
//
//    List<Transaction> top(@Param("offset") long offset, @Param("limit") long limit);
//
//    Long count();
//
//    Long countByBlockAndAddress(@Param("address") String address, @Param("block") Long block);
//
//    List<Transaction> list(@Param("offset") long offset, @Param("limit") long limit,
//                           @Param("address") String address, @Param("block") Long block);

//    List<DayCount> countByTimestamp(@Param("from") String from, @Param("to") String to);
}
