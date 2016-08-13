package com.rush.game;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import com.rush.util.GameLog;
import com.rush.util.TimeUtil;

public abstract class BaseDao {
	protected abstract Connection openConn();

	protected boolean execNoneQuery(String sql) {
		return execNoneQuery(sql, null);
	}

	private void prepareCommand(PreparedStatement pstmt, Map<Integer, DbParameter> parms) throws SQLException {
		if (pstmt == null || parms == null)
			return;

		for (Map.Entry<Integer, DbParameter> entry : parms.entrySet()) {
			pstmt.setObject(entry.getKey(), entry.getValue().getResult());
		}
	}

	protected boolean execNoneQuery(String sql, Map<Integer, DbParameter> params) {
		DbWatch watch = new DbWatch();
		boolean result = false;
		Connection conn = openConn();
		watch.getPool();
		if (conn == null) {
			watch.commit(sql);
			return result;
		}

		PreparedStatement pstmt = null;
		try {
			pstmt = conn.prepareStatement(sql);
			prepareCommand(pstmt, params);
			result = pstmt.executeUpdate() > -1;
		} catch (Exception ex) {
			GameLog.error(String.format(DbMessageType.Format3, DbMessageType.Sql_Error, ex.getMessage(), "调用Sql语句" + sql + "出错"), ex);
		} finally {
			closeConn(conn, pstmt);
			watch.commit(sql);
		}
		return result;
	}

	protected PreparedStatement execQuery(String sql) {
		return execQuery(sql, null);
	}
	
	protected PreparedStatement execQuery(String sql, Map<Integer, DbParameter> params) {
		DbWatch watch = new DbWatch();
		Connection conn = openConn();
		watch.getPool();
		if (conn == null) {
			watch.commit(sql);
			return null;
		}

		PreparedStatement pstmt = null;
		try {
			pstmt = conn.prepareStatement(sql);
			prepareCommand(pstmt, params);
		} catch (Exception ex) {
			GameLog.error(String.format(DbMessageType.Format3, DbMessageType.Sql_Error, ex.getMessage(), "调用Sql语句" + sql + "出错"), ex);
			closeConn(conn, pstmt);
		} finally {
			watch.commit(sql);
		}
		return pstmt;
	}

	protected int execCountQuery(String sql, Map<Integer, DbParameter> params) {
		DbWatch watch = new DbWatch();
		int result = -1;
		Connection conn = openConn();
		watch.getPool();
		if (conn == null) {
			watch.commit(sql);
			return result;
		}
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			pstmt = conn.prepareStatement(sql);
			prepareCommand(pstmt, params);
			rs = pstmt.executeQuery();
			if ((rs != null) && (rs.next())) {
				result = rs.getInt(1);
			}
		} catch (Exception e) {
			GameLog.error("统计Count出错, 调用Sql语句 : " + sql + "出错", e);
			result = -1;
		} finally {
			closeConn(conn, pstmt, rs);
			watch.commit(sql);
		}
		return result;
	}

	protected boolean sqlBatch(String tableName, List<String> sqlComm) {
		if (sqlComm == null || sqlComm.size() == 0)
			return true;

		DbWatch watch = new DbWatch();
		Connection conn = openConn();
		watch.getPool();
		if (conn == null) {
			watch.commit(tableName);
			return false;
		}
		Statement stmt = null;
		try {
			stmt = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
			for (String sqlString : sqlComm) {
				stmt.addBatch(sqlString);
			}
			stmt.executeBatch();
			return true;
		} catch (SQLException e) {
			GameLog.error("批处理执行SQL语句出错: " + Arrays.toString(sqlComm.toArray()), e);
		} finally {
			closeConn(conn, stmt);
			watch.commit(tableName);
		}
		return false;
	}

	private void closeConn(Connection conn) {
		try {
			if (conn != null) {
				conn.close();
			}
		} catch (SQLException e) {
			GameLog.error(DbMessageType.Connect_Close, e);
		}
	}

	private void closeConn(Connection conn, PreparedStatement pstmt) {
		try {
			if (pstmt != null) {
				pstmt.clearParameters();
				pstmt.close();
				pstmt = null;
			}
		} catch (SQLException e) {
			GameLog.error(DbMessageType.Connect_Close, e);
		}
		closeConn(conn);
	}

	private void closeConn(Connection conn, PreparedStatement pstmt, ResultSet rs) {
		try {
			if (rs != null)
				rs.close();
		} catch (SQLException e) {
			GameLog.error(DbMessageType.Connect_Close, e);
		}
		closeConn(conn, pstmt);
	}

	protected void closeConn(Connection conn, Statement statement) {
		if (statement != null) {
			try {
				statement.clearBatch();
				statement.close();
			} catch (SQLException e) {
				GameLog.error(DbMessageType.Connect_Close, e);
			}
		}
		closeConn(conn);
	}

	protected void closeConn(PreparedStatement pstmt, ResultSet rs) {
		try {
			if (pstmt != null) {
				Connection conn = pstmt.getConnection();
				closeConn(conn, pstmt, rs);
			}
		} catch (SQLException e) {
			GameLog.error("关闭db连接时异常", e);
		}
	}
}

class DbWatch {
	private long first = 0;
	private long second = 0;

	public DbWatch() {
		first = TimeUtil.getSysCurTimeMillis();
	}

	public void getPool() {
		second = TimeUtil.getSysCurTimeMillis();
	}

	public void commit(String procName) {
		long end = TimeUtil.getSysCurTimeMillis();
		long spendTime = end - first;
		if (spendTime > 1000) {
			GameLog.error(String.format("执行语句%s花耗时间总时间 超过:%sms,获取连接：%sms,执行sql:%sms", procName, spendTime, second - first, end - second));
			GameLog.error("MainDB PoolState:" + DBPoolMgr.getInstaqnce().getStrategyPoolState());
		}
	}
}
