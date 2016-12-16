package com.db;

import java.sql.Connection;
import java.sql.SQLException;

import com.util.GameLog;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

public class DBPool implements IDBPool
{
	private HikariDataSource dbPool = null;

	/**
	 * <!-- Hikari Datasource -->
	 * <bean id="dataSourceHikari" class="com.zaxxer.hikari.HikariDataSource"
	 * destroy-method="shutdown"> <!--
	 * <property name="driverClassName" value="${db.driverClass}" /> --> <!--
	 * 无需指定，除非系统无法自动识别 --> <property name="jdbcUrl" value=
	 * "jdbc:mysql://localhost:3306/test?useUnicode=true&characterEncoding=UTF-8"
	 * /> <property name="username" value="${db.username}" />
	 * <property name= "password" value="${db.password}" /> <!--
	 * 连接只读数据库时配置为true， 保证安全 --> <property name="readOnly" value="false" /> <!--
	 * 等待连接池分配连接的最大时长（毫秒），超过这个时长还没可用的连接则发生SQLException， 缺省:30秒 -->
	 * <property name="connectionTimeout" value="30000" /> <!--
	 * 一个连接idle状态的最大时长（毫秒），超时则被释放（retired），缺省:10分钟 -->
	 * <property name="idleTimeout" value="600000" /> <!--
	 * 一个连接的生命时长（毫秒），超时而且没被使用则被释放（retired），缺省:30分钟，建议设置比数据库超时时长少30秒，参考MySQL
	 * wait_timeout参数（show variables like '%timeout%';） -->
	 * <property name="maxLifetime" value="1800000" /> <!--
	 * 连接池中允许的最大连接数。缺省值：10；推荐的公式：((core_count * 2) + effective_spindle_count)
	 * --> <property name="maximumPoolSize" value="15" /> </bean> A typical
	 * MySQL configuration for HikariCP might look something like this:
	 * dataSourceClassName=com.mysql.jdbc.jdbc2.optional.MysqlDataSource
	 * dataSource.url=jdbc:mysql://localhost/database dataSource.user=test
	 * dataSource.password=test dataSource.cachePrepStmts=true
	 * dataSource.prepStmtCacheSize=250 dataSource.prepStmtCacheSqlLimit=2048
	 */
	public DBPool(String dbJdbcUrl, String dbUserName, String dbPassWord, String poolName, int minConn, int maxConn) throws Exception
	{
		HikariConfig config = new HikariConfig();
		config.setDriverClassName("com.mysql.jdbc.Driver");
		config.setJdbcUrl(dbJdbcUrl);
		config.setUsername(dbUserName);
		config.setPassword(dbPassWord);
		config.setPoolName(poolName);
		config.addDataSourceProperty("cachePrepStmts", true);
		config.addDataSourceProperty("prepStmtCacheSize", 250);
		config.addDataSourceProperty("prepStmtCacheSqlLimit", 2048);
		config.setMaximumPoolSize(maxConn);
		dbPool = new HikariDataSource(config);
	}

	public void shutdown()
	{
		if (dbPool != null)
		{
			dbPool.close();
		}
	}

	public HikariDataSource getDBPool()
	{
		return dbPool;
	}

	public String getPoolState()
	{
		// StringBuilder sb = new StringBuilder();
		// try {
		// sb.append("total:" +
		// connectionPool.getTotalCreatedConnections()).append(",");
		// sb.append("used:" + connectionPool.getTotalLeased()).append(",");
		// sb.append("free:" + connectionPool.getTotalFree());
		// } catch (Exception e) {
		// GameLog.error("Pool Error", e);
		// }
		// return sb.toString();
		// TODO:LZGLZG 这里打印结果看看
		return dbPool.toString();
	}

	public Connection getConnection()
	{
		try
		{
			return dbPool.getConnection();
		}
		catch (SQLException e)
		{
			GameLog.error("获取数据库连接失败", e);
			return null;
		}
	}

	public boolean isConnect()
	{
		return dbPool != null ? dbPool.isClosed() : false;
	}
}
