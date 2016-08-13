package com.star.light.player;

import java.util.LinkedList;

import com.star.light.util.GameLog;
import com.star.light.util.TimeUtil;

public class LoginsCache extends AbstractCache {
	private LinkedList<String> waiteMap; // 记录排队
	public static int MAX_WAITE = 5;
	private boolean isPass;
	private static int LOGINWAITE = 0; // 登陆排队中
	private static int LOGINPASS = 1; // 登陆通过
	private static int LOGINMAX = -2; // 登陆超过上限

	/**
	 * @param scanInterval  扫描排队间隔时长
	 * @param clearInterval 过期失效间隔时长
	 * @param isOpen
	 */
	public LoginsCache(int scanInterval, int clearInterval, boolean isOpen) {
		super(scanInterval, clearInterval, isOpen);
		isPass = false;
		waiteMap = new LinkedList<String>();
	}

	@Override
	public void clear() {
	}

	// 扫描登陆：用户同步排队用户
	@Override
	public void scan(boolean isPass) {
		this.isPass = isPass;
		if (isPass) {
			return;
		}
		if (waiteMap.size() <= 0) {
			return;
		}

		try {
			synchronized (waiteMap) {
				for (int i = 0; i < MAX_WAITE; i++) {
					if (waiteMap.isEmpty()) {
						return;
					}
					waiteMap.poll();
				}
			}
		} catch (Exception e) {
			GameLog.error("扫描排队列表出错！" + TimeUtil.getSysteCurTime(), e);
		}
	}

	@Override
	public void add(Object para1, Object para2) {
		if (para1 == null || para2 == null) {
			return;
		}
		String name = (String) para1;

		if (getIsOpen()) {
			synchronized (waiteMap) {
				int tempIndex = waiteMap.indexOf(name);
				if (tempIndex < 0) {
					waiteMap.addLast(name);
				}
			}
		}
	}

	@Override
	public Object check(Object para1, Object para2) {
		if (para1 == null) {
			return null;
		}
		add(para1, para2);
		String name = (String) para1;
		LoginMsg msgInfo = new LoginMsg();
		msgInfo.setFrontData(0);
		msgInfo.setKey(LOGINPASS);
		msgInfo.setTime(0);

		// 检测排队,排在前5人Pass
		if (getIsOpen()) {
			int tempIndex = waiteMap.indexOf(name);
			if (tempIndex > 5 || isPass) {
				msgInfo.setFrontData(tempIndex);// 前面人数
				msgInfo.setKey(LOGINWAITE);
				msgInfo.setTotalData(waiteMap.size());
				msgInfo.setTime(isPass == true ? LOGINMAX : tempIndex / MAX_WAITE * LoginMgr.scanTime);
			}
		}
		return msgInfo;
	}

	/**
	 * 登陆人数上限
	 */
	public LoginMsg checkQueue(String name) {
		LoginMsg msgInfo = new LoginMsg();
		msgInfo.setFrontData(0);
		msgInfo.setKey(LOGINPASS);
		msgInfo.setTime(0);
		// 检测排队,排在前5人Pass
		if (getIsOpen()) {
			int tempIndex = waiteMap.indexOf(name);
			if (tempIndex > 5 || isPass) {
				msgInfo.setFrontData(tempIndex);// 前面人数
				msgInfo.setKey(LOGINWAITE);
				msgInfo.setTotalData(waiteMap.size());
				msgInfo.setTime(isPass == true ? LOGINMAX : tempIndex / MAX_WAITE * LoginMgr.scanTime);
			}
		}
		return msgInfo;
	}

	@Override
	public void vip(Object para1) {

	}

	@Override
	public int getManCount() {
		return 0;
	}

	@Override
	public void reset() {

	}

	@Override
	public boolean remove(Object para1) {
		return false;
	}

	@Override
	public boolean update(Object para1, Object para2) {
		return false;
	}

	public void setPass(boolean isPass) {
		this.isPass = isPass;
	}

	public boolean isPass() {
		return isPass;
	}
}
