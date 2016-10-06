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

/*Data for the table `tbl_player` */

insert  into `tbl_player`(`id`,`account`,`name`,`head`,`card`,`room`,`ip`,`points`,`total`,`forbitTime`,`sex`,`haveNewEmail`,`unionid`,`province`,`city`,`country`,`timestamp`,`status`) values (1,'o39_gww71KVmtvqoAt9JCnkH0tt4','那谁家大谁','http://wx.qlogo.cn/mmopen/5n8eVYEAJOl6kGpnEsVzD81uOpyH8XHt3SEcZDIvZicLgfSQUlTveauVz7dtRCbrpySMfDL3oDTnqceJTcGmnKyG29reInE4J/0',8,633997,'192.168.1.32',0,0,'','1',0,'olHXIwE3VKyYEGGLFU-hBXId4Dvo','','','CN',1474534652623,0),(2,'o39_gw9qrCYuqsCPrUNyQq190-Ow','heliang','http://wx.qlogo.cn/mmopen/Q3auHgzwzM5PNrblQ41UanAwmI7PZuUY7gibu4klEsE4Aw3ZJsUqjciawic6hHP50edHPeTCPUhka4koTX0BVekvA/0',8,971526,'192.168.1.51',0,0,'','1',0,'olHXIwFSEF4iLXPByZvypbYg1paw','Beijing','Chaoyang','CN',0,0),(3,'test','hl','',8,546991,'127.0.0.1',0,0,'','1',0,'test','','','',1474537797168,0),(13,'test','heliang','',8,0,'192.168.1.48',0,0,'','1',0,'test','beijing','beijing','china',0,0),(14,'test','7','',8,0,'192.168.1.48',0,0,'','1',0,'test','beijing','beijing','china',NULL,1),(15,'test','13','',8,0,'192.168.1.48',0,0,'','1',0,'test','beijing','beijing','china',NULL,1),(16,'test','15','',8,0,'192.168.1.48',0,0,'','1',0,'test','beijing','beijing','china',NULL,1),(17,'test','2','',8,0,'192.168.1.20',0,0,'','1',0,'test','beijing','beijing','china',NULL,1),(18,'test','17','',8,0,'192.168.1.20',0,0,'','1',0,'test','beijing','beijing','china',NULL,1),(19,'test','1112','',8,0,'192.168.1.20',0,0,'','1',0,'test','beijing','beijing','china',0,0),(20,'test','19','',8,0,'192.168.1.20',0,0,'','1',0,'test','beijing','beijing','china',0,0),(21,'test','1','',8,0,'192.168.1.32',0,0,'','1',0,'test','beijing','beijing','china',NULL,1),(22,'test','88','',8,661177,'192.168.1.31',0,0,'','1',0,'test',NULL,NULL,NULL,0,0),(23,'test','yinsheng','',8,325495,'192.168.1.31',0,0,'','1',0,'test',NULL,NULL,NULL,0,0),(25,'test','86','',8,112985,'192.168.1.31',0,0,'','1',0,'test','','','',0,0),(26,'test','a1','',8,641871,'192.168.1.31',0,0,'','1',0,'test','beijing','beijing','china',0,0),(27,'test','a2','',8,797983,'192.168.1.31',0,0,'','1',0,'test','beijing','beijing','china',0,0),(28,'test','a3','',8,797983,'192.168.1.31',0,0,'','1',0,'test','beijing','beijing','china',0,0),(29,'test','a4','',8,797983,'192.168.1.31',0,0,'','1',0,'test','beijing','beijing','china',0,0),(30,'test','heliang1','',8,-1,'192.168.1.48',0,0,'','1',0,'test','beijing','beijing','china',NULL,1),(31,'test','wzj1','',8,-1,'192.168.1.33',0,0,'','1',0,'test','beijing','beijing','china',NULL,1),(32,'test','b1','',8,736651,'192.168.1.57',0,0,'','1',0,'test','beijing','beijing','china',0,0),(33,'test','heliang234','',8,35292,'192.168.1.48',0,0,'','1',0,'test','beijing','beijing','china',0,0),(34,'test','b3','',8,604912,'192.168.1.57',0,0,'','1',0,'test','beijing','beijing','china',0,0),(35,'test','b4','',8,604912,'192.168.1.57',0,0,'','1',0,'test','beijing','beijing','china',0,0),(36,'test','c1','',8,915921,'192.168.1.57',0,0,'','1',0,'test','beijing','beijing','china',0,0),(37,'test','c2','',8,915921,'192.168.1.57',0,0,'','1',0,'test','beijing','beijing','china',0,0),(38,'test','c3','',8,915921,'192.168.1.26',0,0,'','1',0,'test','beijing','beijing','china',0,0),(39,'test','c4','',8,915921,'192.168.1.26',0,0,'','1',0,'test','beijing','beijing','china',0,0),(40,'test','tt','',8,209876,'192.168.1.30',0,0,'','1',0,'test','beijing','beijing','china',0,0),(41,'test','t1','',8,-1,'192.168.1.20',0,0,'','1',0,'test','beijing','beijing','china',NULL,1),(42,'test','tt1','',8,209876,'192.168.1.30',0,0,'','1',0,'test','beijing','beijing','china',0,0),(43,'test','tt2','',8,-1,'192.168.1.30',0,0,'','1',0,'test','beijing','beijing','china',0,0),(44,'test','tt3','',8,-1,'192.168.1.30',0,0,'','1',0,'test','beijing','beijing','china',0,0),(45,'test','3','',8,-1,'192.168.1.4',0,0,'','1',0,'test','beijing','beijing','china',NULL,1),(46,'test','30','',8,-1,'192.168.1.4',0,0,'','1',0,'test','beijing','beijing','china',NULL,1),(47,'test','55','',8,-1,'192.168.1.39',0,0,'','1',0,'test','beijing','beijing','china',NULL,1),(48,'test','11','',8,-1,'192.168.1.39',0,0,'','1',0,'test','beijing','beijing','china',NULL,1),(49,'test','10','',8,-1,'192.168.1.39',0,0,'','1',0,'test','beijing','beijing','china',NULL,1),(50,'test','dong1','',8,-1,'192.168.1.17',0,0,'','1',0,'test','beijing','beijing','china',NULL,1),(51,'test','iota2','',8,-1,'192.168.1.4',0,0,'','1',0,'test','beijing','beijing','china',NULL,1),(52,'test','4','',8,-1,'192.168.1.39',0,0,'','1',0,'test','beijing','beijing','china',NULL,1);

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

/*Data for the table `tbl_room` */

insert  into `tbl_room`(`roomid`,`ownerAccountID`,`roundTotal`,`battleTime`,`playType`,`createTime`,`roomType`,`isInBattle`,`serverId`) values (11586,'1',8,0,2,1474636842189,0,1,1),(35292,'32',8,0,2,1474688807538,0,1,1),(36713,'36',8,0,2,1474697775751,0,1,1),(58371,'26',8,0,2,1474628428818,0,1,1),(84816,'40',8,0,2,1474695484229,0,1,1),(112985,'1',8,0,2,1474621381574,0,1,1),(133982,'1',8,0,2,1474619577952,0,1,1),(164667,'1',8,0,2,1474620316894,0,1,1),(176010,'1',8,0,2,1474681459099,0,1,1),(184093,'26',8,0,2,1474624225770,0,1,1),(191510,'1',8,0,2,1474635578620,0,1,1),(209876,'42',8,0,2,1474696276941,0,1,1),(223181,'1',8,0,2,1474634320604,0,1,1),(247982,'1',8,0,2,1474634020831,0,1,1),(250027,'1',8,0,2,1474611692816,0,1,1),(268079,'36',8,0,2,1474687817245,0,1,1),(281587,'36',8,0,2,1474694951271,0,1,1),(303454,'26',8,0,2,1474683132442,0,1,1),(325495,'1',8,0,2,1474681557376,0,1,1),(329116,'36',8,0,2,1474701983803,0,1,1),(352984,'36',8,0,2,1474697318861,0,1,1),(353870,'1',8,0,2,1474620575686,0,1,1),(371164,'36',8,0,2,1474700944444,0,1,1),(372484,'36',8,0,2,1474693916965,0,1,1),(485119,'26',8,0,2,1474622050329,0,1,1),(489513,'1',8,0,2,1474633320147,0,1,1),(497430,'1',8,0,2,1474618266266,0,1,1),(529026,'1',8,0,2,1474618860326,0,1,1),(530594,'1',8,0,2,1474622609892,0,1,1),(546991,'1',8,0,2,1474619655233,0,1,1),(572660,'1',8,0,2,1474633708482,0,1,1),(575113,'36',8,0,2,1474693198336,0,1,1),(604912,'32',8,0,2,1474685877740,0,1,1),(605454,'26',8,0,2,1474685351525,0,1,1),(613317,'26',8,0,2,1474632532700,0,1,1),(615074,'1',8,0,2,1474613933402,0,1,1),(632203,'26',8,0,2,1474688841131,0,1,1),(633997,'26',8,0,2,1474689731466,0,1,1),(648734,'1',8,0,2,1474681507233,0,1,1),(661177,'1',8,0,2,1474637610259,0,1,1),(695119,'26',8,0,2,1474630651395,0,1,1),(699680,'26',8,0,2,1474632985619,0,1,1),(704583,'1',8,0,2,1474636987263,0,1,1),(706160,'1',8,0,2,1474637985108,0,1,1),(708620,'1',8,0,2,1474611112399,0,1,1),(736834,'26',8,0,2,1474691878250,0,1,1),(745242,'1',8,0,2,1474634442189,0,1,1),(764839,'26',8,0,2,1474631064739,0,1,1),(797983,'27',8,0,2,1474696352372,0,1,1),(802656,'1',8,0,2,1474620774352,0,1,1),(829442,'36',8,0,2,1474693619636,0,1,1),(851861,'1',8,0,2,1474637498857,0,1,1),(859073,'26',8,0,2,1474702018882,0,1,1),(871550,'1',8,0,2,1474636513973,0,1,1),(875939,'1',8,0,2,1474608617569,0,1,1),(894027,'26',8,0,2,1474626521088,0,1,1),(915921,'36',8,0,2,1474702927359,0,1,1),(929503,'26',8,0,2,1474631288668,0,1,1),(941393,'26',8,0,2,1474628817094,0,1,1),(946130,'36',8,0,2,1474696373350,0,1,1),(971526,'2',8,0,2,1474624316079,0,1,1),(994152,'1',8,0,2,1474619218707,0,1,1);

/*Table structure for table `tbl_server` */

DROP TABLE IF EXISTS `tbl_server`;

CREATE TABLE `tbl_server` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `ip` varchar(50) DEFAULT NULL,
  `port` int(20) DEFAULT NULL,
  `maxuser` int(50) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8;

/*Data for the table `tbl_server` */

insert  into `tbl_server`(`id`,`ip`,`port`,`maxuser`) values (1,'192.168.1.26',9933,5000),(2,'192.168.1.26',9933,5000),(3,'192.168.1.26',9933,5000);

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
