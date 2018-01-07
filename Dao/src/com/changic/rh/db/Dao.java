package com.changic.rh.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import com.changic.rh.db.pool.DBPoolMgr;
import com.changic.rh.util.Log;

public abstract class Dao
{
	/**
	 * 查询,返回的结果类型取决于reader的泛型,<br>
	 * <b>若reader读取时未达到预期效果(返回false),则查询结果为null</b>
	 * 
	 * @param sql
	 * @param reader
	 * @return
	 */
	public <T> T executeQuery(String sql, ResultReader<T> reader)
	{
		return executeQuery(sql, null, reader);
	}

	/**
	 * 查询,返回的结果类型取决于reader的泛型,<br>
	 * <b>若reader读取时未达到预期效果(返回false),则查询结果为null</b>
	 * 
	 * @param sql
	 * @param params
	 *            list形式传参
	 * @param reader
	 * @return
	 */
	public <T> T executeQuery(String sql, List<Object> params, ResultReader<T> reader)
	{
		Connection conn = getConnection();
		if (conn == null)
		{
			return null;
		}
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try
		{
			pstmt = conn.prepareStatement(sql);
			convertParams(pstmt, params);
			rs = pstmt.executeQuery();
			reader.readRs(rs);
			return reader.getResult();
		}
		catch (Exception e)
		{
			Log.error("数据库查询异常,sql:" + sql, e);
			return null;
		}
		finally
		{
			closeConn(pstmt, rs);
		}
	}

	/**
	 * 批量执行,启用事务
	 * 
	 * @param pstmtSql
	 * @param entitys
	 *            被执行的实体参数集合
	 * @return
	 */
	public boolean executeBatch(String pstmtSql, List<List<Object>> entitys)
	{
		Connection conn = getConnection();
		if (conn == null || entitys == null)
		{
			return false;
		}
		PreparedStatement pstmt = null;
		try
		{
			conn.setAutoCommit(false);
			pstmt = conn.prepareStatement(pstmtSql, ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
			for (List<Object> entity : entitys)
			{
				convertParams(pstmt, entity);
				pstmt.addBatch();
			}
			pstmt.executeBatch();
			conn.commit();
			return true;
		}
		catch (Exception e)
		{
			Log.error("批量执行异常!sql:" + pstmtSql, e);
			rollback(conn);
			return false;
		}
		finally
		{
			try
			{
				conn.setAutoCommit(true);
			}
			catch (SQLException e)
			{
				Log.error("重置connection自动提交异常", e);
			}
			closeConn(conn, pstmt);
		}
	}

	public int executeUpdate(String sql, List<Object> params)
	{
		Connection conn = getConnection();
		if (conn == null)
		{
			return -1;
		}
		PreparedStatement pstmt = null;
		try
		{
			pstmt = conn.prepareStatement(sql);
			convertParams(pstmt, (List<Object>) params);
			return pstmt.executeUpdate();
		}
		catch (Exception e)
		{
			Log.error("sql执行异常,sql:" + sql, e);
			return -1;
		}
		finally
		{
			closeConn(conn, pstmt);
		}
	}

	public boolean execute(String sql, List<Object> params)
	{
		return execute0(sql, params) > -1;
	}

	final protected int execute0(String sql, List<Object> params)
	{
		Connection conn = getConnection();
		if (conn == null)
		{
			return -1;
		}
		PreparedStatement pstmt = null;
		try
		{
			pstmt = conn.prepareStatement(sql);
			convertParams(pstmt, (List<Object>) params);
			return pstmt.executeUpdate();
		}
		catch (Exception e)
		{
			Log.error("sql执行异常,sql:" + sql, e);
			return -1;
		}
		finally
		{
			closeConn(conn, pstmt);
		}
	}

	public boolean execute(String sql, Object... params)
	{
		return execute0(sql, params) > -1;
	}

	final protected int execute0(String sql, Object... params)
	{
		Connection conn = getConnection();
		if (conn == null)
		{
			return -1;
		}

		PreparedStatement pstmt = null;
		try
		{
			pstmt = conn.prepareStatement(sql);
			convertParams(pstmt, params);
			return pstmt.executeUpdate();
		}
		catch (Exception e)
		{
			Log.error("sql执行异常,sql:" + sql, e);
			return -1;
		}
		finally
		{
			closeConn(conn, pstmt);
		}
	}

	protected boolean execute(Connection conn, String sql, List<Object> params) throws Exception
	{
		PreparedStatement pstmt = conn.prepareStatement(sql);
		int result = -1;
		try
		{
			convertParams(pstmt, params);
			result = pstmt.executeUpdate();
		}
		finally
		{
			pstmt.close();
		}
		return result == 1;
	}

	/**
	 * 执行SQL，返回主键ID
	 * 
	 * @param sql
	 * @param params
	 * @return
	 */
	public long execAndGetLastId(String sql)
	{
		long result = -1;
		Connection conn = getConnection();
		if (conn == null)
		{
			return result;
		}
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try
		{
			pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			pstmt.executeUpdate();
			rs = pstmt.getGeneratedKeys();
			if (rs.next())
			{
				result = rs.getLong(1);
			}
		}
		catch (Exception e)
		{
			Log.error("sql执行异常,sql:" + sql, e);
		}
		finally
		{
			closeConn(pstmt, rs);
		}
		return result;
	}

	/**
	 * 参数转换
	 * 
	 * @param pstmt
	 * @param params
	 * @throws Exception
	 */
	public void convertParams(PreparedStatement pstmt, List<Object> params) throws Exception
	{
		if (params != null)
		{
			int i = 0;
			for (Object value : params)
			{
				pstmt.setObject(++i, value);
			}
		}
	}

	public void convertParams(PreparedStatement pstmt, Object... params) throws Exception
	{
		int i = 0;
		for (Object value : params)
		{
			pstmt.setObject(++i, value);
		}
	}

	protected void closeConn(Connection conn)
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
			Log.error("关闭db连接时异常", e);
		}
	}

	private void closeConn(Connection conn, PreparedStatement pstmt)
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
			Log.error("关闭db连接时异常", e);
		}
		closeConn(conn);
	}

	private void closeConn(Connection conn, PreparedStatement pstmt, ResultSet rs)
	{
		try
		{
			if (rs != null)
				rs.close();
		}
		catch (SQLException e)
		{
			Log.error("关闭db连接时异常", e);
		}
		closeConn(conn, pstmt);
	}

	protected void closeConn(PreparedStatement pstmt, ResultSet rs)
	{
		try
		{
			if (pstmt != null)
			{
				Connection conn = pstmt.getConnection();
				closeConn(conn, pstmt, rs);
			}
		}
		catch (SQLException e)
		{
			Log.error("关闭db连接时异常", e);
		}
	}

	/** 数据回滚 **/
	protected void rollback(Connection conn)
	{
		try
		{
			conn.rollback();
		}
		catch (Exception e1)
		{
			Log.error("数据回滚异常", e1);
		}
	}

	/**
	 * 获得数据库连接
	 * 
	 * @return
	 */
	protected Connection getConnection()
	{
		return DBPoolMgr.getConnection();
	}
}
