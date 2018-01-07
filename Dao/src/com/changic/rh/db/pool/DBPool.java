package com.changic.rh.db.pool;

import java.sql.Connection;
import java.sql.SQLException;

import com.changic.rh.util.Log;
import com.jolbox.bonecp.BoneCP;
import com.jolbox.bonecp.BoneCPConfig;

/**
 * 数据库连接池
 * 
 */
public class DBPool
{
	private static final String URL = "jdbc:mysql://%s:%s/%s?useUnicode=true&characterEncoding=UTF-8&autoReconnect=true&failOverReadOnly=false";

	private BoneCP boneCP;
	private String poolName;
	private String dbUrl;
	private int partitionCount;
	private int maxConnCountPerPar;
	private int minConnCountPerPar;

	/**
	 * 创建一个数据库连接池
	 * @param poolName
	 * @param host
	 * @param port
	 * @param dbName
	 * @param username
	 * @param password
	 * @param partitionCount
	 * @param minConnCountPerPar
	 * @param maxConnCountPerPar
	 * @throws Exception
	 */
	protected DBPool(String poolName, String host, String port, String dbName, String username, String password, int partitionCount, int minConnCountPerPar,
			int maxConnCountPerPar) throws Exception
	{
		dbUrl = String.format(URL, host, port, dbName);
		this.poolName = poolName;
		this.partitionCount = partitionCount;
		this.maxConnCountPerPar = maxConnCountPerPar;
		this.minConnCountPerPar = minConnCountPerPar;
		BoneCPConfig config = new BoneCPConfig();
		config.setJdbcUrl(dbUrl);
		config.setUsername(username);
		config.setPassword(password);
		// 分区数
		config.setPartitionCount(partitionCount);
		// 每次创建的新连接个数
		config.setAcquireIncrement(2);
		// 处理释放后的conn线程数
		config.setReleaseHelperThreads(3);
		// 分区最大连接数
		config.setMaxConnectionsPerPartition(maxConnCountPerPar);
		// 分区最小连接数
		config.setMinConnectionsPerPartition(minConnCountPerPar);
		// 空闲连接存活时间
		config.setIdleMaxAgeInMinutes(10);
		// 测试连接时间
		config.setIdleConnectionTestPeriodInMinutes(10);
		// 设置重新获取连接的次数。这个参数默认为5
		config.setAcquireRetryAttempts(5);
		// 重新获取连接的间隔时间
		config.setAcquireRetryDelayInMs(3000);
		boneCP = new BoneCP(config);
	}

	/**
	 * 关闭连接池
	 */
	protected void shutdown()
	{
		boneCP.shutdown();
	}

	protected String getPoolName()
	{
		return poolName;
	}

	protected String getDbUrl()
	{
		return dbUrl;
	}

	/**
	 * 获得一个连接
	 */
	public Connection getConnection()
	{
		try
		{
			return boneCP.getConnection();
		}
		catch (SQLException e)
		{
			Log.error("获取数据库连接异常,name:" + poolName, e);
			return null;
		}
	}

	public String toString()
	{
		StringBuffer sb = new StringBuffer();
		sb.append("poolName:		").append(poolName).append("\n");
		sb.append("partitionCount:		").append(partitionCount).append("\n");
		sb.append("maxConnCount:		").append(maxConnCountPerPar).append("\n");
		sb.append("minConnCount:		").append(minConnCountPerPar).append("\n");
		sb.append("dbUrl:		").append(dbUrl);
		return sb.toString();
	}
}
