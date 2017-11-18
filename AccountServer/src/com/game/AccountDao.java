package com.game;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.db.DBDao;
import com.util.Log;
import com.util.TimeUtil;

public class AccountDao extends DBDao
{
	private static final String insertSql = "insert into tbl_account(AccountId, AccountName, CreateTime, LoginIp, Imei, Model, Brand, GameId) value(?, ?, ?, ?, ?, ?, ?, ?);";
	private static final String selectAccountNameSql = "select * from tbl_account where AccountName = ?;";
	private static final String forbidSql = "update tbl_account set LastLockoutDate = NOW(), ForbidReason = ?, IsForbid = ?, ForbidExpirtDate = ?, ForbidOperator = ? where AccountId = ?;";
	private static final String updateLoginSql = "update tbl_account set LastLoginDate = ?, LoginCount = ?, LoginIp = ?, DeleteCoolTime = ?,LastLogOutDate= ? where AccountId = ?;";

	public boolean addAccount(AccountInfo info)
	{
		Connection conn = getConn();
		if (conn == null)
		{
			return false;
		}

		boolean result = false;
		PreparedStatement pstmt = null;
		try
		{
			pstmt = conn.prepareStatement(insertSql);
			pstmt.setLong(1, info.getAccountId());
			pstmt.setString(2, info.getAccountName());
			pstmt.setInt(3, info.getCreateTime());
			pstmt.setString(4, info.getLoginIP());
			pstmt.setString(5, info.getImei());
			pstmt.setString(6, info.getModel());
			pstmt.setString(7, info.getBrand());
			pstmt.setInt(8, info.getGameId());
			result = pstmt.executeUpdate() > -1;
		}
		catch (Exception ex)
		{
			Log.error("调用Sql语句   " + insertSql + "出错", ex);
		}
		finally
		{
			closeConn(conn, pstmt);
		}

		return result;
	}

	public AccountInfo getAccount(String accountName)
	{
		Connection conn = getConn();
		if (conn == null)
		{
			return null;
		}

		ResultSet rs = null;
		PreparedStatement pstmt = null;
		AccountInfo info = null;
		try
		{
			pstmt = conn.prepareStatement(selectAccountNameSql);
			pstmt.setString(1, accountName);
			rs = pstmt.executeQuery();
			if (rs.last())
			{
				info = new AccountInfo();
				info.setAccountId(rs.getLong("AccountId"));
				info.setAccountName(rs.getString("AccountName"));
				info.setCreateTime(rs.getInt("CreateTime"));
				info.setLoginTime(rs.getInt("LoginTime"));
				info.setLogoutTime(rs.getInt("LogoutTime"));
				info.setLoginCount(rs.getInt("LoginCount"));
				info.setLoginIP(rs.getString("LoginIp"));
				info.setForbidReason(rs.getString("ForbidReason"));
				info.setForbidExpireTime(rs.getInt("ForbidExpirtTime"));
				info.setModel(rs.getString("Model"));
				info.setBrand(rs.getString("Brand"));
				info.setGameId(rs.getInt("GameId"));
				info.setDelCDTime(rs.getInt("DelCDTime"));
			}
		}
		catch (SQLException e)
		{
			info = null;
			Log.error("执行出错", e);
		}
		finally
		{
			closeConn(pstmt, rs);
		}

		return info;
	}

	public boolean updateLoginAccount(AccountInfo account)
	{
		// if (!TimeUtil.dateCompare(account.getLastLoginDate())) {
		// account.setLoginTime(TimeUtil.getSysCurSeconds());
		// account.setLoginCount(account.getLoginCount() + 1);
		// }

		Connection conn = getConn();
		if (conn == null)
		{
			return false;
		}

		boolean result = false;
		PreparedStatement pstmt = null;
		try
		{
			pstmt = conn.prepareStatement(updateLoginSql);
			pstmt.setLong(1, account.getLoginTime());
			pstmt.setInt(2, account.getLoginCount());
			pstmt.setString(3, account.getLoginIP());
			pstmt.setInt(4, account.getDelCDTime());
			pstmt.setInt(5, account.getLogoutTime());
			pstmt.setLong(6, account.getAccountId());

			result = pstmt.executeUpdate() > -1;
		}
		catch (Exception ex)
		{
			Log.error("调用Sql语句   " + updateLoginSql + "出错", ex);
		}
		finally
		{
			closeConn(conn, pstmt);
		}

		return result;
	}

	public boolean forbidAccount(long accountId, int forbidExpiretTime, String forbidReason, String forbidOperator)
	{
		Connection conn = getConn();
		if (conn == null)
		{
			return false;
		}

		boolean result = false;
		PreparedStatement pstmt = null;
		try
		{
			pstmt = conn.prepareStatement(forbidSql);
			pstmt.setLong(1, accountId);
			pstmt.setInt(2, forbidExpiretTime);
			pstmt.setString(3, forbidReason);
			pstmt.setString(4, forbidOperator);
			result = pstmt.executeUpdate() > -1;
		}
		catch (Exception ex)
		{
			Log.error("调用Sql语句   " + forbidSql + "出错", ex);
		}
		finally
		{
			closeConn(conn, pstmt);
		}

		return result;
	}
}
