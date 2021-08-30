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
public class Transaction {
    private Long id;
    private Date createTime;
    private Date updateTime;
    private String status;

    private String txHash;
    private String blockHash;
    private long blockNumber;
    private String from;
    private String to;
    private Long value;
    // 0: fail, 1: success
    private String txStatus;
    private Date blockTime;
    private Integer nonce;
    private Integer txIndex;
    // 0: normal; 1: contract creation
    private Integer txType;
    private String data;
    private Long gasPrice;
    private Integer gasLimit;
    private Integer gasUsed;
}
