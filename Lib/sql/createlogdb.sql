-- ----------------------------
-- Table structure for `t_log_diamond`
-- ----------------------------
DROP TABLE IF EXISTS `t_log_diamond`;
CREATE TABLE `t_log_diamond` (
  `Id` int(11) NOT NULL AUTO_INCREMENT COMMENT '自增长主键ID',
  `UserId` bigint(20) NOT NULL COMMENT '用户ID',
  `AccountId` bigint(20) NOT NULL COMMENT '帐号Id',
  `RoleVip` tinyint(3) NOT NULL COMMENT '角色vip等级',
  `AccountCreateTime` datetime DEFAULT NULL COMMENT '帐号创建时间',
  `Counts` int(11) NOT NULL COMMENT '消耗/新增数量',
  `CurrentCounts` int(11) NOT NULL COMMENT '当前数量',
  `ChangeType` smallint(6) NOT NULL COMMENT '改变方式',
  `OpTime` int(11) NOT NULL COMMENT '操作时间',
  PRIMARY KEY (`Id`),
  KEY `UserId` (`UserId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='用户钻石消费日志';

-- ----------------------------
-- Table structure for `t_log_gold`
-- ----------------------------
DROP TABLE IF EXISTS `t_log_gold`;
CREATE TABLE `t_log_gold` (
  `Id` int(11) NOT NULL AUTO_INCREMENT COMMENT '自增长主键ID',
  `UserId` bigint(20) NOT NULL COMMENT '用户ID',
  `Counts` int(11) NOT NULL COMMENT '消耗/新增数量',
  `CurrentCounts` int(11) NOT NULL COMMENT '当前数量',
  `ChangeType` smallint(6) NOT NULL COMMENT '改变方式',
  `OpTime` int(11) NOT NULL COMMENT '操作时间',
  PRIMARY KEY (`Id`),
  KEY `UserId` (`UserId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='用户金币消耗日志';

-- ----------------------------
-- Table structure for `t_log_item`
-- ----------------------------
DROP TABLE IF EXISTS `t_log_item`;
CREATE TABLE `t_log_item` (
  `Id` int(11) NOT NULL AUTO_INCREMENT COMMENT '自增长主键ID',
  `UserId` bigint(20) NOT NULL COMMENT '用户ID',
  `ItemId` bigint(20) NOT NULL COMMENT '物品编号',
  `TempId` int(11) NOT NULL COMMENT '模板编号',
  `Counts` int(11) NOT NULL DEFAULT '0' COMMENT '消耗/新增数量',
  `CurrentCounts` int(11) NOT NULL COMMENT '当前数量',
  `Pos` int(11) NOT NULL COMMENT '道具位置',
  `StrongLv` int(11) NOT NULL DEFAULT '0' COMMENT '等级(强化等级)',
  `ChangeType` smallint(6) NOT NULL COMMENT '改变方式',
  `OpTime` int(11) NOT NULL COMMENT '操作时间',
  PRIMARY KEY (`Id`),
  KEY `UserId` (`UserId`),
  KEY `TempId` (`TempId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='物品操作日志';

-- ----------------------------
-- Table structure for `t_log_resource`
-- ----------------------------
DROP TABLE IF EXISTS `t_log_resource`;
CREATE TABLE `t_log_resource` (
  `Id` int(11) NOT NULL AUTO_INCREMENT COMMENT '自增长主键ID',
  `UserId` bigint(20) NOT NULL COMMENT '用户ID',
  `TempId` int(11) NOT NULL COMMENT '模板编号(3.赤晶，4.魔晶，5.战功，6.爱心，7.勋章)',
  `Counts` int(11) NOT NULL DEFAULT '0' COMMENT '消耗/新增数量',
  `CurrentCounts` int(11) NOT NULL COMMENT '当前数量',
  `ChangeType` int(11) NOT NULL COMMENT '改变方式',
  `OpTime` int(11) NOT NULL COMMENT '操作时间',
  PRIMARY KEY (`Id`),
  KEY `UserId` (`UserId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='用户资源日志(3.赤晶，4.魔晶，5.战功，6.爱心，7.勋章)';

-- ----------------------------
-- Table structure for `t_log_server`
-- ----------------------------
DROP TABLE IF EXISTS `t_log_server`;
CREATE TABLE `t_log_server` (
  `Id` int(11) NOT NULL AUTO_INCREMENT COMMENT '编号',
  `CurrentDate` datetime NOT NULL COMMENT '操作时间',
  `Online` int(11) NOT NULL COMMENT '在线人数',
  `Regedit` int(11) NOT NULL COMMENT '注册人数',
  `SystemDate` datetime NOT NULL DEFAULT '2000-01-01 00:00:00' COMMENT '系统当前时间',
  PRIMARY KEY (`Id`),
  KEY `CurrentDate` (`CurrentDate`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='服务器注册在线人数';

-- ----------------------------
-- Table structure for `t_log_campaign`
-- ----------------------------
DROP TABLE IF EXISTS `t_log_campaign`;
CREATE TABLE `t_log_campaign` (
  `Id` int(11) NOT NULL AUTO_INCREMENT COMMENT '自增长主键ID',
  `UserId` bigint(20) NOT NULL COMMENT '玩家id',
  `CampaignId` int(11) NOT NULL COMMENT '副本ID',
  `FightRs` tinyint(4) NOT NULL COMMENT '是否通过',
  `CampaignType` tinyint(4) NOT NULL COMMENT '副本类型:1,普通副本；2，精英副本；3，普通Boss副本；4，世界Boss副本；5，活动副本',
  `OpenTime` int(11) NOT NULL COMMENT '开始时间',
  `StopTime` int(11) NOT NULL COMMENT '结束时间',
  `OpTime` int(11) NOT NULL COMMENT '记录时间',
  PRIMARY KEY (`Id`),
  KEY `UserId` (`UserId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='副本战斗日志';

-- ----------------------------
-- Table structure for `t_log_upgrade`
-- ----------------------------
DROP TABLE IF EXISTS `t_log_upgrade`;
CREATE TABLE `t_log_upgrade` (
  `Id` int(11) NOT NULL AUTO_INCREMENT COMMENT '编号',
  `UserId` bigint(20) NOT NULL COMMENT '用户ID',
  `CurrentDate` datetime NOT NULL COMMENT '操作时间',
  `OldLevel` smallint(6) NOT NULL DEFAULT '0' COMMENT '之前等级',
  `NowLevel` smallint(6) NOT NULL DEFAULT '0' COMMENT '现在等级',
  `OpType` tinyint(4) NOT NULL COMMENT '类型(1，英雄升级)',
  PRIMARY KEY (`Id`),
  KEY `Index_1` (`CurrentDate`,`UserId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='玩家升级日志';

-- ----------------------------
-- Table structure for `t_log_login`
-- ----------------------------
DROP TABLE IF EXISTS `t_log_login`;
CREATE TABLE `t_log_login` (
  `Id` int(11) NOT NULL AUTO_INCREMENT COMMENT '自增长主键ID',
  `DateId` date DEFAULT NULL COMMENT '日期ID',
  `UserId` bigint(20) DEFAULT NULL COMMENT '用户ID',
  `LoginTime` text COMMENT '登陆时间',
  `LoginCount` int(11) DEFAULT '1' COMMENT '登陆次数',
  `UserCreateTime` datetime DEFAULT NULL COMMENT '用户创建时间',
  `Type` varchar(20) DEFAULT NULL COMMENT '渠道(site值)',
  PRIMARY KEY (`Id`),
  KEY `DateId` (`DateId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='用户登陆记录';

-- ----------------------------
-- Table structure for `t_log_global`
-- ----------------------------
DROP TABLE IF EXISTS `t_log_global`;
CREATE TABLE `t_log_global` (
  `DateId` date NOT NULL COMMENT '主键(YYYY-MM-DD)',
  `RegisterCount` int(11) DEFAULT NULL COMMENT '注册数(当日)',
  `ActiveCount` int(11) DEFAULT NULL COMMENT '今日活跃数',
  `LoginCount` int(11) DEFAULT NULL COMMENT '登陆数(当日)',
  `YesterdayRegisterCount` int(11) DEFAULT NULL COMMENT '昨日新等用户',
  `SecondActiveCount` int(11) DEFAULT NULL COMMENT '昨日新登用户今天活跃数',
  `OnLineNowCount` int(11) DEFAULT NULL COMMENT '在线人数(当日当前)',
  `OnLineMaxCount` int(11) DEFAULT NULL COMMENT '在线人数(当日最高)',
  `ChargeDiamond` bigint(20) DEFAULT NULL COMMENT '充值钻石(当日)',
  `HistoryChargeDiamond` bigint(20) DEFAULT NULL COMMENT '历史充值钻石(针对当日以前)',
  `OutputDiamond` bigint(20) DEFAULT NULL COMMENT '产出钻石(当日在游戏中通过特定玩法产出的钻石)',
  `HistoryOutputDiamond` bigint(20) DEFAULT NULL COMMENT '历史产出钻石(针对当日以前)',
  `InternalOutputDiamond` bigint(20) DEFAULT NULL COMMENT '内部发放钻石(当日在游戏中内部发放钻石)',
  `HistoryInternalOutputDiamond` bigint(20) DEFAULT NULL COMMENT '历史内部发放钻石(针对当日以前)',
  `ServerOutputDiamond` bigint(20) DEFAULT NULL COMMENT '服务器历史产出钻石(充值+游戏内产出+内部发放)',
  `ServerConsumeDiamond` bigint(20) DEFAULT NULL COMMENT '服务器历史消耗钻石(消耗即为系统回收)',
  `ServerRemainDiamond` bigint(20) DEFAULT NULL COMMENT '服务器剩余钻石',
  `ChargeUserCount` int(11) DEFAULT NULL COMMENT '服务器充值人数(为服务器充值过的总人数)',
  `ActiverChargeDiamond` bigint(20) DEFAULT NULL COMMENT '今日活跃用户充值钻石数',
  `ActiverChargeCount` int(11) DEFAULT NULL COMMENT '今日活跃用户充值人数',
  `RegisterChargeDiamond` bigint(20) DEFAULT NULL COMMENT '今日新登用户充值钻石数',
  `RegisterChargeCount` int(11) DEFAULT NULL COMMENT '今日新登用户充值人数',
  PRIMARY KEY (`DateId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='服务器总览日志';

-- ----------------------------
-- Table structure for `t_log_level`
-- ----------------------------
DROP TABLE IF EXISTS `t_log_level`;
CREATE TABLE `t_log_level` (
  `Id` int(11) NOT NULL AUTO_INCREMENT COMMENT '自增长主键ID',
  `LevelDate` date DEFAULT NULL COMMENT '统计日期',
  `LevelJson` text COMMENT '等级统计的json字符串',
  PRIMARY KEY (`Id`),
  KEY `LevelDate` (`LevelDate`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='等级人数分布及流失统计';

-- ----------------------------
-- Table structure for `t_log_onlinetime`
-- ----------------------------
DROP TABLE IF EXISTS `t_log_onlinetime`;
CREATE TABLE `t_log_onlinetime` (
  `Id` int(11) NOT NULL AUTO_INCREMENT COMMENT '自增长主键ID',
  `OnlineDate` date DEFAULT NULL COMMENT '日期',
  `UserId` bigint(20) DEFAULT NULL COMMENT '角色ID',
  `RoleVip` tinyint(3) DEFAULT '0' COMMENT '角色Vip等级',
  `OnlineTime` int(11) DEFAULT NULL COMMENT '在线时间',
  PRIMARY KEY (`Id`),
  KEY `OnlineDate` (`OnlineDate`)
) ENGINE=InnoDB AUTO_INCREMENT=547 DEFAULT CHARSET=utf8 COMMENT='玩家当天在线时间统计';

-- ----------------------------
-- Table structure for `t_log_noviceprocess`
-- ----------------------------
DROP TABLE IF EXISTS `t_log_noviceprocess`;
CREATE TABLE `t_log_noviceprocess` (
  `Id` int(11) NOT NULL AUTO_INCREMENT COMMENT '自增长主键ID',
  `DateId` date DEFAULT NULL COMMENT '日期ID',
  `NoviceProcess` int(11) DEFAULT NULL COMMENT '新手引导进度',
  `Num` int(11) DEFAULT NULL COMMENT '卡在当前引导进度的玩家数',
  PRIMARY KEY (`Id`),
  KEY `DateId` (`DateId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='新手引导进度记录表';

-- ----------------------------
-- Table structure for `t_log_rolevipstat`
-- ----------------------------
DROP TABLE IF EXISTS `t_log_rolevipstat`;
CREATE TABLE `t_log_rolevipstat` (
  `Id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `RoleVip` tinyint(3) DEFAULT NULL COMMENT '角色vip等级',
  `TotalPlayers` int(11) DEFAULT '0' COMMENT '角色数',
  `TotalDiamond` bigint(20) DEFAULT '0' COMMENT '玩家总充钻石',
  `ActivePlayers` int(11) DEFAULT NULL COMMENT '存留角色数',
  `ActiveTotalDiamond` bigint(20) DEFAULT NULL COMMENT '存留角色当前钻石总数',
  `DateId` date DEFAULT NULL COMMENT '统计日期',
  PRIMARY KEY (`Id`),
  KEY `DateId` (`DateId`)
) ENGINE=InnoDB AUTO_INCREMENT=25 DEFAULT CHARSET=utf8 COMMENT='角色Vip统计信息表';


-- ----------------------------
-- Table structure for `t_log_accountlogin`
-- ----------------------------
DROP TABLE IF EXISTS `t_log_accountlogin`;
CREATE TABLE `t_log_accountlogin` (
  `Id` int(11) NOT NULL AUTO_INCREMENT COMMENT '自增长主键ID',
  `DateId` date DEFAULT NULL COMMENT '日期ID',
  `AccountId` bigint(20) DEFAULT NULL COMMENT '帐号Id',
  `LoginTime` text COMMENT '登陆时间',
  `LoginCount` int(11) DEFAULT '1' COMMENT '登陆次数',
  `AccountCreateTime` datetime DEFAULT NULL COMMENT '帐号创建时间',
  `Site` varchar(20) DEFAULT NULL COMMENT '渠道(site值)',
  PRIMARY KEY (`Id`),
  KEY `DateId` (`DateId`),
  KEY `Index_AccountCreateTime` (`AccountCreateTime`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='帐号登陆记录';
