package com.star.light.protocol;

/**
 * 网关服务器协议 Gateway[0x4E21,0x61A8]
 */
public interface GatewayProtocol {
	/**
	 * 玩家登陆网关
	 */
	short G_LOGIN_GATEWAY = 0x4E21;

	/**
	 * Castle通知登陆Gateway
	 */
	short G_PLAYER_NOTICE = 0x4E22;

	/**
	 * 踢玩家下线
	 */
	short G_KICK_AWAY_PLAYER = 0x4E25;

	/**
	 * 准备登陆
	 */
	short G_PLAYER_WAITE = 0x4E26;

	/**
	 * 客户端与服务器端时间同步
	 */
	short G_SYNC_TIME = 0x4E27;

	/**
	 * 广播系统消息
	 */
	short G_BROADCAST_SYSTEM_MSG = 0x4E28;

	/**
	 * 聊天
	 */
	short G_CHAT = 0x4E29;

	/**
	 * 跨服连接
	 */
	short G_CROSS_CONN = 0x4E2A;

	/**
	 * 跨服连接断开
	 */
	short G_CROSS_DISCONN = 0x4E2B;

	/**
	 * 请求user注册cross服务器
	 */
	short G_CROSS_LOGIN = 0x4E2C;
	
	/**
	 * 请求user注销cross服务器
	 */
	short G_CROSS_LOGOUT = 0x4E2D;
	
	/**
	 * 请求重置cross服务器
	 */
	short G_CROSS_RESET = 0x4E2E;
	
	/**
	 * 玩家掉线后重新登录
	 */
	short G_CROSS_ON = 0x4E2F;

	/**
	 * 通知
	 */
	short G_NOTINCE = 0x4E30;

	/**
	 * 删除玩家
	 */
	short G_DELETE_USER = 0x4E31;
}
