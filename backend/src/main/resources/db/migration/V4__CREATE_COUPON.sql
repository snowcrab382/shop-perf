DROP TABLE IF EXISTS `coupon`;

CREATE TABLE `coupon`
(
    `id`               bigint       NOT NULL AUTO_INCREMENT,
    `coupon_type`      varchar(255) NOT NULL,
    `discount_type`    varchar(255) NOT NULL,
    `discount_amount`  int          NULL,
    `discount_percent` int          NULL,
    `total_count`      int          NOT NULL,
    `remaining_count`  int          NOT NULL,
    `started_at`       datetime     NOT NULL,
    `expired_at`       datetime     NOT NULL,
    `created_at`       datetime     NULL,
    `modified_at`      datetime     NULL,
    PRIMARY KEY (`id`)
);
