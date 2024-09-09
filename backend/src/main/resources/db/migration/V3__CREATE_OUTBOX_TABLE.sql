DROP TABLE IF EXISTS `outbox`;

CREATE TABLE `outbox`
(
    `id`          bigint       NOT NULL AUTO_INCREMENT,
    `order_id`    varchar(255) NOT NULL,
    `status`      varchar(20)  NOT NULL,
    `created_at`  datetime     NULL,
    `modified_at` datetime     NULL,
    PRIMARY KEY (`id`)
);
