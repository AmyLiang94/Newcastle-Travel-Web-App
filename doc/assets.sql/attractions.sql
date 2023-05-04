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

 Date: 04/05/2023 17:10:25
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for attractions
-- ----------------------------
DROP TABLE IF EXISTS `attractions`;
CREATE TABLE `attractions` (
  `attraction_id` int NOT NULL AUTO_INCREMENT,
  `attraction_name` varchar(255) COLLATE utf8mb4_general_ci NOT NULL,
  `description` text COLLATE utf8mb4_general_ci,
  `category` varchar(255) COLLATE utf8mb4_general_ci NOT NULL,
  `latitude` decimal(10,8) NOT NULL,
  `longitude` decimal(11,8) NOT NULL,
  `opening_hours` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `ticket_price` decimal(10,2) DEFAULT NULL,
  `image_url` varchar(255) COLLATE utf8mb4_general_ci NOT NULL,
  `attr_rating` double NOT NULL,
  `wheelchair_allow` tinyint(1) DEFAULT NULL,
  `pram_allow` tinyint(1) DEFAULT NULL,
  `hearing_allow` tinyint(1) DEFAULT NULL,
  `postcode` varchar(16) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `address` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `place_id` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL,
  PRIMARY KEY (`attraction_id`)
) ENGINE=InnoDB AUTO_INCREMENT=991 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- ----------------------------
-- Records of attractions
-- ----------------------------
BEGIN;
INSERT INTO `attractions` VALUES (1, 'newtest', 'test', 'History', 24.98877800, 109.23232300, '10', 100.00, 'http://', 4.5, 1, 1, 1, NULL, NULL, NULL);
INSERT INTO `attractions` VALUES (2, 'test', 'test', 'Nature', 24.98877800, 109.23232300, '10', 100.00, 'http://', 3.6, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `attractions` VALUES (3, 'test', 'test', 'Nature', 24.98877800, 109.23232300, '10', 100.00, 'http://', 3.6, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `attractions` VALUES (6, 'test', 'test', 'Nature', 24.98877800, 109.23232300, '10', 100.00, 'http://', 3.6, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `attractions` VALUES (906, 'Newcastle University', 'A good University', 'Historic', 54.97830000, -1.61780000, '0900', 19000.00, 'ncl.ac.uk', 4, 1, 1, 0, NULL, NULL, NULL);
INSERT INTO `attractions` VALUES (909, 'Newcastle University USB', 'A good University', 'Historic', 54.97366000, -1.62574400, '0900', 19000.00, 'ncl.ac.uk', 4, 1, 1, 0, NULL, NULL, NULL);
COMMIT;

SET FOREIGN_KEY_CHECKS = 1;
