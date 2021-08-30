package com.onchain.ethexplorer;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@MapperScan(basePackages = {"com.onchain.ethexplorer.mapper"})
@EnableTransactionManagement
@EnableScheduling
public class EthExplorerApplication {

    public static void main(String[] args) {
        SpringApplication.run(EthExplorerApplication.class, args);
    }

}
