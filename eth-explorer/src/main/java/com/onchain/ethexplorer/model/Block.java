package com.onchain.ethexplorer.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Block {
    private Long id;
    private Date createTime;
    private Date updateTime;
    private String status;

    private Long blockNumber;
    private String blockHash;
    private Date blockTime;
    private String miner;
    private Long difficulty;
    private Long totalDifficulty;
    private Integer size;
    private Integer gasUsed;
    private Integer gasLimit;
    private String nonce;
    private String extraData;
    private String uncleHash;
    private String parentHash;
    private String stateRoot;
    private String receiptsRoot;
    private String transactionsRoot;
    private Integer txCount;
}
