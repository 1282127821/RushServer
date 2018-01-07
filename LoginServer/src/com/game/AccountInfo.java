package com.game;

public class AccountInfo {
	/**
	 * 账号Id
	 */
	private long accountId;

	/**
	 * 玩家账号名
	 */
	private String accountName;

	/**
	 * 创建时间
	 */
	private int createTime;

	/**
	 * 最近登录时间
	 */
	private int loginTime;

	/**
	 * 最近登出时间
	 */
	private int logoutTime;

	/**
	 * 登录IP
	 */
	private String loginIP;

	/**
	 * 禁号原因
	 */
	private String forbidReason;

	/**
	 * 封号操作人
	 */
	private String forbidOperator;

	/**
	 * 禁号过期时间
	 */
	private int forbidExpireTime;

	/**
	 * 手机imei号
	 */
	private String imei;

	/**
	 * 手机型号
	 */
	private String model;
	
	/**
	 * 手机品牌
	 */
	private String brand;

	public long getAccountId() {
		return accountId;
	}

	public void setAccountId(long accountId) {
		this.accountId = accountId;
	}

	public String getAccountName() {
		return accountName;
	}

	public void setAccountName(String accountName) {
		this.accountName = accountName;
	}

	public String getForbidReason() {
		return forbidReason;
	}

	public void setForbidReason(String forbidReason) {
		this.forbidReason = forbidReason;
	}

	public String getForbidOperator() {
		return forbidOperator;
	}
	
	public void setForbidOperator(String forbidOperator) {
		this.forbidOperator = forbidOperator;
	}

	public int getCreateTime() {
		return createTime;
	}

	public void setCreateTime(int createTime) {
		this.createTime = createTime;
	}

	public int getLoginTime() {
		return loginTime;
	}

	public void setLoginTime(int loginTime) {
		this.loginTime = loginTime;
	}

	public int getLogoutTime() {
		return logoutTime;
	}

	public void setLogoutTime(int logoutTime) {
		this.logoutTime = logoutTime;
	}

	public int getForbidExpireTime() {
		return forbidExpireTime;
	}

	public void setForbidExpireTime(int forbidExpireTime) {
		this.forbidExpireTime = forbidExpireTime;
	}

	public String getLoginIP() {
		return loginIP;
	}

	public void setLoginIP(String loginIP) {
		this.loginIP = loginIP;
	}

	public String getImei() {
		return imei;
	}

	public void setImei(String imei) {
		this.imei = imei;
	}

	public String getModel() {
		return model;
	}

	public void setModel(String model) {
		this.model = model;
	}

	public String getBrand() {
		return brand;
	}

	public void setBrand(String brand) {
		this.brand = brand;
	}
}