/*
Navicat MySQL Data Transfer

Source Server         : localhost
Source Server Version : 50722
Source Host           : localhost:3306
Source Database       : sushe

Target Server Type    : MYSQL
Target Server Version : 50722
File Encoding         : 65001

Date: 2019-03-28 18:32:20
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for building
-- ----------------------------
DROP TABLE IF EXISTS `building`;
CREATE TABLE `building` (
  `DB_ID` int(11) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `NAME` varchar(32) DEFAULT NULL COMMENT '宿舍楼名称',
  `BUILDING_NUM` varchar(32) DEFAULT NULL COMMENT '宿舍编号',
  `CHECK_IN_NUMBER` int(11) DEFAULT NULL COMMENT '入住人数',
  `FREE_NUMBER` int(11) DEFAULT NULL COMMENT '空余人数',
  `REMARK` varchar(255) DEFAULT NULL COMMENT '备注',
  `CREATE_TIME` timestamp NULL DEFAULT NULL COMMENT '创建时间',
  `CREATE_USER` varchar(32) DEFAULT NULL COMMENT '创建人',
  `UPDATE_TIME` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
  `UPDATE_USER` varchar(32) DEFAULT NULL COMMENT '修改人',
  `DEL_FLAG` int(1) DEFAULT '1' COMMENT '删除标志',
  PRIMARY KEY (`DB_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='宿舍楼表';

-- ----------------------------
-- Table structure for check_work
-- ----------------------------
DROP TABLE IF EXISTS `check_work`;
CREATE TABLE `check_work` (
  `DB_ID` int(11) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `CHECK_TYPE` int(1) DEFAULT NULL COMMENT '检查类型 0考勤 1 卫生 2 纪律',
  `SCORE` varchar(32) DEFAULT NULL COMMENT '评分',
  `DORM_ID` int(11) DEFAULT NULL COMMENT '宿舍ID',
  `REMARK` varchar(255) DEFAULT NULL COMMENT '备注',
  `CREATE_TIME` timestamp NULL DEFAULT NULL COMMENT '创建时间',
  `CREATE_USER` varchar(32) DEFAULT NULL COMMENT '创建人',
  PRIMARY KEY (`DB_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='考勤表';

-- ----------------------------
-- Table structure for dict
-- ----------------------------
DROP TABLE IF EXISTS `dict`;
CREATE TABLE `dict` (
  `DB_ID` int(11) unsigned NOT NULL AUTO_INCREMENT COMMENT 'id',
  `ITEM_NAME` varchar(32) DEFAULT NULL COMMENT '类型名称',
  `ITEM_VALUE` varchar(64) DEFAULT NULL COMMENT '类型值',
  `ORDER_BY` int(10) DEFAULT NULL COMMENT '排序',
  `CREATE_TIME` timestamp NULL DEFAULT NULL COMMENT '创建时间',
  `UPDATE_TIME` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
  `DEL_FLAG` int(1) DEFAULT '1' COMMENT '删除标志',
  PRIMARY KEY (`DB_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 CHECKSUM=1 DELAY_KEY_WRITE=1 ROW_FORMAT=DYNAMIC COMMENT='字典表';

-- ----------------------------
-- Table structure for dormitory
-- ----------------------------
DROP TABLE IF EXISTS `dormitory`;
CREATE TABLE `dormitory` (
  `DB_ID` int(11) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `NAME` varchar(32) DEFAULT NULL COMMENT '宿舍名称',
  `DORM_NUM` varchar(32) DEFAULT NULL COMMENT '宿舍编号',
  `CHECK_IN_NUMBER` int(11) DEFAULT NULL COMMENT '入住人数',
  `FREE_NUMBER` int(11) DEFAULT NULL COMMENT '空余人数',
  `BULIDING_ID` int(11) DEFAULT NULL COMMENT '宿舍楼ID',
  `CLASS_GRADE` varchar(32) DEFAULT NULL COMMENT '班级/年级',
  `CREATE_TIME` timestamp NULL DEFAULT NULL COMMENT '创建时间',
  `CREATE_USER` varchar(32) DEFAULT NULL COMMENT '创建人',
  `UPDATE_TIME` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
  `UPDATE_USER` varchar(32) DEFAULT NULL,
  `DEL_FLAG` int(1) DEFAULT '1' COMMENT '删除标志 0：删除 1：正常',
  PRIMARY KEY (`DB_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='宿舍表';

-- ----------------------------
-- Table structure for log
-- ----------------------------
DROP TABLE IF EXISTS `log`;
CREATE TABLE `log` (
  `DB_ID` int(11) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `LOG_TYPE` int(1) DEFAULT NULL COMMENT '日志类型',
  `CONTENT` text COMMENT '日志内容',
  `CREATE_TIME` datetime DEFAULT NULL COMMENT '创建时间',
  `CREATE_USER` varchar(32) DEFAULT NULL COMMENT '创建人',
  PRIMARY KEY (`DB_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='日志表';

-- ----------------------------
-- Table structure for m_menu
-- ----------------------------
DROP TABLE IF EXISTS `m_menu`;
CREATE TABLE `m_menu` (
  `DB_ID` varchar(32) NOT NULL COMMENT '菜单id',
  `MENU_NUM` varchar(32) DEFAULT NULL COMMENT '菜单编号',
  `MENU_HREF` varchar(50) DEFAULT NULL COMMENT '菜单地址',
  `MENU_TYPE` varchar(32) DEFAULT NULL COMMENT '菜单类型',
  `MENU_ICON` varchar(255) DEFAULT NULL COMMENT '菜单图标',
  `MENU_NAME` varchar(32) DEFAULT NULL COMMENT '菜单名称',
  `PARENT_MENU_ID` varchar(32) DEFAULT NULL COMMENT '父菜单id',
  `IS_ENABLED` int(1) DEFAULT NULL COMMENT '启用标志',
  `CREATE_TIME` timestamp NULL DEFAULT NULL,
  `CREATE_USER` varchar(32) DEFAULT NULL,
  `UPDATE_TIME` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `UPDATE_USER` varchar(32) DEFAULT NULL,
  `DEL_FLAG` int(1) DEFAULT '1' COMMENT '删除标志 0：删除 1：正常',
  PRIMARY KEY (`DB_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='菜单表';

-- ----------------------------
-- Table structure for m_role
-- ----------------------------
DROP TABLE IF EXISTS `m_role`;
CREATE TABLE `m_role` (
  `DB_ID` int(11) NOT NULL AUTO_INCREMENT COMMENT '角色id',
  `ROLE_NAME` varchar(32) DEFAULT NULL COMMENT '角色名称',
  `IS_ENABLED` int(1) DEFAULT '1' COMMENT '是否启用1 启用',
  `LEVEL` varchar(11) DEFAULT NULL COMMENT '角色等级',
  `MENU_ID` varchar(256) DEFAULT NULL COMMENT '菜单id，多个菜单用，分开',
  `REMARK` varchar(512) DEFAULT NULL COMMENT '备注',
  `CREATE_TIME` timestamp NULL DEFAULT NULL,
  `CREATE_USER` varchar(64) DEFAULT NULL COMMENT '创建者',
  `UPDATE_TIME` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
  `UPDATE_USER` varchar(64) DEFAULT NULL COMMENT '更新人',
  `DEL_FLAG` int(1) DEFAULT '1' COMMENT '删除标志',
  PRIMARY KEY (`DB_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='角色表';

-- ----------------------------
-- Table structure for m_user
-- ----------------------------
DROP TABLE IF EXISTS `m_user`;
CREATE TABLE `m_user` (
  `DB_ID` int(32) NOT NULL AUTO_INCREMENT,
  `LOGIN_NAME` varchar(32) DEFAULT NULL COMMENT '登录名',
  `NICK_NAME` varchar(32) NOT NULL COMMENT '用户昵称',
  `PASSWORD` varchar(64) NOT NULL COMMENT '登录密码',
  `PHONE` varchar(11) DEFAULT NULL COMMENT '手机号',
  `EMAIL` varchar(32) DEFAULT NULL COMMENT 'email',
  `ROLE_ID` int(11) DEFAULT NULL COMMENT '角色id',
  `ROLE_NAME` varchar(64) DEFAULT NULL COMMENT '角色名称',
  `LAST_LOGIN` timestamp NULL DEFAULT NULL COMMENT '最后登录时间',
  `CREATE_TIME` timestamp NULL DEFAULT NULL COMMENT '创建时间',
  `CREATE_USER` varchar(64) DEFAULT NULL COMMENT '创建者',
  `UPDATE_TIME` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '修改时间',
  `UPDATE_USER` varchar(64) DEFAULT NULL COMMENT '修改人',
  `DEL_FLAG` int(1) DEFAULT '1' COMMENT '删除标志',
  PRIMARY KEY (`DB_ID`),
  UNIQUE KEY `USER_NAME` (`NICK_NAME`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 CHECKSUM=1 DELAY_KEY_WRITE=1 ROW_FORMAT=DYNAMIC COMMENT='系统用户表';

-- ----------------------------
-- Table structure for student
-- ----------------------------
DROP TABLE IF EXISTS `student`;
CREATE TABLE `student` (
  `DB_ID` int(11) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `NAME` varchar(32) DEFAULT NULL COMMENT '学生姓名',
  `STUDENT_NUM` varchar(32) DEFAULT NULL COMMENT '学生编号',
  `GENDER` int(1) DEFAULT NULL COMMENT '性别 0：女 1：男',
  `PHONE` varchar(11) DEFAULT NULL COMMENT '电话',
  `CLASS_GRADE` varchar(32) DEFAULT NULL COMMENT '班级/年级',
  `ADDRESS` varchar(255) DEFAULT NULL COMMENT '家庭住址',
  `DORM_ID` int(11) DEFAULT NULL COMMENT '宿舍id',
  `CREATE_TIME` datetime DEFAULT NULL COMMENT '创建时间',
  `CREATE_USER` varchar(32) DEFAULT NULL COMMENT '创建者',
  `UPDATE_TIME` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `UPDATE_USER` varchar(32) DEFAULT NULL COMMENT '更新人',
  `DEL_FLAG` int(1) DEFAULT '1' COMMENT '删除标志',
  PRIMARY KEY (`DB_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='学生表';
