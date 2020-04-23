CREATE SCHEMA `statsbotdb` DEFAULT CHARACTER SET utf8 COLLATE utf8_unicode_ci ;

USE `statsbotdb`;

CREATE TABLE `bot_users` (
  `user_id` int(11) NOT NULL,
  `first_name` varchar(45) NOT NULL,
  `last_name` varchar(45) NOT NULL,
  `user_name` varchar(45) NOT NULL,
  `subscribed` varchar(8) NOT NULL,
  PRIMARY KEY (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

CREATE TABLE `answers` (
  `answer_id` int(11) NOT NULL AUTO_INCREMENT,
  `answer_time` datetime DEFAULT CURRENT_TIMESTAMP,
  `answer_flag` varchar(8) NOT NULL,
  `user_id` int(11) NOT NULL,
  PRIMARY KEY (`answer_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

CREATE TABLE `messages` (
  `message_id` int(11) NOT NULL,
  `processed_flag` varchar(8) NOT NULL DEFAULT 'N',
  `user_id` int(11) NOT NULL,
  PRIMARY KEY (`message_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;