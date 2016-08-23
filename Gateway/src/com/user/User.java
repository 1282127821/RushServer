package com.user;

import com.netmsg.PBMessage;

import io.netty.channel.Channel;

public class User {
	private Channel channel;
	private final Object obj;
	private long lastSyncTime;
	private int acceleratCount;
	private long accountId;

	public User(long accountId, long userId, Channel channel) {
		this.channel = channel;
//		channel.attr(key);
//		this.session.setAttribute("userId", userId);
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

	public void setChannel(Channel channel) {
		this.channel = channel;
	}

	public Channel getChannel() {
		return channel;
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
		if (channel != null && channel.isActive()) {
			synchronized (obj) {
				channel.writeAndFlush(packet);
			}
		}
	}
}