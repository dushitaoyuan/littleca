
CREATE DATABASE /*!32312 IF NOT EXISTS*/`api_user` /*!40100 DEFAULT CHARACTER SET utf8mb4 */;

USE `api_user`;
/*Table structure for table `api_account` */

DROP TABLE IF EXISTS `api_account`;

CREATE TABLE `api_account` (
  `id` bigint(20) NOT NULL COMMENT '主键',
  `api_account` varchar(128) DEFAULT NULL COMMENT '账户',
  `api_secret` varchar(128) DEFAULT NULL COMMENT '账号密钥',
  `api_pub` text COMMENT '公钥',
  `status` tinyint(4) DEFAULT NULL COMMENT '状态0禁用,1可用',
  `white_ip` varchar(256) DEFAULT NULL COMMENT '白名单',
  `active_time` timestamp NULL DEFAULT NULL COMMENT '账户激活时间',
  `end_time` timestamp NULL DEFAULT NULL COMMENT '账户截止时间',
  `desc` varchar(256) DEFAULT NULL COMMENT '账户备注',
  `create_time` timestamp NULL DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `account_uqindex` (`api_account`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

/*Data for the table `api_account` */

insert  into `api_account`(`id`,`api_account`,`api_secret`,`api_pub`,`status`,`white_ip`,`active_time`,`end_time`,`desc`,`create_time`) values 

(1,'dushitaoyuan-rsa',NULL,'-----BEGIN PUBLIC KEY-----\nMIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCrmo3EsVFy+YssvCfJGqLn7UY+\ndVc0y6KuIYG6Pwx6nk2SCDNyuXBhYuP1c+J1Qfv7o9F6AFv5GeYdt0ErdhZcIUHZ\nN2wqRr1J0Dpsob800RdPU33YFogJP7eRRbW0o4KkKjudnVk+s2VEXzmk4b6viKsG\n4wZ2GQLEt1uWHWLU1wIDAQAB\n-----END PUBLIC KEY-----\n',1,NULL,NULL,NULL,'rsa','2020-05-06 16:44:49'),

(2,'dushitaoyuan-sm2',NULL,'-----BEGIN PUBLIC KEY-----\nMFkwEwYHKoZIzj0CAQYIKoEcz1UBgi0DQgAENomKWOcyV2g5bSeD2zOWP3AgSzHb\nL4iNq9cblU2HkD2nwWXFqpOYOe1CbxKEvcAJHnHN+LBv7lExRB+R6YGNpg==\n-----END PUBLIC KEY-----\n',1,NULL,NULL,NULL,'sm2','2020-05-06 16:44:49'),

(3,'dushitaoyuan-hmac','dushitaoyuan',NULL,1,NULL,NULL,NULL,'hmac','2020-05-06 16:44:49');

