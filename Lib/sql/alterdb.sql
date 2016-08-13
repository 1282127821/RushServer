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
