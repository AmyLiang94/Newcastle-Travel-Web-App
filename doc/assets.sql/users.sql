/*
 Navicat MySQL Data Transfer

 Source Server         : cloud
 Source Server Type    : MySQL
 Source Server Version : 80024
 Source Host           : 1.12.235.241:3306
 Source Schema         : tour

 Target Server Type    : MySQL
 Target Server Version : 80024
 File Encoding         : 65001

 Date: 04/05/2023 17:11:23
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for users
-- ----------------------------
DROP TABLE IF EXISTS `users`;
CREATE TABLE `users` (
  `user_id` int NOT NULL AUTO_INCREMENT,
  `email` varchar(255) COLLATE utf8mb4_general_ci NOT NULL,
  `username` varchar(255) COLLATE utf8mb4_general_ci NOT NULL,
  `password` varchar(255) COLLATE utf8mb4_general_ci NOT NULL,
  `salt` varchar(255) COLLATE utf8mb4_general_ci NOT NULL,
  `created_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `confirm_code` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `activation_time` datetime DEFAULT NULL,
  `is_valid` tinyint(1) DEFAULT NULL,
  `verification_code` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL,
  PRIMARY KEY (`user_id`),
  UNIQUE KEY `email` (`email`),
  UNIQUE KEY `username` (`username`)
) ENGINE=InnoDB AUTO_INCREMENT=15 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- ----------------------------
-- Records of users
-- ----------------------------
BEGIN;
INSERT INTO `users` VALUES (6, 'b9068509@ncl.ac.uk', 'sada999', 'a0ec33a3fe2692999538e97dab681f7d', 'o8jyhd', '2023-04-30 05:01:09', '2023-05-02 00:08:43', '1652417760510218240', '2023-04-30 16:01:09', 1, '123456');
INSERT INTO `users` VALUES (8, 'c2040629@ncl.ac.uk', 'asdasd666666', 'b441e9c28a6b048f55d0ec1d5a4f58d1', 'hcld6t', '2023-04-30 23:46:55', '2023-05-02 00:08:42', '1652701065457569792', '2023-05-01 10:46:54', 1, '123456');
INSERT INTO `users` VALUES (11, '931654949@qq.com', 'lyl6666666', 'b72f9e3cc0bc1b8f31523c26e0415ca4', '2ec33c', '2023-05-04 22:39:30', '2023-05-04 22:59:17', '1654133653854359552', '2023-05-05 09:39:30', 1, NULL);
INSERT INTO `users` VALUES (13, '571379772@qq.com', 'lyl66666668', '14848e1448003c6cd3cb405751bb271a', '3vbd3e', '2023-05-04 22:41:17', '2023-05-04 22:41:35', '1654134102745550848', '2023-05-05 09:41:17', 1, NULL);
INSERT INTO `users` VALUES (14, '1226145149@qq.com', 'lasdas666', '832be604b1728960013f103479a1dbb7', 'swm0o7', '2023-05-04 23:00:42', '2023-05-04 23:01:13', '1654138992033140736', '2023-05-05 10:00:43', 1, NULL);
COMMIT;

SET FOREIGN_KEY_CHECKS = 1;
