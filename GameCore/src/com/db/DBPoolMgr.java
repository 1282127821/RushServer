package com.db;

import java.sql.Connection;
import java.sql.SQLException;

import com.DBInfo;
import com.util.Log;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

/**
 * 数据库连接池管理类
 */
public final class DBPoolMgr
{
	private static final String URL = "jdbc:mysql://%s:%s/%s?useUnicode=true&characterEncoding=UTF-8&autoReconnect=true&failOverReadOnly=false&allowMultiQueries=true";
	private HikariDataSource dbPool;
	private String dbUrl;
	private short maxConnCount;

	private static DBPoolMgr instance = new DBPoolMgr();

	public static DBPoolMgr getInstance()
	{
		return instance;
	}

	public boolean initDBPool(DBInfo dbInfo)
	{
		this.dbUrl = String.format(URL, dbInfo.ip, dbInfo.port, dbInfo.dbName);
		this.maxConnCount = 32;
		HikariConfig config = new HikariConfig();
		config.setJdbcUrl(dbUrl);
		config.setUsername(dbInfo.userName);
		config.setPassword(dbInfo.password);
		config.addDataSourceProperty("cachePrepStmts", true);
		config.addDataSourceProperty("prepStmtCacheSize", 250);
		config.addDataSourceProperty("prepStmtCacheSqlLimit", 2048);
		config.setMaximumPoolSize(maxConnCount);
		dbPool = new HikariDataSource(config);
		return dbPool != null;
	}

	/**
	 * 获得一个数据库连接
	 */
	public Connection getConn()
	{
		try
		{
			return dbPool.getConnection();
		}
		catch (SQLException e)
		{
			Log.error("获取数据库连接失败", e);
			return null;
		}
	}

	/**
	 * 关闭数据库连接池
	 */
	public void closeDBPool()
	{
		if (dbPool != null)
		{
			dbPool.close();
			dbPool = null;
		}
	}

	public String toString()
	{
		StringBuffer sb = new StringBuffer();
		sb.append("dbUrl:		").append(dbUrl).append("\n");
		sb.append("maxConnCount:		").append(maxConnCount);
		return sb.toString();
	}
}