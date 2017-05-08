/*
Navicat MySQL Data Transfer

Source Server         : LocalMySQL
Source Server Version : 50716
Source Host           : localhost:3306
Source Database       : bip

Target Server Type    : MYSQL
Target Server Version : 50716
File Encoding         : 65001

Date: 2017-05-08 08:10:01
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for t_action_monitor
-- ----------------------------
DROP TABLE IF EXISTS `t_action_monitor`;
CREATE TABLE `t_action_monitor` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `create_date` datetime DEFAULT NULL,
  `modify_date` datetime DEFAULT NULL,
  `hazard_level` int(4) DEFAULT NULL,
  `ip_address` varchar(50) DEFAULT NULL,
  `mac_address` varchar(50) DEFAULT NULL,
  `specific_action` varchar(100) DEFAULT NULL,
  `target` varchar(100) DEFAULT NULL,
  `user_id` bigint(20) DEFAULT NULL,
  `effect` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK778cx0a2e229a29anx9i682ar` (`user_id`),
  CONSTRAINT `FK778cx0a2e229a29anx9i682ar` FOREIGN KEY (`user_id`) REFERENCES `t_user` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_action_monitor
-- ----------------------------

-- ----------------------------
-- Table structure for t_code_record
-- ----------------------------
DROP TABLE IF EXISTS `t_code_record`;
CREATE TABLE `t_code_record` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `user_id` bigint(20) DEFAULT NULL,
  `code` varchar(20) DEFAULT NULL,
  `type` int(11) DEFAULT NULL,
  `create_date` datetime DEFAULT NULL,
  `modify_date` datetime DEFAULT NULL,
  `expired_date` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_code_record
-- ----------------------------

-- ----------------------------
-- Table structure for t_detail_control
-- ----------------------------
DROP TABLE IF EXISTS `t_detail_control`;
CREATE TABLE `t_detail_control` (
  `id` bigint(20) NOT NULL,
  `role_id` bigint(20) DEFAULT NULL,
  `entity_class` varchar(50) COLLATE utf8_bin DEFAULT NULL,
  `unavailable_columns` varchar(200) COLLATE utf8_bin DEFAULT NULL,
  `filter_rules` varchar(250) COLLATE utf8_bin DEFAULT NULL,
  `priority` smallint(6) DEFAULT NULL,
  `enabled` tinyint(4) DEFAULT NULL,
  `modify_date` datetime DEFAULT NULL,
  `create_date` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

-- ----------------------------
-- Records of t_detail_control
-- ----------------------------
INSERT INTO `t_detail_control` VALUES ('1', '1', 'com.beamofsoul.springboot.entity.User', 'password,sex,status', null, '1', '1', '2016-12-08 14:47:57', '2016-12-08 14:47:57');

-- ----------------------------
-- Table structure for t_organization
-- ----------------------------
DROP TABLE IF EXISTS `t_organization`;
CREATE TABLE `t_organization` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `create_date` datetime DEFAULT NULL,
  `modify_date` datetime DEFAULT NULL,
  `available` bit(1) DEFAULT NULL,
  `descirption` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `name` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `sort` int(11) DEFAULT NULL,
  `parent_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKek1x02uw2s7yti5b17khbr418` (`parent_id`),
  CONSTRAINT `FKek1x02uw2s7yti5b17khbr418` FOREIGN KEY (`parent_id`) REFERENCES `t_organization` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

-- ----------------------------
-- Records of t_organization
-- ----------------------------

-- ----------------------------
-- Table structure for t_permission
-- ----------------------------
DROP TABLE IF EXISTS `t_permission`;
CREATE TABLE `t_permission` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `url` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `action` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `group` varchar(50) COLLATE utf8_bin DEFAULT NULL,
  `parent_id` bigint(20) DEFAULT NULL,
  `resource_type` enum('menu','button') COLLATE utf8_bin DEFAULT NULL,
  `sort` bigint(20) DEFAULT NULL,
  `available` bit(1) DEFAULT NULL,
  `create_date` datetime DEFAULT NULL,
  `modify_date` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=29 DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

-- ----------------------------
-- Records of t_permission
-- ----------------------------
INSERT INTO `t_permission` VALUES ('1', '系统列表', 'sys/list', 'sys:list', '系统管理', '0', 'menu', '0', '', '2016-12-08 14:47:57', '2017-02-24 15:44:51');
INSERT INTO `t_permission` VALUES ('2', '系统添加', 'sys/add', 'sys:add', '系统管理', '1', 'button', '0', '', '2016-12-08 14:47:57', '2017-02-17 14:41:22');
INSERT INTO `t_permission` VALUES ('3', '系统删除', 'sys/delete', 'sys:delete', '系统管理', '1', 'button', '0', '', '2017-02-07 14:02:15', '2017-02-17 14:41:27');
INSERT INTO `t_permission` VALUES ('4', '系统修改', 'sys/update', 'sys:update', '系统管理', '1', 'button', '0', '', '2017-02-07 14:02:43', '2017-02-17 14:41:31');
INSERT INTO `t_permission` VALUES ('5', '用户列表', 'user/list', 'user:list', '用户管理', '1', 'menu', '1', '', '2016-12-08 14:47:57', '2016-12-08 14:47:57');
INSERT INTO `t_permission` VALUES ('6', '用户添加', 'user/add', 'user:add', '用户管理', '5', 'button', '1', '', '2016-12-08 14:47:57', '2017-02-17 14:41:52');
INSERT INTO `t_permission` VALUES ('7', '用户删除', 'user/delete', 'user:delete', '用户管理', '5', 'button', '1', '', '2016-12-08 14:47:57', '2017-02-17 14:41:56');
INSERT INTO `t_permission` VALUES ('8', '用户修改', 'user/update', 'user:update', '用户管理', '5', 'button', '1', '', '2016-12-08 14:47:57', '2017-02-17 14:41:59');
INSERT INTO `t_permission` VALUES ('9', '角色列表', 'role/list', 'role:list', '角色管理', '1', 'menu', '2', '', '2017-02-07 14:03:42', '2017-02-17 14:42:07');
INSERT INTO `t_permission` VALUES ('10', '角色添加', 'role/add', 'role:add', '角色管理', '9', 'button', '2', '', '2017-02-07 14:04:26', '2017-02-17 15:37:44');
INSERT INTO `t_permission` VALUES ('11', '角色删除', 'role/delete', 'role:delete', '角色管理', '9', 'button', '2', '', '2017-02-07 14:05:18', '2017-02-07 14:05:21');
INSERT INTO `t_permission` VALUES ('12', '角色修改', 'role/update', 'role:update', '角色管理', '9', 'button', '2', '', '2017-02-07 14:05:49', '2017-02-07 14:05:51');
INSERT INTO `t_permission` VALUES ('13', '角色分配', 'role/allot', 'role:allot', '角色管理', '9', 'button', '2', '', '2017-02-24 09:02:26', '2017-03-23 10:44:01');
INSERT INTO `t_permission` VALUES ('14', '角色用户列表', 'role/roleuser', 'role:roleuser', '角色管理', '9', 'menu', '2', '', '2017-02-24 09:05:10', '2017-02-24 09:05:10');
INSERT INTO `t_permission` VALUES ('15', '权限列表', 'permission/list', 'permission:list', '权限管理', '1', 'menu', '3', '', '2017-02-17 14:27:55', '2017-02-17 14:27:55');
INSERT INTO `t_permission` VALUES ('16', '权限增加', 'permission/add', 'permission:add', '权限管理', '15', 'button', '3', '', '2017-02-17 14:42:48', '2017-02-17 14:42:48');
INSERT INTO `t_permission` VALUES ('17', '权限修改', 'permission/update', 'permission:update', '权限管理', '15', 'button', '3', '', '2017-02-17 14:43:33', '2017-02-17 14:43:33');
INSERT INTO `t_permission` VALUES ('18', '权限删除', 'permission/delete', 'permission:delete', '权限管理', '15', 'button', '3', '', '2017-02-17 14:43:59', '2017-02-17 14:43:59');
INSERT INTO `t_permission` VALUES ('19', '角色权限分配', 'role/rolepermission', 'role:rolepermission', '角色管理', '9', 'button', '2', '', '2017-02-24 09:44:38', '2017-02-24 09:44:38');
INSERT INTO `t_permission` VALUES ('25', '系统复制', 'sys/copy', 'sys:copy', '系统管理', '1', 'button', '0', '', '2017-02-27 10:32:42', '2017-02-27 10:32:42');
INSERT INTO `t_permission` VALUES ('26', '用户复制', 'user/copy', 'user:copy', '用户管理', '5', 'button', '1', '', '2017-02-27 10:33:07', '2017-02-27 10:33:07');
INSERT INTO `t_permission` VALUES ('27', '角色复制', 'role/copy', 'role:copy', '角色管理', '9', 'button', '2', '', '2017-02-27 10:33:32', '2017-02-27 10:33:32');
INSERT INTO `t_permission` VALUES ('28', '权限复制', 'permission/copy', 'permission:copy', '权限管理', '15', 'button', '3', '', '2017-02-27 10:33:59', '2017-02-27 10:33:59');

-- ----------------------------
-- Table structure for t_role
-- ----------------------------
DROP TABLE IF EXISTS `t_role`;
CREATE TABLE `t_role` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `priority` int(11) DEFAULT NULL,
  `create_date` datetime DEFAULT NULL,
  `modify_date` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

-- ----------------------------
-- Records of t_role
-- ----------------------------
INSERT INTO `t_role` VALUES ('1', 'admin', '0', '2016-12-08 14:47:57', '2016-12-08 14:47:57');
INSERT INTO `t_role` VALUES ('2', 'manager', '1', '2016-12-08 14:47:57', '2016-12-08 14:47:57');
INSERT INTO `t_role` VALUES ('3', 'normal', '98', '2016-12-08 14:47:57', '2017-03-14 15:31:37');

-- ----------------------------
-- Table structure for t_role_permission
-- ----------------------------
DROP TABLE IF EXISTS `t_role_permission`;
CREATE TABLE `t_role_permission` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `permission_id` bigint(20) DEFAULT NULL,
  `role_id` bigint(20) DEFAULT NULL,
  `create_date` datetime DEFAULT NULL,
  `modify_date` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKjobmrl6dorhlfite4u34hciik` (`permission_id`),
  KEY `FK90j038mnbnthgkc17mqnoilu9` (`role_id`),
  CONSTRAINT `FK90j038mnbnthgkc17mqnoilu9` FOREIGN KEY (`role_id`) REFERENCES `t_role` (`id`),
  CONSTRAINT `FKjobmrl6dorhlfite4u34hciik` FOREIGN KEY (`permission_id`) REFERENCES `t_permission` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=389 DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

-- ----------------------------
-- Records of t_role_permission
-- ----------------------------
INSERT INTO `t_role_permission` VALUES ('1', '1', '1', '2017-02-07 14:33:56', '2017-02-07 14:33:56');
INSERT INTO `t_role_permission` VALUES ('2', '2', '1', '2017-02-07 14:33:56', '2017-02-07 14:33:56');
INSERT INTO `t_role_permission` VALUES ('3', '3', '1', '2017-02-07 14:33:56', '2017-02-07 14:33:56');
INSERT INTO `t_role_permission` VALUES ('27', '5', '2', '2017-02-23 10:53:39', '2017-02-23 10:53:39');
INSERT INTO `t_role_permission` VALUES ('31', '9', '2', '2017-02-23 10:53:39', '2017-02-23 10:53:39');
INSERT INTO `t_role_permission` VALUES ('35', '19', '1', '2017-02-27 09:10:59', '2017-02-27 09:11:01');
INSERT INTO `t_role_permission` VALUES ('356', '13', '1', '2017-02-27 09:15:33', '2017-02-27 09:15:33');
INSERT INTO `t_role_permission` VALUES ('357', '14', '1', '2017-02-27 09:15:33', '2017-02-27 09:15:33');
INSERT INTO `t_role_permission` VALUES ('361', '19', '2', '2017-02-27 09:18:05', '2017-02-27 09:18:05');
INSERT INTO `t_role_permission` VALUES ('363', '14', '2', '2017-02-27 09:18:05', '2017-02-27 09:18:05');
INSERT INTO `t_role_permission` VALUES ('364', '15', '2', '2017-02-27 09:18:05', '2017-02-27 09:18:05');
INSERT INTO `t_role_permission` VALUES ('365', '25', '1', '2017-02-27 10:34:41', '2017-02-27 10:34:41');
INSERT INTO `t_role_permission` VALUES ('388', '4', '1', '2017-03-23 15:15:36', '2017-03-23 15:15:36');

-- ----------------------------
-- Table structure for t_user
-- ----------------------------
DROP TABLE IF EXISTS `t_user`;
CREATE TABLE `t_user` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `username` varchar(20) COLLATE utf8_bin DEFAULT NULL,
  `password` varchar(20) COLLATE utf8_bin DEFAULT NULL,
  `nickname` varchar(20) COLLATE utf8_bin DEFAULT NULL,
  `email` varchar(50) COLLATE utf8_bin DEFAULT NULL,
  `phone` varchar(20) COLLATE utf8_bin DEFAULT NULL,
  `photo` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `status` tinyint(4) DEFAULT NULL,
  `create_date` datetime DEFAULT NULL,
  `modify_date` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_pu2s3uanurepbp3159l1ft4xo` (`nickname`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

-- ----------------------------
-- Records of t_user
-- ----------------------------
INSERT INTO `t_user` VALUES ('1', 'tom', '123456', 'Tom', 'tom@sina.com', '', 'tom.jpeg', '1', '2016-12-08 14:47:57', '2017-05-03 08:33:56');
INSERT INTO `t_user` VALUES ('2', 'jack', '123456', 'Jack', 'jack@163.com', '', '', '1', '2016-12-08 14:47:57', '2017-05-03 09:24:36');
INSERT INTO `t_user` VALUES ('3', 'rose', '1234561', 'Rose', 'rose@gmail.com', '', '', '1', '2016-12-08 14:47:57', '2017-05-03 09:24:54');

-- ----------------------------
-- Table structure for t_user_role
-- ----------------------------
DROP TABLE IF EXISTS `t_user_role`;
CREATE TABLE `t_user_role` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `role_id` bigint(20) DEFAULT NULL,
  `user_id` bigint(20) DEFAULT NULL,
  `create_date` datetime DEFAULT NULL,
  `modify_date` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKa9c8iiy6ut0gnx491fqx4pxam` (`role_id`),
  KEY `FKq5un6x7ecoef5w1n39cop66kl` (`user_id`),
  CONSTRAINT `FKa9c8iiy6ut0gnx491fqx4pxam` FOREIGN KEY (`role_id`) REFERENCES `t_role` (`id`),
  CONSTRAINT `FKq5un6x7ecoef5w1n39cop66kl` FOREIGN KEY (`user_id`) REFERENCES `t_user` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

-- ----------------------------
-- Records of t_user_role
-- ----------------------------
INSERT INTO `t_user_role` VALUES ('1', '1', '1', '2016-12-08 14:47:57', '2016-12-08 14:47:57');
INSERT INTO `t_user_role` VALUES ('2', '3', '2', '2017-01-17 10:19:41', '2017-01-17 10:19:44');
INSERT INTO `t_user_role` VALUES ('3', '2', '3', '2016-12-08 14:47:57', '2016-12-08 14:47:57');

-- ----------------------------
-- View structure for v_user_role_combine_role
-- ----------------------------
DROP VIEW IF EXISTS `v_user_role_combine_role`;
CREATE ALGORITHM=UNDEFINED DEFINER=`root`@`localhost` SQL SECURITY DEFINER VIEW `v_user_role_combine_role` AS select ifnull(group_concat(`tur`.`id` order by `tur`.`id` ASC separator ','),rand()) AS `id`,`tu`.`id` AS `user_id`,`tu`.`username` AS `username`,`tu`.`nickname` AS `nickname`,ifnull(group_concat(`tur`.`role_id` order by `tur`.`role_id` ASC separator ','),0) AS `role_id`,ifnull(group_concat(`tr`.`name` order by `tr`.`name` ASC separator ','),'') AS `role_name` from ((`t_user` `tu` left join `t_user_role` `tur` on((`tur`.`user_id` = `tu`.`id`))) left join `t_role` `tr` on((`tr`.`id` = `tur`.`role_id`))) group by `tu`.`id` ;

-- ----------------------------
-- Procedure structure for proc_getChildren
-- ----------------------------
DROP PROCEDURE IF EXISTS `proc_getChildren`;
DELIMITER ;;
CREATE DEFINER=`root`@`%` PROCEDURE `proc_getChildren`(IN parentId BIGINT,
	OUT children VARCHAR(1000))
BEGIN

	DECLARE temp VARCHAR(1000);
	
	SET children = '$';
	SET temp = cast(parentId as CHAR);

	WHILE temp is not null DO
		SET children = concat(children, ',', temp);
		SELECT group_concat(tc.id) INTO temp FROM t_column AS tc where FIND_IN_SET(tc.parent_id, temp) > 0;
	END WHILE;

	SET children = SUBSTRING(children FROM 3 FOR LENGTH(children));

END
;;
DELIMITER ;

-- ----------------------------
-- Function structure for getChildren
-- ----------------------------
DROP FUNCTION IF EXISTS `getChildren`;
DELIMITER ;;
CREATE DEFINER=`root`@`%` FUNCTION `getChildren`(`parentId` bigint) RETURNS varchar(1000) CHARSET utf8
    READS SQL DATA
BEGIN
	DECLARE sTemp VARCHAR(1000);
	DECLARE sTempChd VARCHAR(1000);

	SET sTemp = '$';
	SET sTempChd =cast(parentId as CHAR);

	WHILE sTempChd is not null DO
		SET sTemp = concat(sTemp,',',sTempChd);
		SELECT group_concat(tc.id) INTO sTempChd FROM t_column AS tc where FIND_IN_SET(tc.parent_id, sTempChd) > 0;
	END WHILE;
	
	SET sTemp = SUBSTRING(sTemp FROM 3 FOR LENGTH(sTemp));
	RETURN sTemp;
END
;;
DELIMITER ;
