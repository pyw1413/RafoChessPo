/*
SQLyog Ultimate v11.24 (32 bit)
MySQL - 5.6.33 : Database - login
*********************************************************************
*/

/*!40101 SET NAMES utf8 */;

/*!40101 SET SQL_MODE=''*/;

/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;
CREATE DATABASE /*!32312 IF NOT EXISTS*/`login` /*!40100 DEFAULT CHARACTER SET latin1 */;

USE `login`;

/*Table structure for table `tbl_player` */

DROP TABLE IF EXISTS `tbl_player`;

CREATE TABLE `tbl_player` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `account` varchar(50) DEFAULT NULL,
  `name` varchar(50) DEFAULT NULL,
  `head` varchar(500) DEFAULT NULL,
  `card` int(11) DEFAULT NULL,
  `room` int(11) DEFAULT NULL,
  `ip` varchar(50) DEFAULT NULL,
  `points` int(11) DEFAULT NULL,
  `total` int(11) DEFAULT NULL,
  `forbitTime` varchar(50) DEFAULT NULL,
  `sex` varchar(50) DEFAULT NULL,
  `haveNewEmail` int(11) DEFAULT NULL,
  `unionid` varchar(50) DEFAULT NULL,
  `province` varchar(50) DEFAULT NULL,
  `city` varchar(50) DEFAULT NULL,
  `country` varchar(50) DEFAULT NULL,
  `timestamp` bigint(13) DEFAULT NULL,
  `status` int(11) DEFAULT '1',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=53 DEFAULT CHARSET=utf8;

/*Table structure for table `tbl_room` */

DROP TABLE IF EXISTS `tbl_room`;

CREATE TABLE `tbl_room` (
  `roomid` int(11) NOT NULL,
  `ownerAccountID` varchar(100) NOT NULL,
  `roundTotal` int(11) NOT NULL,
  `battleTime` int(11) NOT NULL DEFAULT '0',
  `playType` int(11) DEFAULT NULL,
  `createTime` bigint(20) DEFAULT NULL,
  `roomType` int(11) DEFAULT NULL,
  `isInBattle` tinyint(4) DEFAULT NULL,
  `serverId` int(11) DEFAULT NULL,
  PRIMARY KEY (`roomid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Table structure for table `tbl_server` */

DROP TABLE IF EXISTS `tbl_server`;

CREATE TABLE `tbl_server` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `ip` varchar(50) DEFAULT NULL,
  `port` int(20) DEFAULT NULL,
  `maxuser` int(50) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
