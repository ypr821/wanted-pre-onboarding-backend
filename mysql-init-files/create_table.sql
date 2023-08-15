--
-- docker 접속 root user 생성
--

CREATE
USER 'root'@'%' identified BY '54321';

SELECT host, user FROM user;

GRANT ALL ON *.*TO'root'@'%';
FLUSH PRIVILEGES;

CREATE
DATABASE
    IF NOT EXISTS `wanted_assignment`
    DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci;

USE
`wanted_assignment`;


--
-- Table structure for table `user`
--

DROP TABLE IF EXISTS `user`;
CREATE TABLE `user`
(
    `user_id`   bigint       NOT NULL AUTO_INCREMENT,
    `email`     varchar(100) NOT NULL,
    `password`  varchar(100) NOT NULL,
    `create_dt` datetime     NOT NULL,
    `update_dt` datetime DEFAULT NULL,
    `delete_dt` datetime DEFAULT NULL,
    PRIMARY KEY (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Table structure for table `post`
--

DROP TABLE IF EXISTS `post`;
CREATE TABLE `post`
(
    `post_id`   bigint       NOT NULL AUTO_INCREMENT,
    `user_id`   bigint       NOT NULL,
    `title`     varchar(200) NOT NULL,
    `content`   text         NOT NULL,
    `create_dt` datetime     NOT NULL,
    `update_dt` datetime DEFAULT NULL,
    `delete_dt` datetime DEFAULT NULL,
    PRIMARY KEY (`post_id`),
    KEY         `FK_user_TO_board_1` (`user_id`),
    CONSTRAINT `FK_user_TO_board_1` FOREIGN KEY (`user_id`) REFERENCES `user` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
