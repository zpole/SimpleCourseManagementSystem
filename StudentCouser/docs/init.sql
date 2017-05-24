CREATE database course;
use  course;
DROP TABLE IF EXISTS  `student`;
create table student(
  id int(11) NOT NULL PRIMARY KEY  AUTO_INCREMENT COMMENT 'ID',
  name VARCHAR(100) not  null DEFAULT '',
  number    int not null DEFAULT 0   comment '学号',
  create_time TIMESTAMP  DEFAULT CURRENT_TIMESTAMP ,
  update_time TIMESTAMP DEFAULT   CURRENT_TIMESTAMP  on UPDATE  CURRENT_TIMESTAMP,
  icon   VARCHAR (100) not null DEFAULT '' comment '头像',
  sex    tinyint not  null DEFAULT  0   comment '性别',
  pwd VARCHAR(100) not NULL  DEFAULT   '' comment '密码',
  academy VARCHAR(100) not NULL  DEFAULT '' comment '学院',
  state  tinyint   DEFAULT  0 comment '账号状态',
  UNIQUE KEY `unqiue_index` (`number`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 COMMENT='学生表';

DROP TABLE IF EXISTS `teacher`;
CREATE TABLE `teacher` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `name` varchar(64) NOT NULL COMMENT '姓名',
  `pwd`  varchar(100) not null comment  '密码',
  account int not NULL   UNIQUE comment '老师账号',
  create_time TIMESTAMP  DEFAULT CURRENT_TIMESTAMP ,
  update_time TIMESTAMP DEFAULT   CURRENT_TIMESTAMP  on UPDATE  CURRENT_TIMESTAMP,
  state  tinyint   DEFAULT  0 comment '账号状态',
  PRIMARY KEY (`id`),
  UNIQUE KEY `unqiue_index` (`account`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 COMMENT='老师表';

drop  table  if EXISTS  `admin`;
CREATE  TABLE   `admin`(
    `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'ID',
   `pwd`  varchar(100) not null comment  '密码',
    `account` int not   null   comment '账号',
   state  tinyint   DEFAULT  0 comment '账号状态',
   create_time TIMESTAMP  DEFAULT CURRENT_TIMESTAMP ,
   update_time TIMESTAMP DEFAULT   CURRENT_TIMESTAMP  on UPDATE  CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
    UNIQUE KEY `unqiue_index` (`account`)
)ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='管理员表';

drop  table  if EXISTS  `course`;
create TABLE    course(
 id int not null auto_increment  PRIMARY KEY  comment 'id',
 create_time TIMESTAMP  DEFAULT CURRENT_TIMESTAMP ,
 update_time TIMESTAMP DEFAULT   CURRENT_TIMESTAMP  on UPDATE  CURRENT_TIMESTAMP,
 teacher_id  int not null DEFAULT  0 ,
 name  VARCHAR(100)  not null default '',
UNIQUE KEY `unq_index` (`teacher_id`,`name`)
)ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='课程表';
drop TABLE  if EXISTS  `course_student`;
CREATE table course_student(
 id int not null auto_increment PRIMARY KEY  comment 'id',
 create_time TIMESTAMP  DEFAULT CURRENT_TIMESTAMP ,
 update_time TIMESTAMP DEFAULT   CURRENT_TIMESTAMP  on UPDATE  CURRENT_TIMESTAMP,
 student_id  int  not null default  0,
 courser_id int not null default 0,
 state  tinyint not null DEFAULT  0  comment '打分状态',
 grade  tinyint not null DEFAULT  0  comment '分数',
 UNIQUE KEY `unq_index` (`student_id`,`courser_id`)
)ENGINE=InnoDB  DEFAULT CHARSET=utf8 COMMENT='课程表';
