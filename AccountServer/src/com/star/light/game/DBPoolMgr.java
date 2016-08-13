package com.star.light.game;

import java.sql.Connection;

import com.star.light.DBInfo;
import com.star.light.util.GameLog;

/**
 * 数据库连接池管理类
 */
public final class DBPoolMgr {
	private String MAIN_POOL_NAME = "mysql";
	private static final String dbUrl = "jdbc:mysql://%s:%s/%s?characterEncoding=utf-8&autoReconnect=true";
	private IDBPool mainDBPool;
	private DBInfo mainDBInfo;
	private int mainDBMaxConn = 30;
	private int mainDBFllow = 3;

	private static DBPoolMgr instance = new DBPoolMgr();

	public static DBPoolMgr getInstaqnce() {
		return instance;
	}

	public boolean initMainDB(DBInfo mainDBInfo) {
		this.mainDBInfo = mainDBInfo;
		return initMainDBPool(true);
	}

	private String getMainDBUrl() {
		return mainDBInfo != null ? String.format(dbUrl, mainDBInfo.ip, mainDBInfo.port, mainDBInfo.dbName) : "";
	}


	private boolean initMainDBPool(boolean isInitMainDB) {
		if (isInitMainDB) {
			mainDBPool = createPools(MAIN_POOL_NAME, getMainDBUrl(), mainDBInfo.userName, mainDBInfo.password);
			return mainDBPool != null;
		}
		return true;
	}

	private boolean initPool(boolean isInitMainDB) {
		return initMainDBPool(isInitMainDB);
	}

	/**
	 * 检查连接池状态是否挂掉，如挂了重新初始化
	 */
	public void checkConnectionPool() {
		boolean initStrategy = false;
		if (mainDBPool == null || mainDBPool.getCurConns() <= 0) {
			initStrategy = true;
			GameLog.error("检查strategyPool发现异常,strategyPool:" + getStrategyPoolState());
		}

		if (initStrategy) {
			boolean result = initPool(initStrategy);
			StringBuilder sb = new StringBuilder("重新初始化连接池");
			sb.append(result ? "成功" : "失败").append(",strategyPoolState:").append(getStrategyPoolState());
			GameLog.error(sb.toString());
		}
	}

	/**
	 * 根据指定属性创建连接池实例.
	 */
	private IDBPool createPools(String poolName, String url, String user, String password) {
		int maxconn = 30;
		int fllow = 3;

		if (poolName.equals(MAIN_POOL_NAME) && mainDBMaxConn > 0 && mainDBFllow > 0) {
			maxconn = mainDBMaxConn;
			fllow = mainDBFllow;
		}

		try {
			GameLog.info(String.format("加载配置连接池:%s,URL:%s完成！", poolName, url));
			return new BoneCPDBPool(url, user, password, maxconn, fllow);
		} catch (Exception e) {
			GameLog.error("创建db连接池失败 poolName : " + poolName, e);
			return null;
		}
	}

	public void closePools() {
		closePoolCommon(mainDBPool, "游戏数据库连接池关闭成功");
		GameLog.info("数据库连接池关闭成功");
	}
	
	private void closePoolCommon(IDBPool dbPool, String logDesc) {
		if (dbPool != null) {
			dbPool.shutdown();
			dbPool = null;
			GameLog.info(logDesc);
		}
	}

	public Connection getMainDBConn() {
		return mainDBPool.getConnection();
	}

	public String getStrategyPoolState() {
		return mainDBPool != null ? mainDBPool.getState() : null;
	}
}