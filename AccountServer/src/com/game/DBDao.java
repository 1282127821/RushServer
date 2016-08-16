package com.game;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Arrays;
import java.util.List;

import com.util.GameLog;

public abstract class DBDao {
	protected abstract Connection openConn();

	protected boolean execNoneQuery(String sql) {
		Connection conn = openConn();
		if (conn == null) {
			return false;
		}

		boolean result = false;
		PreparedStatement pstmt = null;
		try {
			pstmt = conn.prepareStatement(sql);
			result = pstmt.executeUpdate() > -1;
		} catch (Exception ex) {
			GameLog.error(String.format(DbMessageType.Format3, DbMessageType.Sql_Error, ex.getMessage(), "调用Sql语句" + sql + "出错"), ex);
		} finally {
			closeConn(conn, pstmt);
		}
		return result;
	}
	
	protected PreparedStatement execQuery(String sql) {
		Connection conn = openConn();
		if (conn == null) {
			return null;
		}

		PreparedStatement pstmt = null;
		try {
			pstmt = conn.prepareStatement(sql);
		} catch (Exception ex) {
			GameLog.error(String.format(DbMessageType.Format3, DbMessageType.Sql_Error, ex.getMessage(), "调用Sql语句" + sql + "出错"), ex);
			closeConn(conn, pstmt);
		}
		return pstmt;
	}

	protected boolean sqlBatch(String tableName, List<String> sqlComm) {
		if (sqlComm == null || sqlComm.size() == 0)
			return true;

		Connection conn = openConn();
		if (conn == null) {
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
