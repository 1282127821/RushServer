package com.star.light.user;

import java.util.ArrayList;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.mina.core.session.IoSession;

import com.star.light.conn.ClientSet;
import com.star.light.mina.LinkedClient;
import com.star.light.protocol.Protocol;
import com.star.light.socket.PBMessage;
import com.star.light.util.GameLog;

public final class UserMgr {
	public static boolean isRecord = false;
	/**
	 * 在线玩家列表
	 */
	private static ConcurrentHashMap<Long, User> onlineUser = new ConcurrentHashMap<Long, User>();
	private static ConcurrentHashMap<Long, User> onlineAccount = new ConcurrentHashMap<Long, User>();
	private static ConcurrentHashMap<Long, IoSession> tempSessionMap = new ConcurrentHashMap<Long, IoSession>();
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
	public static void addOnline(long accountId, long userId, IoSession session) {
		User user = new User(accountId, userId, session);
		onlineUser.put(userId, user);
		onlineAccount.put(accountId, user);
		session.removeAttribute(LinkedClient.TEMP_SESSION_USER_ID);
		session.removeAttribute(LinkedClient.TEMP_SESSION_KEY);
		session.setAttribute(LinkedClient.KEY_ID, userId);

		// 发送消息到客户端,通知成功登陆网关
		user.sendToClient(new PBMessage(Protocol.S_C_LOGIN_GATEWAY, userId));
	}

	/**
	 * 从在线列表中移除一个玩家
	 */
	public static void removeOnline(long userId, IoSession session) {
		User user = getOnlineUser(userId);
		if (user == null || session != user.getSession()) {
			GameLog.warn("客户端 当前用户已经从在线列表中清除了, userId : " + userId);
			return;
		}
		// 通知其他服务器
		onlineUser.remove(userId);
		onlineAccount.remove(user.getAccountId());

		ClientSet.routeServer(new PBMessage(Protocol.C_S_PLAYER_LOGOUT, userId));

		// 关闭Socket
		IoSession uerSession = user.getSession();
		if (uerSession != null) {
			uerSession.closeNow();
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
				onlineUser.getSession().write(message);
			}
		}
	}

	/**
	 * 保存未经过验证的session对象
	 */
	public static void addTempSession(long userId, String key, IoSession session) {
		session.setAttribute(LinkedClient.TEMP_SESSION_KEY, key);
		session.setAttribute(LinkedClient.TEMP_SESSION_USER_ID, userId);
		tempSessionMap.put(userId, session);
	}

	/**
	 * 移除未经过验证的session对象
	 */
	public static IoSession removeTempSession(long userId, String token) {
		IoSession temp = tempSessionMap.get(userId);
		if (temp != null && temp.getAttribute(LinkedClient.TEMP_SESSION_KEY) != null) {
			String beforeToken = (String) temp.getAttribute(LinkedClient.TEMP_SESSION_KEY);
			if (beforeToken.equalsIgnoreCase(token)) {
				return tempSessionMap.remove(userId);
			}
		} else {
			GameLog.error("removeTempSession temp ==null");
		}
		return null;
	}

	/**
	 * 移除未经过验证的session对象
	 */
	public static IoSession removeTempSession(long userId, IoSession session) {
		IoSession temp = tempSessionMap.get(userId);
		return temp == session ? tempSessionMap.remove(userId) : null;
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
			for (IoSession temp : tempSessionMap.values()) {
				temp.closeNow();
			}

			for (Entry<Long, User> entry : onlineUser.entrySet()) {
				User temp = entry.getValue();
				temp.getSession().closeNow();
			}
		} catch (Exception e) {
			GameLog.error("Client set close client session exception.");
		}
	}
}
