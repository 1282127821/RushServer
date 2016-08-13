package com.protocol;

/**
 * 客户端到服务端的协议编号 Castle[0x1389,0x2710]
 */
public interface C2SProtocol {
	/**
	 * 注册连接到castle服务器
	 */
	short C_REGISTER = 0x2328;
	
	/**
	 *  账号登录
	 */
	short C_S_ACCOUNT_LOGIN = 0x0001;
	
	/**
	 * 创建角色
	 */
	short C_S_CREATE_PLAYER = 0x0002;
	
	/**
	 * 删除角色
	 */
	short C_S_DELETE_PLAYER = 0x0003;

	/**
	 * 验证用户key值
	 */
	short C_S_PLAYER_KEY = 0x1389;

	/**
	 * 玩家登陆游戏
	 */
	short C_S_PLAYER_LOGIN = 0x138A;

	/**
	 * 玩家退出游戏
	 */
	short C_S_PLAYER_LOGOUT = 0x138B;
	
	/**
	 * 玩家进入场景
	 */
    short C_S_ENTER_SCENE = 0x138C;

    /**
	 * 玩家在场景内移动
	 */
    short C_S_SCENE_MOVE = 0x138D;

    /**
	 * 玩家离开场景
	 */
    short C_S_LEAVE_SCNE = 0x138E;
    
    /**
     * 同步飞行道具位置
     */
    short C_S_SYNC_FLY_POSITION = 0x138F;

    /**
     * PVP过程中玩家死亡
     */
    short C_S_PVP_PLAYER_DIE = 0x1390;

    /**
     * 客户端发送过来GM命令
     */
    short C_S_GM_ORDER = 0x1391;

    /**
     * 设置出战技能
     */
    short C_S_SET_FIGHT_SKILL_CHAIN = 0x1392;

    /**
     * 使用回复药
     */
    short C_S_USE_RECOVERY_DRUG = 0x1393;

    /**
     * 同步玩家的位置
     */
    short C_S_SYNC_POSITION = 0x1394;

    /**
     * 同步玩家的动作
     */
    short C_S_SYNC_ACTION = 0x1395;

    /**
     * 同步玩家使用技能
     */
    short C_S_SYNC_USE_SKILL_LINK = 0x1396;

    /**
     * 同步玩家的伤害
     */
    short C_S_SYNC_HURT = 0x1397;
    
    /**
     * 同步飞行道具
     */
    short C_S_FLY_ITEM = 0x1398;
    
	/**
	 * 同步创建飞行道具
	 */
	short C_S_SYNC_CREATEPROJECTILE = 0x1399;
	
	/**
	 * 道具相关的操作
	 */
	short C_S_ITEM_OPERATOR = 0x139A;

	/**
	 * 玩家升级技能
	 */
	short C_S_UPGRADE_SKILL = 0x139B;
	
	/**
	 * 同步PVP的一些状态
	 */
	short C_S_SYNC_PVP_STATE = 0x139C;
	
	/**
	 * 玩家发送PVP已经准备好了
	 */
	short C_S_PVP_READY = 0x139D;
	
	/**
	 * 同步PVP过程中玩家的护盾值
	 */
	short C_S_SYNC_PVP_SHIELDHP = 0x139E;
	
	/**
	 * 客户端发送过来随机骰子获得物品
	 */
	short C_S_PVP_RANDOM_ITEM = 0x139F;
	
	/**
	 * 客户端发送过来查看好友信息
	 */
	short C_S_VIEW_FRIEND_INFO = 0x13A0;
	
	/**
	 * 客户端发送过来添加一个好友
	 */
	short C_S_ADD_FRIEND = 0x13A1;
	
	/**
	 * 客户端发送过来删除好友， 仇敌， 战友，根据操作类型
	 */
	short C_S_DEL_FRIEND = 0x13A2;
	
	/**
	 * 客户端发送过来查看附近玩家
	 */
	short C_S_VIEW_NEAR_USER = 0x13A3;
	
	/**
	 * 客户端发送过来查询好友
	 */
	short C_S_SEARCH_FRIEND = 0x13A4;

	/**
	 * 玩家领取某封邮件的附件
	 */
	short C_S_GET_ONE_MAIL_ATTACH = 0x13A5;

	/**
	 * 玩家领取所有邮件的附件
	 */
	short C_S_GET_ALL_MAIL_ATTACH = 0x13A6;
	
	/**
	 * 客户端发送过来请求所有的邮件信息
	 */
	short C_S_REQUEST_TOTAL_MAIL_INFO = 0x13A7;
	
	/**
	 * 客户端发送过来查看所有公会的列表信息
	 */
	short C_S_REQUEST_TOTAL_GUILD_INFO = 0x13A9;
	
	/**
	 * 创建公会
	 */
	short C_S_CREATE_GUILD = 0x13AA;

	/**
	 * 搜索公会
	 */
	short C_S_SEARCH_GUILD = 0x13AB;

	/**
	 * 玩家公会捐献
	 */
	short C_S_GUILD_DONATE = 0x13AC;
	
	/**
	 * 请求查看某个公会的信息
	 */
	short C_S_VIEW_GUILD_INFO = 0x13AD;
	
	/**
	 * 客户端发送过来改变公会成员的权限
	 */
	short C_S_APPOINT_GUILD_POWER = 0x13AE;
	
	/**
	 * 同意某个玩家加入公会
	 */
	short C_S_ACCEPT_GUILD_APPLY = 0x13AF;
	
	/**
	 * 拒绝某个玩家加入公会
	 */
	short C_S_REJECT_GUILD_APPLY = 0x13B0;
	
	/**
	 * 改变公会的宣言
	 */
	short C_S_CHANGE_GUILD_SLOGAN = 0x13B1;
	
	/**
	 * 把某个玩家踢出公会
	 */
	short C_S_KICK_OUT_GUILD_MEMBER = 0x13B2;
	
	/**
	 * 玩家退出公会
	 */
	short C_S_LEAVE_GUILD = 0x13B3;
	
	/**
	 * 玩家查看公会申请列表
	 */
	short C_S_VIEW_TOTAL_GUILD_APPLY = 0x13B4;
	
	/**
	 * 玩家拒绝所有公会申请列表
	 */
	short C_S_REJECT_TOTAL_GUILD_APPLY = 0x13B5;
	
	/**
	 * 玩家取消申请加入公会
	 */
	short C_S_CANCLE_GUILD_APPLY = 0x13B6;
	
	/**
	 * 玩家发送过来申请加入公会
	 */
	short C_S_APPLY_JOIN_GUILD = 0x13B7;
	
	/**
	 * 玩家同意所有公会申请列表
	 */
	short C_S_ACCEPT_TOTAL_GUILD_APPLY = 0x13B8;
	
	/**
	 *  PVP播放场景动画
	 */
	short C_S_PVP_START_SCENE_ANIMATION = 0x13BE;
	
	/**
	 * PVP中进入战斗区域
	 */
	short C_S_ENTER_FIGHT_AREA = 0x13BF;

	/**
	 * 聊天
	 */
	short C_S_CHAT = 0x13C0;
	
	/**
	 * 添加一个玩家到黑名单
	 */
	short C_S_ADD_USER_BLACK = 0x13C1;
	
	/**
	 * 从黑名单中移除掉一个玩家
	 */
	short C_S_DELETE_BLACK_USER = 0x13C2;
	
	/**
	 * 装备进阶
	 */
	short C_S_EQUIP_ADVANCE = 0x13C3;
	
	/**
	 * 装备祝福
	 */
	short C_S_EQUIP_BLESS = 0x13C4;
	
	/**
	 * 装备镶嵌
	 */
	short C_S_EQUIP_INLAY = 0x13C5;
	
	/**
	 * 客户端请求过来要求组队
	 */
	short C_S_REQUEST_COMBINE_TEAM = 0x1493;
	
	/**
	 * 客户端发送过来请求队伍有关的信息
	 */
	short C_S_REQUEST_TEAM_INFO = 0x1494;
	
	/**
	 * 创建队伍
	 */
	short C_CREATE_TEAM = 0x1495;
	
	/**
	 * 离开队伍
	 */
	short C_LEAVE_TEAM = 0x1496;
	
	/**
	 * 同步队伍列表
	 */
	short C_SYNC_TEAMLIST = 0x1497;
	
	/**
	 * 加入队伍
	 */
	short C_JOIN_TEAM = 0x1498;
	
	/**
	 * 解散队伍
	 */
	short C_DISBAN_TEAM = 0x1499;
	
	/**
	 * 踢出队伍
	 */
	short C_KICKOUT_TEAM = 0x149A;
	/**
	 * 改变队长
	 */
	short C_CHANGELEADER_TEAM = 0x149B;

	/**
	 * 创建副本
	 */
	short C_CREATE_INSTANCE = 0x149C;
	
	/**
	 * 离开副本
	 */
	short C_LEAVE_INSTANCE = 0x149D;
	
	/**
	 * 请求房间列表信息
	 */
	short C_S_ROOM_INFO_LIST = 0x149E;
	
	/**
	 * 打劫副本
	 */
	short C_S_ROBBERY_INSTANCE = 0x149F;
	
	/**
	 * 队伍同步消息
	 */
	short C_S_SYNMESSAGE_TEAM = 0x14A0;
}
