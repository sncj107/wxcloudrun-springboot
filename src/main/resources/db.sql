CREATE TABLE `Counters` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `count` int(11) NOT NULL DEFAULT '1',
  `createdAt` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updatedAt` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;

-- 创建用户表，存储来自微信的用户信息
CREATE TABLE `Users` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `openId` varchar(64) NOT NULL COMMENT '微信用户唯一标识',
  `unionId` varchar(64) DEFAULT NULL COMMENT '微信开放平台唯一标识',
  `sessionKey` varchar(64) DEFAULT NULL COMMENT '会话密钥',
  `nickname` varchar(100) DEFAULT NULL COMMENT '用户昵称',
  `avatarUrl` varchar(500) DEFAULT NULL COMMENT '用户头像 URL',
  `gender` tinyint(1) DEFAULT 0 COMMENT '性别 0-未知 1-男 2-女',
  `language` varchar(50) DEFAULT NULL COMMENT '语言',
  `city` varchar(100) DEFAULT NULL COMMENT '城市',
  `province` varchar(100) DEFAULT NULL COMMENT '省份',
  `country` varchar(100) DEFAULT NULL COMMENT '国家',
  `createdAt` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updatedAt` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_openId` (`openId`),
  KEY `idx_unionId` (`unionId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='微信用户信息表';
