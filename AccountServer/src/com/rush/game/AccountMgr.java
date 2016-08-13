package com.rush.game;

import java.util.concurrent.ConcurrentHashMap;

import com.rush.util.GameLog;

import io.netty.channel.Channel;

public final class AccountMgr {
	/**
	 * 在线账号列表
	 */
	private static ConcurrentHashMap<String, Account> onlineAccount = new ConcurrentHashMap<String, Account>();
	
	/**
	 * 在线的channel列表
	 */
	private static ConcurrentHashMap<Long, Channel> channelMap = new ConcurrentHashMap<Long, Channel>();

	private static AccountMgr instance = new AccountMgr();

	public static AccountMgr getInstance() {
		return instance;
	}
	
	/**
	 * 获取一个在线账号
	 */
	public Account getOnlineAccount(String accountName) {
		return onlineAccount.get(accountName);
	}
	
	public void addChannel(long channelId, Channel channel) {
		channelMap.put(channelId, channel);
	}

	/**
	 * 添加一个账号
	 */
	public void addAccount(String accountName) {
		Account account = new Account();
		account.setAccountName(accountName);
		onlineAccount.put(accountName, account);
//		session.setAttribute(LinkedClient.KEY_ID, userId);
	}

	/**
	 * 删除一个账号
	 */
	public void removeAccount(String accountName) {
		Account account = getOnlineAccount(accountName);
//		if (user == null || session != user.getSession()) {
//			GameLog.warn("客户端 当前用户已经从在线列表中清除了, userId : " + userId);
//			return;
//		}
//		// 通知其他服务器
//		onlineUser.remove(userId);
//		onlineAccount.remove(user.getAccountId());
//
//		ClientSet.routeCastle(userId, new PBMessage(Protocol.C_S_PLAYER_LOGOUT, userId));
//
//		// 关闭Socket
//		IoSession uerSession = user.getSession();
//		if (uerSession != null) {
//			uerSession.closeNow();
//		}
	}

	/**
	 * 移除未经过验证的session对象
	 */
//	public static IoSession removeTempSession(long userId, String token) {
//		IoSession temp = tempSessionMap.get(userId);
//		if (temp != null && temp.getAttribute(LinkedClient.TEMP_SESSION_KEY) != null) {
//			String beforeToken = (String) temp.getAttribute(LinkedClient.TEMP_SESSION_KEY);
//			if (beforeToken.equalsIgnoreCase(token)) {
//				return tempSessionMap.remove(userId);
//			}
//		} else {
//			GameLog.error("removeTempSession temp ==null");
//		}
//		return null;
//	}

	/**
	 * 移除未经过验证的session对象
	 */
//	public static IoSession removeTempSession(long userId, IoSession session) {
//		IoSession temp = tempSessionMap.get(userId);
//		return temp == session ? tempSessionMap.remove(userId) : null;
//	}
	
	/**
	 * 关闭gateway,将客户端连接全部断开
	 */
	public static void stop() {
		try {
//			for (Channel channel : channelMap.values()) {
//				channel.close();
//			}
		} catch (Exception e) {
			GameLog.error("Client set close client session exception.");
		}
	}
}
