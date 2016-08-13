package com.user;

import org.apache.mina.core.session.IoSession;

import com.netmsg.PBMessage;

public class User {
	private IoSession session;
	private final Object obj;
	private long lastSyncTime;
	private int acceleratCount;
	private long accountId;

	public User(long accountId, long userId, IoSession session) {
		this.session = session;
		this.session.setAttribute("userId", userId);
		obj = new Object();
		lastSyncTime = 0;
		acceleratCount = 0;
		this.accountId = accountId;
	}

	public long getAccountId() {
		return accountId;
	}

	public void setAccountId(long accountId) {
		this.accountId = accountId;
	}

	public void setSession(IoSession session) {
		this.session = session;
	}

	public IoSession getSession() {
		return session;
	}

	public long getLastSyncTime() {
		return lastSyncTime;
	}

	public int getAcceleratCount() {
		return acceleratCount;
	}

	public void addAcceleratCount() {
		acceleratCount++;
	}

	public void sendToClient(PBMessage packet) {
		if (session != null && !session.isClosing()) {
			synchronized (obj) {
				session.write(packet);
			}
		}
	}
}