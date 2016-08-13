package com.player;

public interface PlayerState {
	/**
	 * 离线
	 */
	short OFFLINE = 0;
	
	/**
	 * 在线
	 */
	short ONLINE = 1;

	/**
	 * 禁止
	 */
	short FORBID = 2;

	/**
	 * 加载数据中
	 */
	short LOADING = 3;

	/**
	 * 离开
	 */
	short LEAVE = 4;

	/**
	 * 请勿打扰
	 */
	short NO_DISTURB = 5;

	/**
	 * 忙碌
	 */
	short BUSY = 6;

	/**
	 * 删号
	 */
	short DELETE = 7;
}
