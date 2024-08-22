DROP TABLE IF EXISTS `product`;

CREATE TABLE `product`
(
    `id`          bigint       NOT NULL AUTO_INCREMENT,
    `category_id` bigint       NOT NULL,
    `seller_id`   bigint       NOT NULL,
    `name`        varchar(30)  NOT NULL,
    `image`       varchar(255) NULL,
    `description` varchar(255) NOT NULL,
    `price`       bigint       NOT NULL,
    `stock`       bigint       NOT NULL,
    `created_at`  datetime     NULL,
    `modified_at` datetime     NULL,
    PRIMARY KEY (`id`)
);

DROP TABLE IF EXISTS `orders`;

CREATE TABLE `orders`
(
    `id`              varchar(255) NOT NULL,
    `user_id`         bigint       NOT NULL,
    `orderer_name`    varchar(20)  NOT NULL,
    `orderer_email`   varchar(100) NOT NULL,
    `road_address`    varchar(100) NOT NULL,
    `address_detail`  varchar(100) NOT NULL,
    `zipcode`         varchar(50)  NOT NULL,
    `receiver_name`   varchar(20)  NOT NULL,
    `receiver_phone`  varchar(20)  NOT NULL,
    `request_message` varchar(255) NULL,
    `state`           varchar(20)  NOT NULL,
    `created_at`      datetime     NULL,
    `modified_at`     datetime     NULL,
    PRIMARY KEY (`id`)
);

DROP TABLE IF EXISTS `category`;

CREATE TABLE `category`
(
    `id`          bigint      NOT NULL AUTO_INCREMENT,
    `name`        varchar(50) NOT NULL,
    `created_at`  datetime    NULL,
    `modified_at` datetime    NULL,
    PRIMARY KEY (`id`)
);

DROP TABLE IF EXISTS `order_line`;

CREATE TABLE `order_line`
(
    `id`          bigint       NOT NULL AUTO_INCREMENT,
    `order_id`    varchar(255) NOT NULL,
    `product_id`  bigint       NOT NULL,
    `quantity`    int          NOT NULL,
    `price`       bigint       NOT NULL,
    `amounts`     bigint       NOT NULL,
    `created_at`  datetime     NULL,
    `modified_at` datetime     NULL,
    PRIMARY KEY (`id`)
);

DROP TABLE IF EXISTS `cart_product`;

CREATE TABLE `cart_product`
(
    `id`          bigint   NOT NULL AUTO_INCREMENT,
    `cart_id`     bigint   NOT NULL,
    `product_id`  bigint   NOT NULL,
    `quantity`    int      NOT NULL,
    `created_at`  datetime NULL,
    `modified_at` datetime NULL,
    PRIMARY KEY (`id`)
);

DROP TABLE IF EXISTS `user`;

CREATE TABLE `user`
(
    `id`          bigint       NOT NULL AUTO_INCREMENT,
    `username`    varchar(50)  NULL COMMENT 'provider_providerId',
    `name`        varchar(20)  NULL,
    `email`       varchar(100) NULL,
    `role`        varchar(10)  NOT NULL,
    `provider`    varchar(10)  NULL,
    `created_at`  datetime     NULL,
    `modified_at` datetime     NULL,
    PRIMARY KEY (`id`)
);

DROP TABLE IF EXISTS `cart`;

CREATE TABLE `cart`
(
    `id`          bigint   NOT NULL AUTO_INCREMENT,
    `user_id`     bigint   NOT NULL,
    `created_at`  datetime NULL,
    `modified_at` datetime NULL,
    PRIMARY KEY (`id`)
);

DROP TABLE IF EXISTS `payment`;

CREATE TABLE `payment`
(
    `id`          bigint       NOT NULL AUTO_INCREMENT,
    `order_id`    varchar(255) NOT NULL,
    `amount`      bigint       NOT NULL,
    `payment_key` varchar(255) NOT NULL,
    `type`        varchar(20)  NOT NULL,
    `status`      varchar(20)  NOT NULL,
    `created_at`  datetime     NULL,
    `modified_at` datetime     NULL,
    PRIMARY KEY (`id`)
);

DROP TABLE IF EXISTS `address_book`;

CREATE TABLE `address_book`
(
    `id`              bigint       NOT NULL AUTO_INCREMENT,
    `user_id`         bigint       NOT NULL,
    `road_address`    varchar(100) NOT NULL,
    `address_detail`  varchar(100) NOT NULL,
    `zipcode`         varchar(50)  NOT NULL,
    `receiver_name`   varchar(20)  NOT NULL,
    `receiver_phone`  varchar(20)  NOT NULL,
    `request_message` varchar(255) NULL,
    `created_at`      datetime     NULL,
    `modified_at`     datetime     NULL,
    PRIMARY KEY (`id`)
);

DROP TABLE IF EXISTS `delivery`;

CREATE TABLE `delivery`
(
    `id`              bigint       NOT NULL AUTO_INCREMENT,
    `order_id`        varchar(255) NOT NULL,
    `road_address`    varchar(100) NOT NULL,
    `address_detail`  varchar(100) NOT NULL,
    `zipcode`         varchar(50)  NOT NULL,
    `receiver_name`   varchar(20)  NOT NULL,
    `receiver_phone`  varchar(20)  NOT NULL,
    `request_message` varchar(255) NULL,
    `status`          varchar(20)  NOT NULL,
    `created_at`      datetime     NULL,
    `modified_at`     datetime     NULL,
    PRIMARY KEY (`id`)
);
