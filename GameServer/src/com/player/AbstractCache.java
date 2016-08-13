package com.player;

import com.util.GameLog;
import com.util.TimeUtil;

public abstract class AbstractCache {
	// 上次扫描时间
	private int lastScanTime;

	// 扫描过期1分钟
	private int scanInterval;

	// 当前扫描位置
	private int scanIndex;

	// 上次清除过期时间
	private int lastClearTime;

	// 清除过期10分钟
	private int clearInterval;

	// 当前人数
	private int currentData;

	// 是否开放updateLogin
	private boolean isOpen;

	private boolean isDebug = false;

	public AbstractCache(int scanInterval, int clearInterval, boolean isOpen) {
		setLastScanTime(TimeUtil.getSysCurSeconds());
		this.scanInterval = scanInterval;
		setLastClearTime(TimeUtil.getSysCurSeconds());
		this.clearInterval = clearInterval;
		setIsOpen(isOpen);
		scanIndex = 0;
	}

	/**
	 * 扫描队列
	 */
	public abstract void scan(boolean isPass);

	/**
	 * 清除过期队列
	 */
	public abstract void clear();

	/**
	 *重置用户状态
	 */
	public abstract void reset();

	/**
	 * 删除
	 * 
	 * @param para1
	 * @return
	 */
	public abstract boolean remove(Object para1);

	/**
	 * 更新队列
	 * 
	 * @param para1
	 * @param para2
	 * @return
	 */
	public abstract boolean update(Object para1, Object para2);

	/**
	 * 检测扫描是否超时
	 */
	public boolean timeOutScan(int dt) {
		return TimeUtil.getSysCurSeconds() - dt > scanInterval;
	}

	/**
	 * 检测清除是否超时
	 */
	public boolean timeOutClear(int dt) {
		return TimeUtil.getSysCurSeconds() - dt > clearInterval;
	}

	/**
	 * 检测
	 */
	public abstract Object check(Object para1, Object para2);

	/**
	 * 添加
	 */
	public abstract void add(Object para1, Object para2);

	/**
	 * 获取当前人数
	 */
	public abstract int getManCount();

	/**
	 * Vip帐号
	 * 
	 * @param para1
	 */
	public abstract void vip(Object para1);

	protected void debugMsg(String name, String pass, String opString) {
		if (isDebug) {
			String msg = String.format("【%s】,用户%s 密码%s：%s了，", TimeUtil.getSysteCurTime(), name, pass, opString);
			GameLog.info(msg);
		}
	}

	public void setLastScanTime(int lastScanTime) {
		this.lastScanTime = lastScanTime;
	}

	public long getLastScanTime() {
		return lastScanTime;
	}

	public void setLastClearTime(int lastClearTime) {
		this.lastClearTime = lastClearTime;
	}

	public long getLastClearTime() {
		return lastClearTime;
	}

	public int getClearInterval() {
		return clearInterval;
	}

	public void setCurrentData(int currentData) {
		this.currentData = currentData;
	}

	public int getCurrentData() {
		return currentData;
	}

	public void setIsOpen(boolean isOpen) {
		this.isOpen = isOpen;
	}

	public boolean getIsOpen() {
		return isOpen;
	}

	public void setScanIndex(int scanIndex) {
		this.scanIndex = scanIndex;
	}

	public int getScanIndex() {
		return scanIndex;
	}

	public int getScanInterval() {
		return scanInterval;
	}

}
