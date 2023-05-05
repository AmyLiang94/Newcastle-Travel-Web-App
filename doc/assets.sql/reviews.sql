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

 Date: 04/05/2023 17:11:05
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for reviews
-- ----------------------------
DROP TABLE IF EXISTS `reviews`;
CREATE TABLE `reviews` (
  `review_id` int NOT NULL AUTO_INCREMENT,
  `attraction_id` int NOT NULL,
  `user_id` int NOT NULL,
  `rating` int NOT NULL,
  `review_text` text COLLATE utf8mb4_general_ci,
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`review_id`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- ----------------------------
-- Records of reviews
-- ----------------------------
BEGIN;
INSERT INTO `reviews` VALUES (4, 1, 1, 1, 'bad', '2023-04-30 00:06:29', '2023-04-30 00:06:29');
INSERT INTO `reviews` VALUES (5, 1, 1, 0, 'so bad', '2023-04-30 00:09:59', '2023-04-30 00:09:59');
INSERT INTO `reviews` VALUES (6, 1, 1, 5, 'good', '2023-04-30 00:10:24', '2023-04-30 00:10:24');
INSERT INTO `reviews` VALUES (7, 1, 5, 5, 'good', '2023-05-04 02:47:27', '2023-05-04 02:47:27');
COMMIT;

SET FOREIGN_KEY_CHECKS = 1;
