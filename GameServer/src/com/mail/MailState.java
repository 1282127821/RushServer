package com.mail;

/**
 * 邮箱状态
 */
public interface MailState {
	/**
	 * 未领取
	 */
	short NOTGET = 1;

	/**
	 * 已领取
	 */
	short GET = 2;

	/**
	 * 已删除
	 */
	short DELETE = 3;
}
