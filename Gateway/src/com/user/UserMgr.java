package com.user;

import java.util.ArrayList;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

import com.netmsg.PBMessage;
import com.util.GameLog;

import io.netty.channel.Channel;

public final class UserMgr {
	public static boolean isRecord = false;
	/**
	 * 在线玩家列表
	 */
	private static ConcurrentHashMap<Long, User> onlineUser = new ConcurrentHashMap<Long, User>();
	private static ConcurrentHashMap<Long, User> onlineAccount = new ConcurrentHashMap<Long, User>();
	private static ConcurrentHashMap<Long, Channel> tempChannelMap = new ConcurrentHashMap<Long, Channel>();
	private static ConcurrentHashMap<Long, Boolean> listenUserId = new ConcurrentHashMap<Long, Boolean>();
	private static boolean isRecordDetail = false;
	private static boolean plugswitch = true; // 外挂掉线开关

	/**
	 * 获取一个在线用户
	 */
	public static User getOnlineUser(long userId) {
		return onlineUser.get(userId);
	}

	/**
	 * 获取一个在线账号
	 */
	public static User getOnlineAccount(long accoutnId) {
		return onlineAccount.get(accoutnId);
	}

	/**
	 * 添加一个玩家到在线列表中
	 */
	public static void addOnline(long accountId, long userId, Channel channel) {
		User user = new User(accountId, userId, channel);
		onlineUser.put(userId, user);
		onlineAccount.put(accountId, user);
//		session.removeAttribute(LinkedClient.TEMP_SESSION_USER_ID);
//		session.removeAttribute(LinkedClient.TEMP_SESSION_KEY);
//		session.setAttribute(LinkedClient.KEY_ID, userId);

		// 发送消息到客户端,通知成功登陆网关
//		user.sendToClient(new PBMessage(Protocol.S_C_LOGIN_GATEWAY, userId));
	}

	/**
	 * 从在线列表中移除一个玩家
	 */
	public static void removeOnline(long userId, Channel channel) {
		User user = getOnlineUser(userId);
		if (user == null || channel != user.getChannel()) {
			GameLog.warn("客户端 当前用户已经从在线列表中清除了, userId : " + userId);
			return;
		}
		
		// 通知其他服务器
		onlineUser.remove(userId);
		onlineAccount.remove(user.getAccountId());

//		ClientSet.routeServer(new PBMessage(Protocol.C_S_PLAYER_LOGOUT, userId));

		// 关闭Socket
		Channel userChannel = user.getChannel();
		if (userChannel != null) {
			userChannel.close();
		}
	}

	/**
	 * 广播消息
	 */
	public static void broadcastMessage(PBMessage message) {
		ArrayList<User> totalOnlineUser = new ArrayList<User>();
		totalOnlineUser.addAll(onlineUser.values());
		for (User onlineUser : totalOnlineUser) {
			if (onlineUser != null) {
				onlineUser.getChannel().writeAndFlush(message);
			}
		}
	}

	/**
	 * 保存未经过验证的session对象
	 */
	public static void addTempChannel(long userId, String key, Channel channel) {
//		session.setAttribute(LinkedClient.TEMP_SESSION_KEY, key);
//		session.setAttribute(LinkedClient.TEMP_SESSION_USER_ID, userId);
		tempChannelMap.put(userId, channel);
	}

	/**
	 * 移除未经过验证的session对象
	 */
	public static Channel removeTempChannel(long userId, String token) {
		Channel channel = tempChannelMap.get(userId);
		if (channel != null) {
			return tempChannelMap.remove(userId);
		}
		
		// TODO:LZGLZG 这里需要修改
//		if (channel != null && temp.getAttribute(LinkedClient.TEMP_SESSION_KEY) != null) {
//			String beforeToken = (String) temp.getAttribute(LinkedClient.TEMP_SESSION_KEY);
//			if (beforeToken.equalsIgnoreCase(token)) {
//				return tempChannelMap.remove(userId);
//			}
//		} else {
//			GameLog.error("removeTempSession temp ==null");
//		}
		return null;
	}

	/**
	 * 移除未经过验证的session对象
	 */
	public static Channel removeTempSession(long userId, Channel session) {
		Channel temp = tempChannelMap.get(userId);
		return temp == session ? tempChannelMap.remove(userId) : null;
	}

	/**
	 * 判断玩家id是否在协议记录列表中
	 */
	public static boolean isRecord(long userId) {
		return listenUserId.containsKey(userId);
	}

	public static boolean isRecordDetail() {
		return isRecordDetail;
	}

	/**
	 * 开始监听指定玩家的数据包
	 */
	public static void startListen(boolean isDetail, long[] userIds) {
		listenUserId.clear();
		for (long userId : userIds) {
			listenUserId.put(userId, true);
		}
		isRecord = true;
		isRecordDetail = isDetail;
	}

	public static void stopListen() {
		listenUserId.clear();
		isRecord = false;
		isRecordDetail = false;
	}

	/**
	 * 外挂掉线开关是否开启
	 */
	public static boolean isPlugswitch() {
		return plugswitch;
	}

	/**
	 * 外挂掉线开关设置
	 */
	public static void setPlugswitch(boolean plugswitch) {
		UserMgr.plugswitch = plugswitch;
	}

	/**
	 * 关闭gateway,将客户端连接全部断开
	 */
	public static void stop() {
		try {
			for (Channel channel : tempChannelMap.values()) {
				channel.close();
			}

			for (Entry<Long, User> entry : onlineUser.entrySet()) {
				User user = entry.getValue();
				user.getChannel().close();
			}
		} catch (Exception e) {
			GameLog.error("Client set close client session exception.");
		}
	}
}
