DROP TABLE IF EXISTS `user_coupon`;

CREATE TABLE `user_coupon`
(
    `id`          bigint   NOT NULL AUTO_INCREMENT,
    `user_id`     bigint   NOT NULL,
    `coupon_id`   bigint   NOT NULL,
    `is_used`     boolean  NOT NULL,
    `created_at`  datetime NULL,
    `modified_at` datetime NULL,
    PRIMARY KEY (`id`)
);
