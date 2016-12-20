package com.friend;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.db.DBDao;
import com.db.DBOption;
import com.util.GameLog;

public class FriendDBInfoDao extends DBDao
{
	private static final String insertFriendSql = "insert into tbl_friend(UserId, FriendGroup, FriendCount, EnemyGroup, EnemyCount, BattleFriendGroup, BattleFriendCount) values(?, ?, ?, ?, ?, ?, ?);";
	private static final String updateFriendSql = "update tbl_friend set FriendGroup = ?, FriendCount = ?, EnemyGroup = ?, EnemyCount = ?, BattleFriendGroup = ?, BattleFriendCount = ? where UserId = ?;";
	private static final String selectFriendSql = "select FriendGroup, FriendCount, EnemyGroup, EnemyCount, BattleFriendGroup, BattleFriendCount from tbl_friend where UserId = ?";

	public void insertFriendDBInfo(long userId, FriendDBInfo info)
	{
		if (info != null && info.beginAdd())
		{
			Connection conn = getConn();
			if (conn == null)
			{
				return;
			}

			boolean result = false;
			PreparedStatement pstmt = null;
			try
			{
				pstmt = conn.prepareStatement(insertFriendSql);
				pstmt.setLong(1, userId);
				pstmt.setString(2, info.getStrFriendGroup());
				pstmt.setShort(3, info.getFriendCount());
				pstmt.setString(4, info.getStrEnemyGroup());
				pstmt.setShort(5, info.getEnemyCount());
				pstmt.setString(6, info.getStrBattleFriendGroup());
				pstmt.setShort(7, info.getBattleFriendCount());
				result = pstmt.executeUpdate() > -1;
			}
			catch (Exception ex)
			{
				GameLog.error("调用Sql语句   " + insertFriendSql + "出错", ex);
			}
			finally
			{
				closeConn(conn, pstmt);
			}
			info.commitAdd(userId, result);
		}
	}

	public void updateFriendDBInfo(long userId, FriendDBInfo info)
	{
		if (info != null && info.beginUpdate())
		{
			Connection conn = getConn();
			if (conn == null)
			{
				return;
			}

			boolean result = false;
			PreparedStatement pstmt = null;
			try
			{
				pstmt = conn.prepareStatement(updateFriendSql);
				pstmt.setString(1, info.getStrFriendGroup());
				pstmt.setShort(2, info.getFriendCount());
				pstmt.setString(3, info.getStrEnemyGroup());
				pstmt.setShort(4, info.getEnemyCount());
				pstmt.setString(5, info.getStrBattleFriendGroup());
				pstmt.setShort(6, info.getBattleFriendCount());
				pstmt.setLong(7, userId);
				result = pstmt.executeUpdate() > -1;
			}
			catch (Exception ex)
			{
				GameLog.error("调用Sql语句   " + updateFriendSql + "出错", ex);
			}
			finally
			{
				closeConn(conn, pstmt);
			}
			info.commitUpdate(userId, result);
		}
	}

	public FriendDBInfo getFriendDBInfo(long userId)
	{
		Connection conn = getConn();
		if (conn == null)
		{
			return null;
		}

		ResultSet rs = null;
		PreparedStatement pstmt = null;
		FriendDBInfo info = null;
		try
		{
			pstmt = conn.prepareStatement(selectFriendSql);
			pstmt.setLong(1, userId);
			rs = pstmt.executeQuery();
			while (rs.next())
			{
				info = new FriendDBInfo();
				info.setStrFriendGroup(rs.getString("FriendGroup"));
				info.setFriendCount(rs.getShort("FriendCount"));
				info.setStrEnemyGroup(rs.getString("EnemyGroup"));
				info.setEnemyCount(rs.getShort("EnemyCount"));
				info.setStrBattleSFriendGroup(rs.getString("BattleFriendGroup"));
				info.setBattleFriendCount(rs.getShort("BattleFriendCount"));
				info.setOp(DBOption.NONE);
			}
		}
		catch (SQLException e)
		{
			GameLog.error("执行出错" + selectFriendSql, e);
		}
		finally
		{
			closeConn(pstmt, rs);
		}

		return info;
	}
}