/*
Navicat MySQL Data Transfer

Source Server         : local
Source Server Version : 50519
Source Host           : localhost:3306
Source Database       : gomoku

Target Server Type    : MYSQL
Target Server Version : 50519
File Encoding         : 65001

Date: 2018-04-27 22:58:26
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for conf_common
-- ----------------------------
DROP TABLE IF EXISTS `conf_common`;
CREATE TABLE `conf_common` (
  `id` smallint(5) unsigned NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `nid` varchar(30) NOT NULL COMMENT '唯一标识符',
  `value` varchar(2000) DEFAULT NULL COMMENT '参数值',
  `remark` varchar(50) NOT NULL COMMENT '参数描述',
  `create_time` int(11) DEFAULT NULL,
  `update_time` int(11) DEFAULT NULL,
  `del_flag` tinyint(2) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`),
  UNIQUE KEY `nid` (`nid`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8 COMMENT='参数配置';

-- ----------------------------
-- Records of conf_common
-- ----------------------------
INSERT INTO `conf_common` VALUES ('1', 'match_timeout', '20', '匹配超时时间（秒）', null, null, '0');
INSERT INTO `conf_common` VALUES ('2', 'first_name', '赵,钱,孙,李,周,吴,郑,王', '随机姓氏', null, null, '0');
INSERT INTO `conf_common` VALUES ('3', 'second_name', '基于,链接,节点,无界,线程,安全,队列', '随机名称', null, null, '0');

-- ----------------------------
-- Table structure for conf_title
-- ----------------------------
DROP TABLE IF EXISTS `conf_title`;
CREATE TABLE `conf_title` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `title_sort` int(11) DEFAULT '0' COMMENT '段位级别',
  `min_scr` int(11) DEFAULT '0' COMMENT '大于等于积分',
  `max_scr` int(11) DEFAULT '0' COMMENT '小于积分',
  `title` varchar(255) DEFAULT NULL COMMENT '段位名称',
  `create_uid` int(11) DEFAULT NULL COMMENT '创建人UID',
  `create_time` int(11) DEFAULT NULL COMMENT '创建时间',
  `update_uid` int(11) DEFAULT NULL COMMENT '更新人员',
  `update_time` int(11) DEFAULT NULL COMMENT '更新时间',
  `del_flag` tinyint(2) DEFAULT '0' COMMENT '删除标记 ',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=utf8mb4 COMMENT='段位配置';

-- ----------------------------
-- Records of conf_title
-- ----------------------------
INSERT INTO `conf_title` VALUES ('1', '0', '-30', '10', '1-1段', null, null, null, null, '0');
INSERT INTO `conf_title` VALUES ('2', '1', '10', '20', '1-2段', null, null, null, null, '0');
INSERT INTO `conf_title` VALUES ('3', '2', '20', '30', '1-3段', null, null, null, null, '0');
INSERT INTO `conf_title` VALUES ('4', '3', '30', '40', '2-1段', null, null, null, null, '0');
INSERT INTO `conf_title` VALUES ('5', '4', '40', '60', '2-2段', null, null, null, null, '0');
INSERT INTO `conf_title` VALUES ('6', '5', '60', '80', '2-3段', null, null, null, null, '0');
INSERT INTO `conf_title` VALUES ('7', '6', '80', '100', '3-1段', null, null, null, null, '0');
INSERT INTO `conf_title` VALUES ('8', '7', '100', '120', '3-2段', null, null, null, null, '0');
INSERT INTO `conf_title` VALUES ('9', '8', '120', '150', '3-3段', null, null, null, null, '0');
INSERT INTO `conf_title` VALUES ('10', '9', '150', '180', '4-1段', null, null, null, null, '0');
INSERT INTO `conf_title` VALUES ('11', '10', '180', '210', '4-2段', null, null, null, null, '0');
INSERT INTO `conf_title` VALUES ('12', '11', '210', '240', '4-3段', null, null, null, null, '0');

-- ----------------------------
-- Table structure for usr_admin
-- ----------------------------
DROP TABLE IF EXISTS `usr_admin`;
CREATE TABLE `usr_admin` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT COMMENT '管理员ID ',
  `user_name` varchar(32) NOT NULL COMMENT '管理员用户名',
  `password` varchar(100) DEFAULT NULL COMMENT '密码',
  `nick_name` varchar(32) DEFAULT NULL COMMENT '昵称',
  `role` varchar(500) DEFAULT NULL COMMENT '用户角色',
  `icon` int(5) DEFAULT NULL COMMENT '头像',
  `phone` varchar(15) DEFAULT NULL COMMENT '手机',
  `email` varchar(50) DEFAULT NULL COMMENT '邮箱',
  `count` mediumint(8) unsigned DEFAULT '0' COMMENT '登录次数',
  `create_time` int(11) DEFAULT NULL COMMENT '创建时间',
  `create_uid` int(11) DEFAULT NULL COMMENT '创建者',
  `update_uid` int(11) DEFAULT NULL COMMENT '更新者',
  `update_time` int(11) DEFAULT NULL COMMENT '更新时间',
  `del_flag` tinyint(2) DEFAULT '0' COMMENT '删除标记',
  PRIMARY KEY (`id`),
  KEY `name` (`user_name`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8 COMMENT='管理员表';

-- ----------------------------
-- Records of usr_admin
-- ----------------------------
INSERT INTO `usr_admin` VALUES ('1', 'admin', 'zV6nPNWPgn+nju9xl7juYGyZsuY=', '管理员', '0', null, null, null, '0', null, null, null, null, '0');

-- ----------------------------
-- Table structure for usr_game_info
-- ----------------------------
DROP TABLE IF EXISTS `usr_game_info`;
CREATE TABLE `usr_game_info` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `uid` int(11) DEFAULT '0' COMMENT '用户id',
  `coin` int(11) DEFAULT '0' COMMENT '金币',
  `score` int(11) DEFAULT '0' COMMENT '积分',
  `title_sort` int(5) DEFAULT NULL COMMENT '段位级别',
  `win_count` int(11) DEFAULT '0' COMMENT '赢的局数',
  `lose_count` int(11) DEFAULT '0' COMMENT '输的局数',
  `title` varchar(255) DEFAULT NULL COMMENT '段位',
  `create_uid` int(11) DEFAULT NULL COMMENT '创建人UID',
  `create_time` int(11) DEFAULT NULL COMMENT '创建时间',
  `update_uid` int(11) DEFAULT NULL COMMENT '更新人员',
  `update_time` int(11) DEFAULT NULL COMMENT '更新时间',
  `del_flag` tinyint(2) DEFAULT '0' COMMENT '删除标记 ',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=18 DEFAULT CHARSET=utf8mb4 COMMENT='用户游戏信息';

-- ----------------------------
-- Records of usr_game_info
-- ----------------------------
INSERT INTO `usr_game_info` VALUES ('1', '5', '0', '0', '0', '0', '0', '1-1段', null, null, null, null, '0');
INSERT INTO `usr_game_info` VALUES ('2', '6', '0', '0', '0', '0', '0', '1-1段', null, null, null, null, '0');
INSERT INTO `usr_game_info` VALUES ('3', '7', '0', '0', '0', '0', '0', '1-1段', null, null, null, null, '0');
INSERT INTO `usr_game_info` VALUES ('4', '8', '0', '0', '0', '0', '0', '1-1段', null, null, null, null, '0');
INSERT INTO `usr_game_info` VALUES ('5', '9', '0', '0', '0', '0', '0', '1-1段', null, null, null, null, '0');
INSERT INTO `usr_game_info` VALUES ('6', '10', '0', '0', '0', '0', '0', '1-1段', null, null, null, null, '0');
INSERT INTO `usr_game_info` VALUES ('7', '11', '0', '0', '0', '0', '0', '1-1段', null, null, null, null, '0');
INSERT INTO `usr_game_info` VALUES ('8', '12', '0', '0', '0', '0', '0', '1-1段', null, null, null, null, '0');
INSERT INTO `usr_game_info` VALUES ('9', '13', '0', '0', '0', '0', '0', '1-1段', null, null, null, null, '0');
INSERT INTO `usr_game_info` VALUES ('10', '14', '0', '0', '0', '0', '0', '1-1段', null, null, null, null, '0');
INSERT INTO `usr_game_info` VALUES ('11', '15', '0', '0', '0', '0', '0', '1-1段', null, null, null, null, '0');
INSERT INTO `usr_game_info` VALUES ('12', '16', '0', '0', '0', '0', '0', '1-1段', null, null, null, null, '0');
INSERT INTO `usr_game_info` VALUES ('13', '17', '0', '0', '0', '0', '0', '1-1段', null, null, null, null, '0');
INSERT INTO `usr_game_info` VALUES ('14', '18', '0', '0', '0', '0', '0', '1-1段', null, null, null, null, '0');
INSERT INTO `usr_game_info` VALUES ('15', '19', '0', '0', '0', '0', '0', '1-1段', null, null, null, null, '0');
INSERT INTO `usr_game_info` VALUES ('16', '20', '0', '0', '0', '0', '0', '1-1段', null, null, null, null, '0');
INSERT INTO `usr_game_info` VALUES ('17', '21', '0', '0', '0', '0', '0', '1-1段', null, null, null, null, '0');

-- ----------------------------
-- Table structure for usr_user
-- ----------------------------
DROP TABLE IF EXISTS `usr_user`;
CREATE TABLE `usr_user` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT COMMENT '用户ID ',
  `user_name` varchar(32) NOT NULL COMMENT '用户名',
  `password` varchar(100) DEFAULT NULL COMMENT '密码',
  `nick_name` varchar(32) DEFAULT NULL COMMENT '昵称',
  `role` varchar(500) DEFAULT NULL COMMENT '用户角色',
  `icon` int(5) DEFAULT NULL COMMENT '头像',
  `phone` varchar(15) DEFAULT NULL COMMENT '手机',
  `email` varchar(50) DEFAULT NULL COMMENT '邮箱',
  `count` mediumint(8) unsigned DEFAULT '0' COMMENT '登录次数',
  `create_time` int(11) DEFAULT NULL COMMENT '创建时间',
  `create_uid` int(11) DEFAULT NULL COMMENT '创建者',
  `update_uid` int(11) DEFAULT NULL COMMENT '更新者',
  `update_time` int(11) DEFAULT NULL COMMENT '更新时间',
  `del_flag` tinyint(2) DEFAULT '0' COMMENT '删除标记',
  PRIMARY KEY (`id`),
  KEY `name` (`user_name`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=22 DEFAULT CHARSET=utf8 COMMENT='用户表';

-- ----------------------------
-- Records of usr_user
-- ----------------------------
INSERT INTO `usr_user` VALUES ('2', 'ⵆ鶀䋐웟분겄퇫俎䫏ꞣ', null, 'ⵆ鶀䋐웟분겄퇫俎䫏ꞣ', null, null, null, null, '0', null, null, null, null, '0');
INSERT INTO `usr_user` VALUES ('3', 'gImfRRKBVG', null, 'gImfRRKBVG', null, null, null, null, '0', null, null, null, null, '0');
INSERT INTO `usr_user` VALUES ('4', 'GFUewFRssC', null, 'GFUewFRssC', null, null, null, null, '0', null, null, null, null, '0');
INSERT INTO `usr_user` VALUES ('5', 'ZhZtDICwhQ', null, 'ZhZtDICwhQ', null, null, null, null, '0', null, null, null, null, '0');
INSERT INTO `usr_user` VALUES ('6', 'XWkvFqWrDl', null, 'XWkvFqWrDl', null, null, null, null, '0', null, null, null, null, '0');
INSERT INTO `usr_user` VALUES ('7', 'KXkXmkhxmB', null, 'KXkXmkhxmB', null, null, null, null, '0', null, null, null, null, '0');
INSERT INTO `usr_user` VALUES ('8', 'DCJJTXHirn', null, 'DCJJTXHirn', null, null, null, null, '0', null, null, null, null, '0');
INSERT INTO `usr_user` VALUES ('9', 'VIBhHcTQco', null, 'VIBhHcTQco', null, null, null, null, '0', null, null, null, null, '0');
INSERT INTO `usr_user` VALUES ('10', 'gCcCGXeiuH', null, 'gCcCGXeiuH', null, null, null, null, '0', null, null, null, null, '0');
INSERT INTO `usr_user` VALUES ('11', 'tozgCkZFoy', null, 'tozgCkZFoy', null, null, null, null, '0', null, null, null, null, '0');
INSERT INTO `usr_user` VALUES ('12', 'rFncEpPVLc', null, 'rFncEpPVLc', null, null, null, null, '0', null, null, null, null, '0');
INSERT INTO `usr_user` VALUES ('13', 'uunTimhCzs', null, 'uunTimhCzs', null, null, null, null, '0', null, null, null, null, '0');
INSERT INTO `usr_user` VALUES ('14', 'nHyXlRdOut', null, 'nHyXlRdOut', null, null, null, null, '0', null, null, null, null, '0');
INSERT INTO `usr_user` VALUES ('15', 'tgnHRMfGCf', null, 'tgnHRMfGCf', null, null, null, null, '0', null, null, null, null, '0');
INSERT INTO `usr_user` VALUES ('16', 'tksARSOxlU', null, 'tksARSOxlU', null, null, null, null, '0', null, null, null, null, '0');
INSERT INTO `usr_user` VALUES ('17', 'mJMgfJgjyM', null, 'mJMgfJgjyM', null, null, null, null, '0', null, null, null, null, '0');
INSERT INTO `usr_user` VALUES ('18', 'XiveqgbBOx', null, 'XiveqgbBOx', null, null, null, null, '0', null, null, null, null, '0');
INSERT INTO `usr_user` VALUES ('19', 'cEANrMSkYE', null, 'cEANrMSkYE', null, null, null, null, '0', null, null, null, null, '0');
INSERT INTO `usr_user` VALUES ('20', 'btKeBuNEeM', null, 'btKeBuNEeM', null, null, null, null, '0', null, null, null, null, '0');
INSERT INTO `usr_user` VALUES ('21', 'gleaHkbbMV', null, '王安全', null, null, null, null, '0', null, null, null, null, '0');
SET FOREIGN_KEY_CHECKS=1;
