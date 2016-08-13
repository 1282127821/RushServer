package com.db;

import java.sql.Connection;
import java.sql.SQLException;

import com.util.GameLog;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

public class DBPool implements IDBPool {
	private HikariDataSource dbPool = null;

	/**
	 * <!-- Hikari Datasource -->
	 * <bean id="dataSourceHikari" class="com.zaxxer.hikari.HikariDataSource"
	 * destroy-method="shutdown"> <!--
	 * <property name="driverClassName" value="${db.driverClass}" /> --> <!--
	 * 无需指定，除非系统无法自动识别 --> <property name="jdbcUrl" value=
	 * "jdbc:mysql://localhost:3306/test?useUnicode=true&characterEncoding=UTF-8"
	 * /> <property name="username" value="${db.username}" /> <property name=
	 * "password" value="${db.password}" /> <!-- 连接只读数据库时配置为true， 保证安全 -->
	 * <property name="readOnly" value="false" /> <!--
	 * 等待连接池分配连接的最大时长（毫秒），超过这个时长还没可用的连接则发生SQLException， 缺省:30秒 -->
	 * <property name="connectionTimeout" value="30000" /> <!--
	 * 一个连接idle状态的最大时长（毫秒），超时则被释放（retired），缺省:10分钟 -->
	 * <property name="idleTimeout" value="600000" /> <!--
	 * 一个连接的生命时长（毫秒），超时而且没被使用则被释放（retired），缺省:30分钟，建议设置比数据库超时时长少30秒，参考MySQL
	 * wait_timeout参数（show variables like '%timeout%';） -->
	 * <property name="maxLifetime" value="1800000" /> <!--
	 * 连接池中允许的最大连接数。缺省值：10；推荐的公式：((core_count * 2) + effective_spindle_count)
	 * --> <property name="maximumPoolSize" value="15" /> </bean>
	 */
	public DBPool(String dbConnUrl, String dbUserName, String dbPassWord, int maxConn) throws Exception {
		Class.forName("com.mysql.jdbc.Driver").newInstance();
		HikariConfig config = new HikariConfig();
		config.setJdbcUrl(dbConnUrl);
		config.setUsername(dbUserName);
		config.setPassword(dbPassWord);
		config.setReadOnly(false);
		config.setConnectionTimeout(30000);
		config.setIdleTimeout(600000);
		config.setMaxLifetime(1800000);
		config.setMaximumPoolSize(maxConn);
		dbPool = new HikariDataSource(config);
	}

	public void shutdown() {
		if (dbPool != null) {
			dbPool.close();
		}
	}

	public HikariDataSource getDBPool() {
		return dbPool;
	}
	
	public String getState() {
		return dbPool.toString();
	} 

	public Connection getConnection() {
		try {
			return dbPool.getConnection();
		} catch (SQLException e) {
			GameLog.error("获取数据库连接失败", e);
			return null;
		}
	}

	@Override
	public int getCurConns() {
//		return dbPool != null ? dbPool : 0;
		return 0;
	}
}
