CREATE TABLE user
(
    id       BIGINT      NOT NULL AUTO_INCREMENT,
    username VARCHAR(50) NOT NULL,
    name     VARCHAR(30) NOT NULL,
    email    VARCHAR(30) NOT NULL,
    role     VARCHAR(30) NOT NULL,
    PRIMARY KEY (id)
);