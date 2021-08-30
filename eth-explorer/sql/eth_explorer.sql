DROP DATABASE IF EXISTS eth_explorer;
CREATE DATABASE eth_explorer
  DEFAULT CHARSET = utf8mb4
  COLLATE utf8mb4_general_ci;
USE eth_explorer;

DROP TABLE IF EXISTS `tbl_address_daily_summary`;
CREATE TABLE `tbl_address_daily_summary` (
    `id`            BIGINT   AUTO_INCREMENT PRIMARY KEY,
    `create_time`   DATETIME DEFAULT CURRENT_TIMESTAMP                             NOT NULL,
    `update_time`   DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP NOT NULL,
    `status`        VARCHAR(20) DEFAULT ''                                         NOT NULL,
    `time`          INT(11)      NOT NULL COMMENT '当天UTC0点时间戳',
    `contract_hash` VARCHAR(64)  NOT NULL COMMENT '合约hash值',
    `address`       VARCHAR(255) NOT NULL DEFAULT '',
    KEY `idx_time`(`time`)
) ;

DROP TABLE IF EXISTS `tbl_account`;
CREATE TABLE `tbl_account` (
    `address`              VARCHAR(42)    NOT NULL COMMENT 'hex string of account address hash-42 用户地址',
    `type`                 tinyint(2)     NOT NULL DEFAULT '0' COMMENT 'account type, 0: normal; 1: contract',
    `balance`              bigint(16)      NOT NULL DEFAULT '0' COMMENT 'account balance in Gwei',
    `nonce`                int(8) unsigned NOT NULL DEFAULT '0' COMMENT 'nonce',
    `block_time`           datetime       NOT NULL COMMENT 'block timestamp 账户生成时间',
    PRIMARY KEY (`address`),
    KEY `idx_account_balance` (`balance`)
);

DROP TABLE IF EXISTS `tbl_block`;
CREATE TABLE `tbl_block` (
    `id`             BIGINT       AUTO_INCREMENT PRIMARY KEY,
    `create_time`    DATETIME     DEFAULT CURRENT_TIMESTAMP                             NOT NULL,
    `update_time`    DATETIME     DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP NOT NULL,
    `status`         VARCHAR(20)  DEFAULT ''                                            NOT NULL,

    `block_number`   BIGINT       NOT NULL COMMENT '区块高度',
    `block_hash`     VARCHAR(66)  NOT NULL DEFAULT '' COMMENT '区块hash值',
    `block_time`     datetime     NOT NULL COMMENT '区块时间戳',
    `miner`          VARCHAR(42)  NOT NULL COMMENT 'hex string of miner address',
    `difficulty`     bigint(16) unsigned NOT NULL COMMENT 'difficulty',
    `total_difficulty` bigint(16) unsigned NOT NULL COMMENT 'total difficulty',
    `size`          int(8) unsigned NOT NULL COMMENT 'data size in bytes',
    `gas_used`      int(8) unsigned NOT NULL COMMENT 'gas used',
    `gas_limit`     int(8) unsigned NOT NULL COMMENT 'gas limit',
    `nonce`         VARCHAR(18) NOT NULL COMMENT 'nonce',
    `extra_data`    text COMMENT 'extra data',
    `parent_hash`   VARCHAR(66) NOT NULL COMMENT 'hex string of block parent hash',
    `uncle_hash`    VARCHAR(66) NOT NULL COMMENT 'hex string of block uncle hash',
    `state_root`    VARCHAR(66) DEFAULT NULL COMMENT 'state hash root',
    `receipts_root` VARCHAR(66) DEFAULT NULL COMMENT 'receipts hash root',
    `transactions_root` VARCHAR(66) DEFAULT NULL COMMENT 'transactions hash root',
    `tx_count`       INT     NOT NULL COMMENT '区块里的交易数量',
    UNIQUE KEY `idx_block_number` (`block_number`),
    UNIQUE KEY `idx_block_hash` (`block_hash`),
    KEY `idx_block_miner` (`miner`)
) ;

DROP TABLE IF EXISTS `tbl_contract_daily_summary`;
CREATE TABLE `tbl_contract_daily_summary` (
    `id`                   BIGINT         AUTO_INCREMENT PRIMARY KEY,
    `create_time`          DATETIME       DEFAULT CURRENT_TIMESTAMP                             NOT NULL,
    `update_time`          DATETIME       DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP NOT NULL,
    `status`               VARCHAR(20)    DEFAULT ''                                            NOT NULL,
    `time`                 INT(11)        NOT NULL COMMENT '当天的UTC0点时间戳',
    `contract_hash`        VARCHAR(64)    NOT NULL DEFAULT '' COMMENT '合约hash值',
    `tx_count`             INT(10)        NOT NULL COMMENT '此合约当天的交易数量',
    `active_address_count` INT(10)        NOT NULL COMMENT '此合约当天的活跃地址数',
    `new_address_count`    INT(10)        NOT NULL COMMENT '此合约当天的新地址数',
    KEY `idx_time`(`time`) USING BTREE,
    KEY `idx_contract_hash`(`contract_hash`) USING BTREE
) ;

DROP TABLE IF EXISTS `tbl_contract`;
CREATE TABLE `tbl_contract` (
    `id`              BIGINT         AUTO_INCREMENT PRIMARY KEY,
    `create_time`     DATETIME       DEFAULT CURRENT_TIMESTAMP                             NOT NULL,
    `update_time`     DATETIME       DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP NOT NULL,
    `status`          VARCHAR(20)    DEFAULT ''                                            NOT NULL,

    `contract_hash`   VARCHAR(255)   NOT NULL DEFAULT '' COMMENT '合约hash值',
    `name`            VARCHAR(255)   NOT NULL DEFAULT '' COMMENT '名称',
    `abi`             TEXT           NOT NULL COMMENT '合约abi',
    `code`            TEXT           NOT NULL COMMENT '合约code',
    `source_code`     TEXT           NOT NULL COMMENT '合约源码',
    `block_time`      INT(11)        NOT NULL COMMENT '合约上链时间',
    `audit_flag`      TINYINT(1)     NOT NULL DEFAULT 0 COMMENT '审核标识，1：审核通过 0：未审核',
    `contact_info`    VARCHAR(1000)  NOT NULL DEFAULT '' COMMENT '合约项目方联系信息.json格式字符串',
    `description`     VARCHAR(1000)  NOT NULL DEFAULT '' COMMENT '合约描述',
    `logo`            VARCHAR(255)   NOT NULL DEFAULT '' COMMENT '合约logo的url',
    `creator`         VARCHAR(255)   NOT NULL DEFAULT '' COMMENT '合约创建者',
    `channel`         VARCHAR(255)   NOT NULL DEFAULT '' COMMENT '提交渠道',
    `address_count`   INT(11)        NOT NULL COMMENT '该合约的总的地址数 ',
    `tx_count`        INT(11)        NOT NULL COMMENT '合约总的交易量',
    `category`        VARCHAR(255)   NOT NULL DEFAULT '' COMMENT '合约分类',
     KEY (`contract_hash`) USING BTREE
) ;

DROP TABLE IF EXISTS `tbl_current`;
CREATE TABLE `tbl_current` (
    `id`             BIGINT         AUTO_INCREMENT PRIMARY KEY,
    `create_time`    DATETIME       DEFAULT CURRENT_TIMESTAMP                             NOT NULL,
    `update_time`    DATETIME       DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP NOT NULL,
    `status`         VARCHAR(20)    DEFAULT ''                                            NOT NULL,
    `block_number`   BIGINT         NOT NULL COMMENT  '当前同步的最新区块高度',
    `tx_count`       INT            NOT NULL COMMENT '当前同步的最新交易数量'
);

INSERT INTO tbl_current(block_number, tx_count)
VALUES (-1, 0);

DROP TABLE IF EXISTS `tbl_daily_summary`;
CREATE TABLE `tbl_daily_summary` (
    `id`                   BIGINT         AUTO_INCREMENT PRIMARY KEY,
    `create_time`          DATETIME       DEFAULT CURRENT_TIMESTAMP                             NOT NULL,
    `update_time`          DATETIME       DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP NOT NULL,
    `status`               VARCHAR(20)    DEFAULT ''                                            NOT NULL,
    `time`                 INT(11)        NOT NULL COMMENT '当月第一天的UTC0点时间戳',
    `block_count`          INT(11)        NOT NULL COMMENT '当天的区块数量',
    `tx_count`             INT(11)        NOT NULL COMMENT '当天的交易数量',
    `active_address_count` INT(11)        NOT NULL COMMENT '当天的活跃地址数量',
    `new_address_count`    INT(11)        NOT NULL COMMENT '当天的新地址数量',
    UNIQUE KEY `idx_time`(`time`) USING BTREE
);

DROP TABLE IF EXISTS `tbl_monthly_summary`;
CREATE TABLE `tbl_monthly_summary` (
    `id`                   BIGINT         AUTO_INCREMENT PRIMARY KEY,
    `create_time`          DATETIME       DEFAULT CURRENT_TIMESTAMP                             NOT NULL,
    `update_time`          DATETIME       DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP NOT NULL,
    `status`               VARCHAR(20)    DEFAULT ''                                            NOT NULL,
    `time`                 INT(11)        NOT NULL COMMENT '当天的UTC0点时间戳',
    `block_count`          INT(11)        NOT NULL COMMENT '当天的区块数量',
    `tx_count`             INT(11)        NOT NULL COMMENT '当天的交易数量',
    `active_address_count` INT(11)        NOT NULL COMMENT '当天的活跃地址数量',
    `new_address_count`    INT(11)        NOT NULL COMMENT '当天的新地址数量',
    UNIQUE KEY `idx_time`(`time`) USING BTREE
);

DROP TABLE IF EXISTS `tbl_node`;
CREATE TABLE `tbl_node` (
    `id`                   BIGINT         AUTO_INCREMENT PRIMARY KEY,
    `create_time`          DATETIME       DEFAULT CURRENT_TIMESTAMP                             NOT NULL,
    `update_time`          DATETIME       DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP NOT NULL,
    `status`               VARCHAR(20)    DEFAULT ''                                            NOT NULL,
    `name`                 VARCHAR(255)   NOT NULL COMMENT '节点名称',
    `region`               VARCHAR(255)   NOT NULL COMMENT '地区',
    `ip`                   VARCHAR(15)    NOT NULL COMMENT '节点的IP',
    `rest_port`            INT            NOT NULL default '20334' COMMENT '节点的restful端口',
    `version`              VARCHAR(50)    NOT NULL COMMENT '节点的版本号',
    `is_active`            BOOLEAN        NOT NULL COMMENT '是否处于活跃状态'
);

DROP TABLE IF EXISTS `tbl_transaction`;
CREATE TABLE `tbl_transaction` (
    `id`                   BIGINT         AUTO_INCREMENT PRIMARY KEY,
    `create_time`          DATETIME       DEFAULT CURRENT_TIMESTAMP                             NOT NULL,
    `update_time`          DATETIME       DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP NOT NULL,
    `status`               VARCHAR(20)    DEFAULT ''                                            NOT NULL,

    `tx_hash`              VARCHAR(66)    NOT NULL DEFAULT '' COMMENT '交易hash',
    `block_hash` char(66) NOT NULL COMMENT 'hex string of block hash',
    `block_number` bigint(16) unsigned NOT NULL COMMENT 'block number',
    `from` char(42) NOT NULL COMMENT 'hex string of the sender account address',
    `to` char(42) NOT NULL COMMENT 'hex string of the receiver account address',
    `value` bigint(16) DEFAULT NULL COMMENT 'value of transaction in Gwei',
    `tx_status` 		varchar(10) 	NOT NULL COMMENT 'transaction status, 0: failed; 1: success; ',
    `block_time` datetime DEFAULT NULL COMMENT 'transaction timestamp',
    `nonce` int(8) unsigned NOT NULL DEFAULT '0' COMMENT 'transaction nonce',
    `tx_index` int(8) unsigned NOT NULL DEFAULT '0' COMMENT 'transaction position index',
    `tx_type` tinyint(2) DEFAULT '0' COMMENT 'transaction type, 0: normal; 1: contract creation',
    `data` text COMMENT 'transaction data',
    `gas_price` bigint(16) DEFAULT NULL COMMENT 'gas price',
    `gas_limit` int(8) DEFAULT NULL COMMENT 'gas limit',
    `gas_used` int(8) DEFAULT NULL COMMENT 'gas used',
    UNIQUE KEY (`tx_hash`),
    KEY `idx_transaction_from` (`from`),
    KEY `idx_transaction_to` (`to`),
    KEY `idx_blknumber_txindex` (`block_number`,`tx_index`),
    KEY `idx_transaction_timestamp` (`block_time`)
);

DROP TABLE IF EXISTS `tbl_transaction_log`;
CREATE TABLE `tbl_transaction_log` (
    `id`                   BIGINT         AUTO_INCREMENT PRIMARY KEY,
    `create_time`          DATETIME       DEFAULT CURRENT_TIMESTAMP                             NOT NULL,
    `update_time`          DATETIME       DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP NOT NULL,
    `status`               VARCHAR(20)    DEFAULT ''                                            NOT NULL,

    `tx_hash`              VARCHAR(66)    NOT NULL DEFAULT '' COMMENT '交易hash值',
    `tx_type`              tinyint(2)     DEFAULT '0' COMMENT 'transaction type, 0: normal; 1: contract creation',
    `block_time`           datetime       DEFAULT NULL COMMENT 'transaction timestamp',
    `block_hash`           VARCHAR(66)    NOT NULL COMMENT 'hex string of block hash',
    `block_number`         BIGINT        NOT NULL COMMENT '区块高度',
    `log_index`            INT(11)        NOT NULL COMMENT 'log在交易里的索引',
    `tx_index`             INT(11)        NOT NULL COMMENT '交易在区块里的索引',

    `address`              VARCHAR(42)   NOT NULL DEFAULT '' COMMENT '？',
    `data`                 VARCHAR(1024)  NOT NULL DEFAULT '' COMMENT '该日志未indexed的参数',
    `type`                 VARCHAR(255)   NOT NULL DEFAULT '' COMMENT '？',
    `topics`               JSON           NOT NULL  COMMENT 'indexed 参数列表',
    KEY `idx_tx_hash`(`tx_hash`),
    KEY `idx_block_number`(`block_number`),
    KEY `idx_address`(`address`)
);