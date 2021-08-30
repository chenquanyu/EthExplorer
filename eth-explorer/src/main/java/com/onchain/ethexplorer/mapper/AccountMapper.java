package com.onchain.ethexplorer.mapper;


import com.onchain.ethexplorer.model.Account;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface AccountMapper {
//
//    Account load(@Param("hash") String hash);
//
//    Long count();
//
//    List<Account> topByBalance(@Param("offset") long offset, @Param("limit") long limit);

    @Insert("<script>" +
            "insert into tbl_account(address, type, balance, nonce, block_time) values " +
            "<foreach collection='addresses' item='item' separator=','> " +
            "( #{item.address},  #{item.type},  #{item.balance}, #{item.nonce}, #{item.blockTime} ) " +
            "</foreach> " +
            " on duplicate key update " +
            " balance = values(balance), nonce = values(nonce) " +
            "</script>")
    Integer update(@Param("addresses") List<Account> addresses);
//
//    Long sumBalance();
}
