package com.onchain.ethexplorer.config;

import com.onchain.ethexplorer.constant.Constant;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.http.HttpService;

@Configuration
public class Web3jConfig {
    @Bean
    public Web3j getHttpWeb3j() {
        return Web3j.build(new HttpService(Constant.HttpNodeAddress));
    }

}
