package com.onchain.ethexplorer.mapper;

import com.onchain.ethexplorer.model.Block;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

public interface BlockMapper {

    @Select("select max(block_number) from tbl_block")
    Long latestNumber();
//
//    Block load(@Param("number") long number);
//
//    Block loadByHash(@Param("hash") String hash);

    @Insert("insert into tbl_block (block_number, block_hash, block_time, miner, difficulty, " +
            "total_difficulty, size, gas_used, gas_limit, " +
            "nonce, extra_data, parent_hash, uncle_hash, state_root, " +
            "receipts_root, transactions_root, tx_count) " +
            "values " +
            "(#{block.blockNumber}, #{block.blockHash}, #{block.blockTime}, #{block.miner}, #{block.difficulty}, " +
            "#{block.totalDifficulty}, #{block.size}, #{block.gasUsed}, #{block.gasLimit}, " +
            "#{block.nonce}, #{block.extraData}, #{block.parentHash}, #{block.uncleHash}, #{block.stateRoot}, " +
            "#{block.receiptsRoot}, #{block.transactionsRoot}, #{block.txCount})")
    Integer insert(@Param("block") Block block);
//
//    List<Block> topByNumber(@Param("startNumber") long startNumber, @Param("endNumber") long endNumber);
//
//    List<Block> list(@Param("offset") long offset, @Param("limit") long limit, @Param("miner") String miner);
//
//    Long count(@Param("miner") String miner);
}
