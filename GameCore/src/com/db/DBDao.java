package com.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.util.Log;

public abstract class DBDao
{
	protected Connection getConn()
	{
		return DBPoolMgr.getInstance().getConn();
	}

	private void closeConn(Connection conn)
	{
		try
		{
			if (conn != null)
			{
				conn.close();
			}
		}
		catch (SQLException e)
		{
			Log.error("关闭数据库出错", e);
		}
	}

	protected void closeConn(Connection conn, PreparedStatement pstmt)
	{
		try
		{
			if (pstmt != null)
			{
				pstmt.close();
				pstmt = null;
			}
		}
		catch (SQLException e)
		{
			Log.error("关闭数据库出错", e);
		}
		closeConn(conn);
	}

	protected void closeConn(PreparedStatement pstmt, ResultSet rs)
	{
		try
		{
			if (pstmt != null)
			{
				if (rs != null)
					rs.close();

				closeConn(pstmt.getConnection(), pstmt);
			}
		}
		catch (SQLException e)
		{
			Log.error("关闭db连接时异常", e);
		}
	}
}
