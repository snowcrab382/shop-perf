DROP TABLE IF EXISTS `product`;
DROP TABLE IF EXISTS `order`;
DROP TABLE IF EXISTS `order_product`;
DROP TABLE IF EXISTS `category`;
DROP TABLE IF EXISTS `cart`;
DROP TABLE IF EXISTS `cart_product`;
DROP TABLE IF EXISTS `user`;
DROP TABLE IF EXISTS `payment`;

CREATE TABLE `product`
(
    `id`          bigint       NOT NULL AUTO_INCREMENT,
    `category_id` bigint       NOT NULL,
    `name`        varchar(30)  NOT NULL,
    `description` varchar(255) NOT NULL,
    `price`       bigint       NOT NULL,
    `stock`       bigint       NOT NULL,
    `created_at`  datetime     NULL,
    `modified_at` datetime     NULL,
    primary key (`id`)
);

CREATE TABLE `order`
(
    `id`           bigint       NOT NULL AUTO_INCREMENT,
    `user_id`      bigint       NOT NULL,
    `datetime`     date         NOT NULL,
    `total_amount` bigint       NOT NULL,
    `status`       varchar(255) NOT NULL,
    `created_at`   datetime     NULL,
    `modified_at`  datetime     NULL,
    primary key (`id`)
);

CREATE TABLE `category`
(
    `id`          bigint      NOT NULL AUTO_INCREMENT,
    `name`        varchar(50) NOT NULL,
    `created_at`  datetime    NULL,
    `modified_at` datetime    NULL,
    primary key (`id`)
);

CREATE TABLE `order_product`
(
    `id`          bigint   NOT NULL AUTO_INCREMENT,
    `order_id`    bigint   NOT NULL,
    `product_id`  bigint   NOT NULL,
    `quantity`    int      NOT NULL,
    `price`       bigint   NOT NULL,
    `created_at`  datetime NULL,
    `modified_at` datetime NULL,
    primary key (`id`)
);

CREATE TABLE `cart_product`
(
    `id`          bigint   NOT NULL AUTO_INCREMENT,
    `cart_id`     bigint   NOT NULL,
    `product_id`  bigint   NOT NULL,
    `quantity`    int      NOT NULL,
    `price`       bigint   NOT NULL,
    `created_at`  datetime NULL,
    `modified_at` datetime NULL,
    primary key (`id`)
);

CREATE TABLE `user`
(
    `id`          bigint       NOT NULL AUTO_INCREMENT,
    `username`    varchar(255) NULL COMMENT 'provider_providerId',
    `name`        varchar(10)  NULL,
    `email`       varchar(30)  NULL,
    `role`        varchar(10)  NOT NULL DEFAULT 'ROLE_USER',
    `provider`    varchar(10)  NULL,
    `created_at`  datetime     NULL,
    `modified_at` datetime     NULL,
    primary key (`id`)
);

CREATE TABLE `cart`
(
    `id`          bigint   NOT NULL AUTO_INCREMENT,
    `user_id`     bigint   NOT NULL,
    `created_at`  datetime NULL,
    `modified_at` datetime NULL,
    primary key (`id`)
);

CREATE TABLE `payment`
(
    `id`          bigint       NOT NULL AUTO_INCREMENT,
    `order_id`    bigint       NOT NULL,
    `amount`      bigint       NOT NULL,
    `status`      varchar(255) NOT NULL,
    `created_at`  datetime     NULL,
    `modified_at` datetime     NULL,
    primary key (`id`)
);