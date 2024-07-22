CREATE TABLE `user`
(
    `id`       bigint       NOT NULL auto_increment,
    `username` varchar(255) NULL COMMENT 'provider_providerId',
    `name`     varchar(10)  NULL,
    `email`    varchar(30)  NULL,
    `role`     varchar(10)  NULL DEFAULT 'ROLE_USER',
    `provider` varchar(10)  NULL,
    primary key (`id`)
);
