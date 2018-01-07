package com.changic.rh.db.pool;

import java.sql.Connection;
import java.util.Map;

import com.changic.rh.util.Config;
import com.changic.rh.util.ConfigMgr;
import com.changic.rh.util.Log;

public class DBPoolMgr
{
	/**
	 * 游戏数据库连接池
	 */
	private static DBPool gameDBPool;

	static
	{
		try
		{
			Class.forName("com.mysql.jdbc.Driver");
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	/**
	 * 初始化全局配置中的所有数据库
	 * 
	 * @return
	 */
	public static boolean initAllFromConfig()
	{
		Map<String, Config> configs = ConfigMgr.getDbConfigs();
		if (configs == null || configs.size() == 0)
		{
			Log.error("找不到数据库配置");
			return false;
		}

		for (Map.Entry<String, Config> entry : configs.entrySet())
		{
			String poolName = entry.getKey();
			Config config = entry.getValue();
			if (!initPool(poolName, config))
			{
				return false;
			}
		}
		return true;
	}

	private static boolean initPool(String poolName, Config config)
	{
		try
		{
			String keyParCount = Config.DB_PAR_COUNT_PRE + poolName.toLowerCase();
			String keyMinCount = Config.DB_MAX_COUNT_PRE + poolName.toLowerCase();
			String keyMaxCount = Config.DB_MIN_COUNT_PRE + poolName.toLowerCase();
			int partitionCount = ConfigMgr.getInt(keyParCount);
			int maxConnCount = ConfigMgr.getInt(keyMinCount);
			int minConnCount = ConfigMgr.getInt(keyMaxCount);
			partitionCount = partitionCount > 0 ? partitionCount : 1;
			minConnCount = minConnCount > 0 ? minConnCount : 2;
			maxConnCount = maxConnCount > 0 ? maxConnCount : 5;

			String host = config.getString(Config.GLOBAL_ATTR_DB_HOST);
			String port = config.getString(Config.GLOBAL_ATTR_DB_PORT);
			String dbName = config.getString(Config.GLOBAL_ATTR_DB_DBNAME);
			String username = config.getString(Config.GLOBAL_ATTR_DB_USERNAME);
			String password = config.getString(Config.GLOBAL_ATTR_DB_PASSWORD);
			gameDBPool = new DBPool(poolName, host, port, dbName, username, password, partitionCount, minConnCount, maxConnCount);
			Log.info("启动数据库连接池完成!\n" + gameDBPool.toString());
			return true;
		}
		catch (Exception e)
		{
			Log.error("启动数据库连接池失败!", e);
			return false;
		}
	}

	public static void shutdown()
	{
		if (gameDBPool != null)
		{
			gameDBPool.shutdown();
		}
	}

	/**
	 * 根据连接池名获得一个数据库连接
	 */
	public static Connection getConnection()
	{
		if (gameDBPool == null)
		{
			Log.error("获取数据库连接池失败, PoolName:  " + Config.GLOBAL_TAG_DB_GAME);
			return null;
		}
		return gameDBPool.getConnection();
	}
}
