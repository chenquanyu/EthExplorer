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
public class Account {
    private Long id;
    private Date createTime;
    private Date updateTime;
    private String status;

    private String address;
    private Integer type;
    private Long balance;
    private Integer nonce;
    private Date blockTime;
}
