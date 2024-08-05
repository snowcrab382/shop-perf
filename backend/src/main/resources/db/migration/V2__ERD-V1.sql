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
    `id`           bigint   NOT NULL AUTO_INCREMENT,
    `user_id`      bigint   NOT NULL,
    `total_amount` bigint   NOT NULL,
    `created_at`   datetime NULL,
    `modified_at`  datetime NULL,
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

DROP TABLE IF EXISTS `order_product`;

CREATE TABLE `order_product`
(
    `id`          bigint   NOT NULL AUTO_INCREMENT,
    `order_id`    bigint   NOT NULL,
    `product_id`  bigint   NOT NULL,
    `quantity`    int      NOT NULL,
    `price`       bigint   NOT NULL,
    `created_at`  datetime NULL,
    `modified_at` datetime NULL,
    PRIMARY KEY (`id`)
);

DROP TABLE IF EXISTS `cart_product`;

CREATE TABLE `cart_product`
(
    `id`          bigint   NOT NULL AUTO_INCREMENT,
    `cart_id`     bigint   NOT NULL,
    `product_id`  bigint   NOT NULL,
    `quantity`    int      NOT NULL,
    `price`       bigint   NOT NULL,
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
    `id`          bigint      NOT NULL AUTO_INCREMENT,
    `order_id`    bigint      NOT NULL,
    `amount`      bigint      NOT NULL,
    `type`        varchar(20) NOT NULL,
    `status`      varchar(20) NOT NULL,
    `created_at`  datetime    NULL,
    `modified_at` datetime    NULL,
    PRIMARY KEY (`id`)
);

DROP TABLE IF EXISTS `recipient`;

CREATE TABLE `recipient`
(
    `id`             bigint       NOT NULL AUTO_INCREMENT,
    `user_id`        bigint       NOT NULL,
    `name`           varchar(20)  NOT NULL,
    `phone`          varchar(15)  NOT NULL,
    `street_address` varchar(100) NOT NULL,
    `detail_address` varchar(50)  NOT NULL,
    `zipcode`        varchar(50)  NOT NULL,
    `created_at`     datetime     NULL,
    `modified_at`    datetime     NULL,
    PRIMARY KEY (`id`)
);

DROP TABLE IF EXISTS `delivery`;

CREATE TABLE `delivery`
(
    `id`             bigint       NOT NULL AUTO_INCREMENT,
    `order_id`       bigint       NOT NULL,
    `name`           varchar(20)  NOT NULL,
    `phone`          varchar(15)  NOT NULL,
    `street_address` varchar(100) NOT NULL,
    `detail_address` varchar(50)  NOT NULL,
    `zipcode`        varchar(50)  NOT NULL,
    `request`        text         NULL,
    `status`         varchar(20)  NOT NULL,
    `created_at`     datetime     NULL,
    `modified_at`    datetime     NULL,
    PRIMARY KEY (`id`)
);

DROP TABLE IF EXISTS `order_history`;

CREATE TABLE `order_history`
(
    `id`          bigint      NOT NULL AUTO_INCREMENT,
    `order_id`    bigint      NOT NULL,
    `status`      varchar(20) NOT NULL,
    `created_at`  datetime    NULL,
    `modified_at` datetime    NULL,
    PRIMARY KEY (`id`)
);
