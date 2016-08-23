package com.db;

import java.sql.Connection;

import com.DBInfo;
import com.util.GameLog;

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
	private int mainDBMinConn = 3;
	private int mainDBMaxConn = 30;
	private int logDBMinConn = 3;
	private int logDBMaxConn = 30;

	private static DBPoolMgr instance = new DBPoolMgr();

	public static DBPoolMgr getInstaqnce() {
		return instance;
	}

	public boolean initMainDB(DBInfo mainDBInfo) {
		this.mainDBInfo = mainDBInfo;
		return initMainDBPool();
	}

	public boolean initLogDB(DBInfo logDBInfo) {
		this.logDBInfo = logDBInfo;
		return initLogDBPool();
	}

	private String getMainDBUrl() {
		return mainDBInfo != null ? String.format(dbUrl, mainDBInfo.ip, mainDBInfo.port, mainDBInfo.dbName) : "";
	}

	private String getLogDBUrl() {
		return logDBInfo != null ? String.format(dbUrl, logDBInfo.ip, logDBInfo.port, logDBInfo.dbName) : "";
	}

	public boolean initMainDBPool() {
		mainDBPool = createDBPool(MAIN_POOL_NAME, getMainDBUrl(), mainDBInfo.userName, mainDBInfo.password, mainDBMinConn, mainDBMaxConn);
		return mainDBPool != null;
	}

	public boolean initLogDBPool() {
		logDBPool = createDBPool(LOG_POOL_NAME, getLogDBUrl(), logDBInfo.userName, logDBInfo.password, logDBMinConn, logDBMaxConn);
		return logDBPool != null;
	}

	/**
	 * 检查连接池状态是否挂掉，如挂了重新初始化
	 */
	public void checkConnectionPool() {
		boolean result = false;
		if (mainDBPool == null || !mainDBPool.isConnect()) {
			result = initMainDBPool();
			GameLog.error("检查mainDBPool发现异常, mainDBPool:" + getMainDBPoolState());
		}

		if (logDBPool == null || !logDBPool.isConnect()) {
			result = initLogDBPool();
			GameLog.error("检查logPool发现异常,logPool:" + getLogDBPoolState());
		}

		StringBuilder sb = new StringBuilder("重新初始化连接池");
		sb.append(result ? "成功" : "失败").append(",mainDBPool:").append(getMainDBPoolState()).append(",logPoolState：").append(getLogDBPoolState());
		GameLog.error(sb.toString());
	}

	/**
	 * 根据指定属性创建连接池实例.
	 */
	private IDBPool createDBPool(String poolName, String url, String user, String password, int minConn, int maxConn) {
		try {
			GameLog.info(String.format("加载配置连接池:%s,URL:%s完成！", poolName, url));
			return new DBPool(url, user, password, poolName, minConn, maxConn);
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

	public String getMainDBPoolState() {
		return mainDBPool != null ? mainDBPool.getPoolState() : null;
	}

	public String getLogDBPoolState() {
		return logDBPool != null ? logDBPool.getPoolState() : null;
	}
}