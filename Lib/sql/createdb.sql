-- ----------------------------
-- Table structure for `tbl_account`
-- ----------------------------
DROP TABLE IF EXISTS `tbl_account`;
CREATE TABLE `tbl_account` (
  `AccountId` bigint(20) NOT NULL COMMENT '账号编号',
  `AccountName` varchar(100) NOT NULL COMMENT '账号名',
  `CreateTime` int(11) DEFAULT '0' COMMENT '账号创建时间',
  `LoginTime` int(11) DEFAULT '0' COMMENT '最近登陆时间',
  `LogoutTime` int(11) DEFAULT '0' COMMENT '最后离开时间',
  `LoginCount` int(11) DEFAULT '0' COMMENT '总登陆次数',
  `LoginIp` varchar(30) DEFAULT NULL COMMENT '登陆IP地址',
  `ForbidReason` varchar(50) DEFAULT NULL COMMENT '禁号原因',
  `ForbidOperator` varchar(50) DEFAULT NULL COMMENT '封号操作人',
  `ForbidExpirtTime` int(11) DEFAULT '0' COMMENT '禁号过期时间',
  `Imei` varchar(32) DEFAULT NULL COMMENT '手机imei',
  `Model` varchar(32) DEFAULT NULL COMMENT '手机型号',
  `Brand` varchar(20) DEFAULT NULL COMMENT '手机品牌',
  `GameId` int(11) DEFAULT '0' COMMENT '游戏Id',
  `DelCDTime` int(11) DEFAULT '0' COMMENT '删号冷却时间',
  PRIMARY KEY (`AccountId`),
  UNIQUE KEY `AName` (`AccountName`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='账号信息';

-- ----------------------------
-- Table structure for `tbl_playerinfo`
-- ----------------------------
DROP TABLE IF EXISTS `tbl_playerinfo`;
CREATE TABLE `tbl_playerinfo` (
  `UserId` bigint(20) NOT NULL COMMENT '用户编号',
  `AccountId` bigint(20) NOT NULL COMMENT '账号编号',
  `UserName` varchar(50) NOT NULL COMMENT '角色名字',
  `Diamond` int(11) DEFAULT '0' COMMENT '钻石',
  `Gold` int(11) DEFAULT '0' COMMENT '黄金',
  `JobId` int(11) NOT NULL COMMENT '玩家职业Id',
  `PlayerState` smallint(6) DEFAULT '0' COMMENT ' 状态',
  `NoviceProcess` int(11) DEFAULT '0' COMMENT '新手引导进度',
  `PlayerLv` int(11) DEFAULT '0' COMMENT '角色等级',
  `VipLv` tinyint(3) DEFAULT '0' COMMENT 'VIP等级',
  `FightStrength` int(11) DEFAULT '0' COMMENT '角色战斗力',
  `PlayerExp` int(11) DEFAULT '0' COMMENT '角色经验',
  `CurrHP` int(11) DEFAULT '0' COMMENT '玩家的当前血量',
  `CurrMP` int(11) DEFAULT '0' COMMENT '玩家的当前法术值',
  `PosX` float DEFAULT '0' COMMENT '玩家所在的X坐标位置',
  `PosY` float DEFAULT '0' COMMENT '玩家所在的Y坐标位置',
  `PosZ` float DEFAULT '0' COMMENT '玩家所在的Z坐标位置',
  `Direct` float DEFAULT '0' COMMENT '玩家的方向',
  `CreateTime` int(11) DEFAULT '0' COMMENT '角色创建时间',
  `LoginTime` int(11) DEFAULT '0' COMMENT '角色登录游戏时间',
  `LogoutTime` int(11) DEFAULT '0' COMMENT '角色登出游戏时间',
  `ResetTime` int(11) DEFAULT '0' COMMENT '角色重置时间',
  `GuildId` bigint(20) DEFAULT '0' COMMENT '公会Id',
  `BlackUser` varchar(3000) DEFAULT '' COMMENT '玩家的黑名单列表',
  `IsDelete` tinyint(1) DEFAULT '0' COMMENT '是否删除',
  PRIMARY  KEY (`UserId`),
  KEY `PAccountId` (`AccountId`),
  UNIQUE KEY `PUserName` (`UserName`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='角色信息';

-- ----------------------------
-- Table structure for `tbl_iteminfo`
-- ----------------------------
DROP TABLE IF EXISTS `tbl_iteminfo`;
CREATE TABLE `tbl_iteminfo` (
  `Id` bigint(20) NOT NULL COMMENT '物品编号',
  `UserId` bigint(20) NOT NULL COMMENT '所属用户Id',
  `TemplateId` int(11) DEFAULT '0' COMMENT '道具的模板Id',
  `PosIndex` smallint(6) DEFAULT '0' COMMENT '物品所在格子位置',
  `StackCount` int(11) DEFAULT '0' COMMENT '当前数量',
  `BagType` tinyint(3) DEFAULT '0' COMMENT '道具存储空间类型，0为装备栏，1为物品背包',
  `GainTime` int(11) DEFAULT '0' COMMENT '道具获得时间',
  `BlessLv` tinyint(3) DEFAULT '0' COMMENT '祝福等级',
  `BlessAttribute` int(11) DEFAULT '0' COMMENT '祝福的属性',
  `InlayCard` varchar(50) DEFAULT '0' COMMENT '镶嵌卡片列表',
  `AttributeInfo` varchar(80) DEFAULT '' COMMENT '副属性信息',
  PRIMARY KEY (`Id`),
  KEY `IUserId` (`UserId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='玩家物品表';

-- ----------------------------
-- Table structure for `tbl_fightskill`
-- ----------------------------
DROP TABLE IF EXISTS `tbl_fightskill`;
CREATE TABLE `tbl_fightskill` (
  `SkillDBId` bigint(20) NOT NULL COMMENT '数据库主键',
  `UserId` bigint(20) NOT NULL COMMENT '玩家Id',
  `JobType` tinyint(3) DEFAULT '0' COMMENT '职业类型',
  `SkillId` int(11) NOT NULL COMMENT '技能Id',
  `SkillLv` int(11) DEFAULT '0' COMMENT '技能等级',
  `IsActiveSkill` tinyint(3) DEFAULT '1' COMMENT '技能类型  1为主动技能 0为被动技能',
  PRIMARY KEY (`SkillDBId`),
  KEY `index_1` (`UserId`, `JobType`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='角色技能表';

-- ----------------------------
-- Table structure for table `tbl_skillchain`
-- ----------------------------
DROP TABLE IF EXISTS `tbl_skillchain`;
CREATE TABLE `tbl_skillchain` (
  `UserId` bigint(20) NOT NULL COMMENT '角色Id',
  `JobType` int(11) NOT NULL COMMENT '职业类型',
  `FightSkillChain` varchar(96) DEFAULT NULL COMMENT '角色出战技能设置列表',
  PRIMARY KEY (`UserId`, `JobType`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='角色出战技能信息';

-- ----------------------------
-- Table structure for `tbl_offlineattribute`
-- ----------------------------
DROP TABLE IF EXISTS `tbl_offlineattribute`;
CREATE TABLE `tbl_offlineattribute` (
  `UserId` bigint(20) NOT NULL COMMENT '用户编号',
  `Live` int(11) DEFAULT '0' COMMENT '生命',
  `Armor` int(11) DEFAULT '0' COMMENT '护甲',
  `Attack` int(11) DEFAULT '0' COMMENT '攻击',
  `Sunder` int(11) DEFAULT '0' COMMENT '破甲',
  `Defence` int(11) DEFAULT '0' COMMENT '防御',
  `Crit` int(11) DEFAULT '0' COMMENT '暴击',
  `Tenacity` int(11) DEFAULT '0' COMMENT '坚韧',
  `Hit` int(11) DEFAULT '0' COMMENT '命中',
  `Dodge` int(11) DEFAULT '0' COMMENT '闪避',
  PRIMARY KEY (`UserId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='用户离线属性';

-- ----------------------------
-- Table structure for table `tbl_friend`
-- ----------------------------
DROP TABLE IF EXISTS `tbl_friend`;
CREATE TABLE `tbl_friend` (
  `UserId` bigint(20) NOT NULL COMMENT '角色Id',
  `FriendGroup` varchar(3000) DEFAULT NULL COMMENT '玩家的好友列表',
  `FriendCount` smallint(6) DEFAULT '30' COMMENT '玩家的好友上限个数',
  `EnemyGroup` varchar(1000) DEFAULT NULL COMMENT '玩家的仇敌列表',
  `EnemyCount` smallint(6) DEFAULT '30' COMMENT '玩家的仇敌上限个数',
  `BattleFriendGroup` varchar(1000) DEFAULT NULL COMMENT '玩家的战友列表',
  `BattleFriendCount` smallint(6) DEFAULT '30' COMMENT '玩家的战友上限个数',
  PRIMARY KEY (`UserId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='角色好友信息';

-- ----------------------------
-- Table structure for `tbl_guild`
-- ----------------------------
DROP TABLE IF EXISTS `tbl_guild`;
CREATE TABLE `tbl_guild` (
  `GuildId` bigint(20) NOT NULL COMMENT '公会Id',
  `GuildName` varchar(20) NOT NULL COMMENT '公会名字',
  `GuildSlogan` varchar(100) DEFAULT NULL  COMMENT '公会公告',
  `GuildEmblem` tinyint(3) DEFAULT '0' COMMENT '公会徽',
  `GuildLv` tinyint(3) DEFAULT '0' COMMENT '公会的等级',
  `TotalExp` int(11) DEFAULT '0' COMMENT '公会总经验值',
  `IsAudit` tinyint(3) DEFAULT '1' COMMENT '是否需要审核加入公会',
  `CreateTime` int(11) DEFAULT '0' COMMENT '公会创建时间',
  `IsExist` tinyint(1) DEFAULT '1' COMMENT '是否已删除',
  PRIMARY KEY (`GuildId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='公会列表';

-- ----------------------------
-- Table structure for `tbl_guildmember`
-- ----------------------------
DROP TABLE IF EXISTS `tbl_guildmember`;
CREATE TABLE `tbl_guildmember` (
  `GuildId` bigint(20) NOT NULL COMMENT '公会Id',
  `UserId` bigint(20) NOT NULL COMMENT '玩家Id',
  `GuildPower` tinyint(3) DEFAULT '0' COMMENT '公会职位',
  `Contribution` int(11) DEFAULT '0' COMMENT '贡献度',
  PRIMARY KEY (`UserId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='公会成员表';

-- ----------------------------
-- Table structure for `tbl_guildapply`
-- ----------------------------
DROP TABLE IF EXISTS `tbl_guildapply`;
CREATE TABLE `tbl_guildapply` (
  `GuildId` bigint(20) NOT NULL COMMENT '公会Id',
  `UserId` bigint(20) NOT NULL COMMENT '玩家Id',
  `ApplyTime` int(11) NOT NULL COMMENT '申请时间',
  PRIMARY KEY (`GuildId`, `UserId`),
  KEY `GTUserId` (`UserId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='公会申请表';

-- ----------------------------
-- Table structure for `tbl_guildtech`
-- ----------------------------
DROP TABLE IF EXISTS `tbl_guildtech`;
CREATE TABLE `tbl_guildtech` (
  `UserId` bigint(20) NOT NULL COMMENT '玩家Id',
  `TechId` int(11) NOT NULL COMMENT '公会科技Id',
  `TechLv` tinyint(3) DEFAULT '0' COMMENT '公会科技等级',
  `Contribution` int(11) DEFAULT '0' COMMENT '公会科技贡献度',
  PRIMARY KEY (`UserId`, `TechId`),
  KEY `GTUserId` (`UserId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='玩家公会科技表';

-- ----------------------------
-- Table structure for `tbl_guildevent`
-- ----------------------------
DROP TABLE IF EXISTS `tbl_guildevent`;
CREATE TABLE `tbl_guildevent` (
  `Id` int(11) NOT NULL AUTO_INCREMENT COMMENT '编号',
  `GuildId` bigint(20) NOT NULL COMMENT '公会Id',
  `EventDesc` varchar(100) DEFAULT '' COMMENT '公会事件描述',
  `EventTime` int(11) DEFAULT '0' COMMENT '公会事件发生时间',
  PRIMARY KEY (`Id`),
  KEY `GTGuildId` (`GuildId`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COMMENT='公会事件动态表';

-- ----------------------------
-- Table structure for `tbl_mail`
-- ----------------------------
DROP TABLE IF EXISTS `tbl_mail`;
CREATE TABLE `tbl_mail` (
  `MailId` bigint(20) NOT NULL COMMENT '邮件Id',
  `UserId` bigint(20) NOT NULL COMMENT '玩家Id',
  `MailTitle` varchar(40) DEFAULT NULL COMMENT '邮件标题',
  `MailContent` varchar(400) DEFAULT NULL COMMENT '邮件内容',
  `MailAttach` varchar(400) DEFAULT NULL COMMENT '邮件附件内容',
  `MailState` tinyint(3) DEFAULT '0' COMMENT '邮件状态',
  `MailType` tinyint(3) DEFAULT '0' COMMENT '邮件类型',
  `SendTime` int(11) DEFAULT '0' COMMENT '发送邮件时间',
  PRIMARY KEY (`MailId`),
  KEY `MTUserId` (`UserId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='邮件信息';