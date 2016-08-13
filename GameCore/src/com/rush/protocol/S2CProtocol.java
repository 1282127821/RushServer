package com.rush.protocol;

/**
 * 服务端到客户端的协议编号 Client[0x0000,0x1388]
 */
public interface S2CProtocol {
	/**
	 * 登录网关返回
	 */
	short S_C_LOGIN_GATEWAY = 0x0001;

	/**
	 * 服务器踢掉某个玩家 1账号在其他地方登陆 2gm踢人 3 服务器停服
	 */
	short S_C_KICK_PLAYER = 0x0002;

	/**
	 * 同步玩家信息
	 */
	short S_C_PLAYER_INFO = 0x0003;

	/**
	 * 同步服务器时间
	 */
	short S_C_SYNC_SERVER_TIME = 0x0004;

	/**
	 * 同步主城内的玩家信息
	 */
	short S_C_SYNC_MAIN_CITY_PLAYER = 0x0005;

	/**
	 * 某个玩家进入主城
	 */
	short S_C_PLAYER_ENTER_MAIN_CITY = 0x0006;

	/**
	 * 玩家在主城内移动
	 */
	short S_C_MAIN_CITY_MOVE = 0x0007;

	/**
	 * 某个玩家离开主城
	 */
	short S_C_PLAYER_LEAVE_MAIN_CITY = 0x0008;

	/**
	 * 返回给客户端PVP的结果
	 */
	short S_C_PVP_RESULT = 0x0009;

	/**
	 * 返回给客户端取消玩家无敌状态
	 */
	public static short S_C_PVP_CANCEL_INVINCE = 0x000A;

	/**
	 * 返回给客户端可以使用回复药道具
	 */
	short S_C_USE_RECOVERY_DRUG = 0x000B;

	/**
	 * 返回给客户端同步玩家的位置
	 */
	short S_C_SYNC_POSITION = 0x000C;

	/**
	 * 返回给客户端同步玩家的使用技能
	 */
	short S_C_SYNC_USE_SKILL_LINK = 0x000D;

	/**
	 * 返回给客户端同步玩家的动作
	 */
	short S_C_SYNC_ACTION = 0x000E;

	/**
	 * 返回给客户端同步玩家的伤害
	 */
	short S_C_SYNC_HURT = 0x000F;

	/**
	 * 返回给客户端同步飞行道具
	 */
	short S_C_SYNC_FLY_ITEM = 0x0010;

	/**
	 * 返回给客户端创建飞行道具
	 */
	short S_C_SYNC_CREATEPROJECTILE = 0x0011;

	/**
	 * 提示语
	 */
	short S_C_TIP_INFO = 0x0012;

	/**
	 * 背包物品初始化
	 */
	short S_C_ITEM_INIT = 0x0013;

	/**
	 * 背包物品改变
	 */
	short S_C_ITEM_CHANGE = 0x0014;

	/**
	 * 发送技能信息给玩家
	 */
	short S_C_SKILL_INFO = 0x0015;

	/**
	 * 玩家升级技能
	 */
	short S_C_UPGRADE_SKILL = 0x0016;

	/**
	 * 发送玩家属性信息给客户端，同步属性都使用这个协议
	 */
	short S_C_PLAYER_ATTRIBUTE = 0x0017;

	/**
	 * 玩家出战技能列表
	 */
	short S_C_FIGHT_SKILL_CHAIN = 0x0018;

	/**
	 * 同步PVP的一些状态
	 */
	short S_C_SYNC_PVP_STATE = 0x0019;

	/**
	 * 服务器返回随机骰子获得物品
	 */
	short S_C_PVP_RANDOM_ITEM = 0x001A;

	/**
	 * 服务器返回玩家接受了任务
	 */
	short S_C_ACCEPT_TASK = 0x001B;

	/**
	 * 房间的BOSS的房主更改
	 */
	short S_C_PVP_MASTER_CHANGE = 0x001C;

	/**
	 * 同步队伍接受任务的消息
	 */
	short S_C_SYNMESSAGE_TEAM = 0x001D;
	
	/**
	 * 服务器返回查看好友所有信息
	 */
	short S_C_VIEW_FRIEND_INFO = 0x001E;
	
	/**
	 * 服务器返回添加一个好友
	 */
	short S_C_ADD_FRIEND = 0x001F;
	
	/**
	 * 服务器返回删除好友， 仇敌， 战友，根据操作类型
	 */
	short S_C_DEL_FRIEND = 0x0020;
	
	/**
	 * 服务器返回查看附近玩家
	 */
	short S_C_VIEW_NEAR_USER = 0x0021;
	
	/**
	 * 服务器返回查询好友
	 */
	short S_C_SEARCH_FRIEND = 0x0022;
	
	/**
	 * 返回玩家详细信息
	 */
	short S_C_PLAYER_DETAIL_INFO = 0x002A;
	
	/**
	 * 同步飞行道具的位置
	 */
	short S_C_SYNC_FLY_POSITION = 0x002B;

	/**
	 * 房间创建信息
	 */
	short S_C_ROOM_CREATE = 0x002D;

	/**
	 * pvp 好友信息
	 */
	short S_C_PVP_FRIEND_INFO = 0x002E;

	/**
	 * 房间状态修改同步
	 */
	short S_C_PVP_STATE_INFO = 0x002F;

	/**
	 * 返回给玩家所有的邮件信息列表
	 */
	short S_C_TOTAL_MAIL_LIST = 0x0030;

	/**
	 * 返回给客户端玩家领取某封邮件的附件的结果
	 */
	short S_C_GET_ONE_MAIL_ATTACH = 0x0031;
	
	/**
	 * 返回给客户端玩家领取所有邮件的附件的结果
	 */
	short S_C_GET_ALL_MAIL_ATTACH = 0x0032;
	
	/**
	 * 服务器返回同步PVP过程中玩家的护盾值
	 */
	short S_C_SYNC_PVP_SHIELDHP = 0x0033;
	
	/**
	 * 通知客户端是否开启AI
	 */
	short S_C_STAR_PVP_AI = 0x0034;

	/**
	 * 受邀玩家成功加入公会
	 */
	short S_C_SUCCESS_JOIN_GUILD = 0x0041;

	/**
	 * 玩家成功通过申请加入公会
	 */
	short S_C_SUCCESS_GUILD_APPLY = 0x0042;

	/**
	 * 成功任命某人的公会职位
	 */
	short S_C_APPOINT_GUILD_POWER = 0x0043;

	/**
	 * 公会的玩家收到被踢的消息
	 */
	short S_C_ANSWER_KICK_OUT_MEMBER = 0x0044;

	/**
	 * 成功离开某个公会
	 */
	short S_C_LEAVE_GUILD = 0x0045;

	/**
	 * 成功解散某个公会
	 */
	short S_C_ANSWER_DISMISS_GUILD = 0x0046;

	/**
	 * 邀请玩家返回
	 */
	short S_C_SUCCESS_INVITE_JOIN_GUILD = 0x0047;

	/**
	 * 成功修改公会公告
	 */
	short S_C_CHANGE_GUILD_SLOGAN = 0x0048;

	/**
	 * 返回给玩家的公会科技信息列表
	 */
	short S_C_ANSWER_GUILD_TECH_LIST = 0x004F;

	/**
	 * 返回给玩家公会科技的信息
	 */
	short S_C_SYNC_GUILD_TECH = 0x0050;

	/**
	 * 返回玩家公会的动态事件列表
	 */
	short S_C_ANSWER_GUILD_EVENT_LIST = 0x0051;
	
	/**
	 * 返回查看某个公会的信息
	 */
	short S_C_SEARCH_GUILD = 0x0052;
	
	/**
	 * 返回查看某个公会的所有成员详细信息
	 */
	short S_C_VIEW_GUILD_INFO = 0x0053;
	
	/**
	 * 服务器返回所有的公会申请列表
	 */
	short S_C_VIEW_TOTAL_GUILD_APPLY = 0x0054;
	
	/**
	 * 服务器返回查询的公会列表信息
	 */
	short S_C_REQUEST_TOTAL_GUILD_INFO = 0x0055;

	/**
	 * 成功拒绝所有公会的申请返回给客户端
	 */
	short S_C_REJECT_TOTAL_GUILD_APPLY = 0x0056;

	/**
	 * 返回给客户端个人公会的相关信息
	 */
	short S_C_RETURN_GUILD_INFO = 0x0057;
	
	/**
	 * 返回给客户端自己公会的信息
	 */
	short S_C_SELF_GUILD_INFO = 0x0058;
	
	/**
	 * 返回创建公会成功
	 */
	short S_C_CREATE_GUILD = 0x0059;
	
	/**
	 * 玩家发送过来申请加入公会
	 */
	short S_C_APPLY_JOIN_GUILD = 0x005A;
	
	/**
	 * 返回玩家取消申请加入公会
	 */
	short S_C_CANCLE_GUILD_APPLY = 0x005B;
	
	/**
	 * 创建公会失败
	 */
	short S_C_FAILE_CREATE_GUILD = 0x005C;
	
	/**
	 * 拒绝某个玩家的申请加入公会
	 */
	short S_C_REJECT_GUILD_APPLY = 0x005D;
	
	/**
	 * 同意某个玩家的申请加入公会
	 */
	short S_C_ACCEPT_GUILD_APPLY = 0x005E;
	
	/**
	 * 同意所有公会的申请要求
	 */
	short S_C_ACCEPT_TOTAL_GUILD_APPLY = 0x005F;
	
	/**
	 * 收到被踢出公会
	 */
	short S_C_BE_KICK_OUT = 0x0060;
	
	/**
	 * 收到被成为公会会长
	 */
	short S_C_BE_GUILD_LEADER = 0x0061;
	
	/**
	 * 聊天
	 */
	short S_C_CHAT = 0x0065;
	
	/**
	 * 返回成功添加一个玩家到黑名单
	 */
	short S_C_ADD_USER_BLACK = 0x0066;
	
	/**
	 * 从黑名单中移除掉一个玩家
	 */
	short S_C_DELETE_BLACK_USER = 0x0067;
	
	/**
	 * 服务器发送所有的黑名单列表给客户端
	 */
	short S_C_TOTAL_BLACK_USER = 0x0068;
	
	/**
	 * 装备进阶
	 */
	short S_C_EQUIP_ADVANCE = 0x0069;
	
	/**
	 * 装备祝福
	 */
	short S_C_EQUIP_BLESS = 0x006A;
	
	/**
	 * 装备镶嵌
	 */
	short S_C_EQUIP_INLAY = 0x006B;
	
	/**
	 * 服务器返回请求队伍有关的信息
	 */
	short S_C_REQUEST_TEAM_INFO = 0x015C;
	
	/**
	 * 队伍创建成功
	 */
	short S_C_TEAM_CREATE_SUCCESS = 0x015D;

	/**
	 * 队伍加入成功
	 */
	short S_C_TEAM_JOIN_SUCCESS = 0x015E;

	/**
	 * 
	 * 解散队伍成功
	 */
	short S_C_TEAM_DISBAN_SUCCESS = 0x015F;

	/**
	 * 加入队伍
	 */
	short S_C_TEAM_JOIN = 0x0160;

	/**
	 * 离开队伍
	 */
	short S_C_TEAM_LEAVE = 0x0161;

	/**
	 * 解散队伍
	 */
	short S_C_TEAM_DISBAN = 0x0162;
	/**
	 * 队伍列表信息
	 */
	short S_C_TEAM_LIST = 0x0163;
	/**
	 * 队伍初始化信息
	 */
	short S_C_TEAM_INIT = 0x0164;

	/**
	 * 队长改变
	 */
	short S_C_TEAM_CHANGELEADER = 0x0165;

	/**
	 * 同步被踢出去
	 */
	short S_C_TEAM_KICKOUT = 0x0166;

	/**
	 * 玩家进入副本
	 */
	short S_C_ENTER_INSTANCE = 0x0167;

	/**
	 * 玩家离开副本
	 */
	short S_C_LEAVE_INSTANCE = 0x0168;

	/**
	 * 返回房间列表信息给客户端
	 */
	short S_C_ROOM_INFO_LIST = 0x0169;

	/**
	 * 返回给主动打劫副本的玩家
	 */
	short S_C_ROBBERY_INSTANCE = 0x016A;

	/**
	 * 返回给被打劫的玩家
	 */
	short S_C_ROBBERYED_INSTANCE = 0x016B;
}
