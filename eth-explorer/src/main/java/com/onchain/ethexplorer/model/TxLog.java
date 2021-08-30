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
public class TxLog {
    private Long id;
    private Date createTime;
    private Date updateTime;
    private String status;

    private String txHash;
    // 0: normal; 1: contract creation
    private Integer txType;
    private Date blockTime;
    private String blockHash;
    private long blockNumber;
    private Integer logIndex;
    private Integer txIndex;

    private String address;
    private String data;
    private String type;
    private String topics;
}
