package com.star.light.player;

public class PlayerKey {
	private long accountId;
	private String key;
	private int lastDate;
	private int userId;
	
	public PlayerKey(long accountId, String key, int lastDate) {
		this.accountId = accountId;
		this.key = key;
		this.lastDate = lastDate;
	}

	public long getAccountId() {
		return accountId;
	}

	public void setAccountId(long accountId) {
		this.accountId = accountId;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getKey() {
		return key;
	}

	public void setLastDate(int lastDate) {
		this.lastDate = lastDate;
	}

	public int getLastDate() {
		return lastDate;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}
}
