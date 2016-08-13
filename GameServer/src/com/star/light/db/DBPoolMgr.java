package com.star.light.db;

import java.sql.Connection;

import com.star.light.DBInfo;
import com.star.light.util.GameLog;

/**
 * 数据库连接池管理类
 */
public final class DBPoolMgr {
	private String MAIN_POOL_NAME = "mysql";
	private String LOG_POOL_NAME = "log";
	private static final String dbUrl = "jdbc:mysql://%s:%s/%s?characterEncoding=utf-8&autoReconnect=true";
	private IDBPool mainDBPool;
	private IDBPool logDBPool;
	private DBInfo mainDBInfo;
	private DBInfo logDBInfo;
	private int mainDBMaxConn = 30;
	private int mainDBFllow = 3;
	private int logDBMaxConn = 30;
	private int logDBFllow = 3;

	private static DBPoolMgr instance = new DBPoolMgr();

	public static DBPoolMgr getInstaqnce() {
		return instance;
	}

	public boolean initMainDB(DBInfo mainDBInfo) {
		this.mainDBInfo = mainDBInfo;
		return initMainDBPool(true);
	}

	public boolean initLogDB(DBInfo logDBInfo) {
		this.logDBInfo = logDBInfo;
		return initLogDBPool(true);
	}

	private String getMainDBUrl() {
		return mainDBInfo != null ? String.format(dbUrl, mainDBInfo.ip, mainDBInfo.port, mainDBInfo.dbName) : "";
	}

	private String getLogDBUrl() {
		return logDBInfo != null ? String.format(dbUrl, logDBInfo.ip, logDBInfo.port, logDBInfo.dbName) : "";
	}

	private boolean initMainDBPool(boolean isInitMainDB) {
		if (isInitMainDB) {
			mainDBPool = createDBPool(MAIN_POOL_NAME, getMainDBUrl(), mainDBInfo.userName, mainDBInfo.password);
			return mainDBPool != null;
		}
		return true;
	}

	private boolean initLogDBPool(boolean isInitLogDB) {
		if (isInitLogDB) {
			logDBPool = createDBPool(LOG_POOL_NAME, getLogDBUrl(), logDBInfo.userName, logDBInfo.password);
			return logDBPool != null;
		}
		return true;
	}

	private boolean initPool(boolean isInitMainDB, boolean isInitLogDB) {
		return initMainDBPool(isInitMainDB) && initLogDBPool(isInitLogDB);
	}

	/**
	 * 检查连接池状态是否挂掉，如挂了重新初始化
	 */
	public void checkConnectionPool() {
		boolean initStrategy = false;
		boolean initLog = false;
		if (mainDBPool == null || mainDBPool.getCurConns() <= 0) {
			initStrategy = true;
			GameLog.error("检查strategyPool发现异常,strategyPool:" + getStrategyPoolState());
		}

		if (logDBPool == null || logDBPool.getCurConns() <= 0) {
			initLog = true;
			GameLog.error("检查logPool发现异常,logPool:" + getLogPoolState());
		}

		if (initStrategy || initLog) {
			boolean result = initPool(initStrategy, initLog);
			StringBuilder sb = new StringBuilder("重新初始化连接池");
			sb.append(result ? "成功" : "失败").append(",strategyPoolState:").append(getStrategyPoolState()).append(",logPoolState：").append(getLogPoolState());
			GameLog.error(sb.toString());
		}
	}

	/**
	 * 根据指定属性创建连接池实例.
	 */
	private IDBPool createDBPool(String poolName, String url, String user, String password) {
		int maxconn = 30;
		int fllow = 3;

		if (poolName.equals(MAIN_POOL_NAME) && mainDBMaxConn > 0 && mainDBFllow > 0) {
			maxconn = mainDBMaxConn;
			fllow = mainDBFllow;
		} else if (poolName.equals(LOG_POOL_NAME) && logDBMaxConn > 0 && logDBFllow > 0) {
			maxconn = logDBMaxConn;
			fllow = logDBFllow;
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
		closePoolCommon(logDBPool, "日志数据库连接池关闭成功");
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

	public Connection getLogDBConn() {
		return logDBPool.getConnection();
	}

	public String getStrategyPoolState() {
		return mainDBPool != null ? mainDBPool.getState() : null;
	}

	public String getLogPoolState() {
		return logDBPool != null ? logDBPool.getState() : null;
	}
}