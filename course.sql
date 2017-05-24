/*
SQLyog Ultimate v11.27 (32 bit)
MySQL - 5.7.16-log : Database - course
*********************************************************************
*/

/*!40101 SET NAMES utf8 */;

/*!40101 SET SQL_MODE=''*/;

/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;
CREATE DATABASE /*!32312 IF NOT EXISTS*/`course` /*!40100 DEFAULT CHARACTER SET utf8 */;

USE `course`;

/*Table structure for table `admin` */

DROP TABLE IF EXISTS `admin`;

CREATE TABLE `admin` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `pwd` varchar(100) NOT NULL COMMENT '密码',
  `account` int(11) NOT NULL COMMENT '账号',
  `state` tinyint(4) DEFAULT '0' COMMENT '账号状态',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `unqiue_index` (`account`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8 COMMENT='管理员表';

/*Data for the table `admin` */

insert  into `admin`(`id`,`pwd`,`account`,`state`,`create_time`,`update_time`) values (1,'99e1973ed186b22804a03e2c566096c3',123123,0,'2016-11-26 21:51:16','2016-11-27 08:09:11');

/*Table structure for table `course` */

DROP TABLE IF EXISTS `course`;

CREATE TABLE `course` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `teacher_id` int(11) NOT NULL DEFAULT '0',
  `name` varchar(100) NOT NULL DEFAULT '',
  PRIMARY KEY (`id`),
  UNIQUE KEY `unq_index` (`teacher_id`,`name`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8 COMMENT='课程表';

/*Data for the table `course` */

insert  into `course`(`id`,`create_time`,`update_time`,`teacher_id`,`name`) values (1,'2016-11-27 08:12:45','2016-11-27 08:12:45',4,'123123');

/*Table structure for table `course_student` */

DROP TABLE IF EXISTS `course_student`;

CREATE TABLE `course_student` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `teacher_id` int(11) NOT NULL DEFAULT '0',
  `student_id` int(11) NOT NULL DEFAULT '0',
  `courser_id` int(11) NOT NULL DEFAULT '0',
  `state` tinyint(4) NOT NULL DEFAULT '0' COMMENT '打分状态',
  `grade` tinyint(4) NOT NULL DEFAULT '0' COMMENT '分数',
  PRIMARY KEY (`id`),
  UNIQUE KEY `unq_index` (`student_id`,`courser_id`)
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8 COMMENT='课程表';

/*Data for the table `course_student` */

insert  into `course_student`(`id`,`create_time`,`update_time`,`teacher_id`,`student_id`,`courser_id`,`state`,`grade`) values (9,'2016-11-27 11:28:05','2016-11-27 11:28:54',0,2,1,0,70);

/*Table structure for table `student` */

DROP TABLE IF EXISTS `student`;

CREATE TABLE `student` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `name` varchar(100) NOT NULL DEFAULT '',
  `number` int(11) NOT NULL DEFAULT '0' COMMENT '学号',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `icon` varchar(100) NOT NULL DEFAULT '' COMMENT '头像',
  `sex` tinyint(4) NOT NULL DEFAULT '0' COMMENT '性别',
  `pwd` varchar(100) NOT NULL DEFAULT '' COMMENT '密码',
  `academy` varchar(100) NOT NULL DEFAULT '' COMMENT '学院',
  `state` tinyint(4) DEFAULT '0' COMMENT '账号状态',
  PRIMARY KEY (`id`),
  UNIQUE KEY `unqiue_index` (`number`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8 COMMENT='学生表';

/*Data for the table `student` */

insert  into `student`(`id`,`name`,`number`,`create_time`,`update_time`,`icon`,`sex`,`pwd`,`academy`,`state`) values (1,'123123',123123,'2016-11-26 21:55:13','2016-11-26 21:55:13','',1,'string','',1),(2,'123',123,'2016-11-26 22:05:34','2016-11-27 10:45:27','',2,'99e1973ed186b22804a03e2c566096c3','',0),(5,'qasd',14056712,'2016-11-27 11:00:25','2016-11-27 11:00:25','',1,'99e1973ed186b22804a03e2c566096c3','',1);

/*Table structure for table `teacher` */

DROP TABLE IF EXISTS `teacher`;

CREATE TABLE `teacher` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `name` varchar(64) NOT NULL COMMENT '姓名',
  `pwd` varchar(100) NOT NULL COMMENT '密码',
  `account` int(11) NOT NULL COMMENT '老师账号',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `state` tinyint(4) DEFAULT '0' COMMENT '账号状态',
  PRIMARY KEY (`id`),
  UNIQUE KEY `account` (`account`),
  UNIQUE KEY `unqiue_index` (`account`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8 COMMENT='老师表';

/*Data for the table `teacher` */

insert  into `teacher`(`id`,`name`,`pwd`,`account`,`create_time`,`update_time`,`state`) values (4,'666','99e1973ed186b22804a03e2c566096c3',666,'2016-11-26 23:19:25','2016-11-27 08:09:01',0),(5,'123123','99e1973ed186b22804a03e2c566096c3',123123,'2016-11-27 11:06:23','2016-11-27 11:06:23',0);

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
