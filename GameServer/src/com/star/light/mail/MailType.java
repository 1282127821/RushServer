package com.star.light.mail;

public interface MailType {
	/**
	 * 公告 （ 更新、维护）
	 */
	short ANNOUNCE = 1;

	/**
	 * 系统 （战场、世界BOSS）
	 */
	short SYSTEM = 2;

	/**
	 * 活动 （登录奖励）
	 */
	short ACTIVITY = 3;
}
