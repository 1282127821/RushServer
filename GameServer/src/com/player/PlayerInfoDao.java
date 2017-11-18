package com.player;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.db.DBDao;
import com.db.DBOption;
import com.util.Log;

public class PlayerInfoDao extends DBDao
{
	private static final String insertSql = "insert into tbl_playerinfo(UserId, AccountId, UserName, JobId, CreateTime, PlayerLv) values(?, ?, ?, ?, ?, ?);";
	private static final String updateSql = "update tbl_playerinfo set Diamond = ?, Gold = ?, JobId = ?, PlayerState = ?, NoviceProcess = ?, "
			+ "PlayerLv = ?, FightStrength = ?, PlayerExp = ?, CurrHP = ?, CurrMP = ?, PosX = ?, PosY = ?, PosZ = ?, Direct = ?, LoginTime = ?, LogoutTime = ?, ResetTime = ?, "
			+ "GuildId = ?, BlackUser = ?  where UserId = ?;";
	private static final String selectSql = "select UserId, AccountId, UserName, Diamond, Gold, JobId, PlayerState, NoviceProcess,"
			+ "PlayerLv, FightStrength, PlayerExp, CreateTime, LoginTime, LogoutTime, GuildId, BlackUser from tbl_playerinfo where UserId = ?;";
	private static final String deletePlayerSql = "update tbl_playerinfo set IsDelete = true where UserId = ?;";
	private static final String selectNameSql = "select * from tbl_playerinfo where UserName = ? and IsDelete = false;";
	private static final String resetStateSql = "update tbl_playerinfo set PlayerState = 0;";

	public boolean addPlayerInfo(PlayerInfo info)
	{
		boolean result = false;
		if (info != null && info.beginAdd())
		{
			Connection conn = getConn();
			if (conn == null)
			{
				return result;
			}

			PreparedStatement pstmt = null;
			long userId = info.getUserId();
			try
			{
				pstmt = conn.prepareStatement(insertSql);
				pstmt.setLong(1, userId);
				pstmt.setLong(2, info.getAccountId());
				pstmt.setString(3, info.getUserName());
				pstmt.setInt(4, info.getJobId());
				pstmt.setInt(5, info.getCreateTime());
				pstmt.setInt(6, info.getPlayerLv());
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
			info.commitAdd(userId, result);
		}
		return result;
	}

	public PlayerInfo getPlayerInfo(long userId)
	{
		Connection conn = getConn();
		if (conn == null)
		{
			return null;
		}

		PreparedStatement pstmt = null;
		ResultSet rs = null;
		PlayerInfo info = null;
		try
		{
			pstmt = conn.prepareStatement(selectSql);
			pstmt.setLong(1, userId);
			rs = pstmt.executeQuery();
			while (rs.next())
			{
				info = getPlayerInfo(rs);
			}
		}
		catch (SQLException e)
		{
			Log.error("执行出错" + selectSql, e);
		}
		finally
		{
			closeConn(pstmt, rs);
		}

		return info;
	}

	private PlayerInfo getPlayerInfo(ResultSet rs)
	{
		PlayerInfo info;
		try
		{
			info = new PlayerInfo();
			info.setUserId(rs.getLong("UserId"));
			info.setAccountId(rs.getLong("AccountId"));
			info.setUserName(rs.getString("UserName"));
			info.setDiamond(rs.getInt("Diamond"));
			info.setGold(rs.getInt("Gold"));
			info.setJobId(rs.getInt("JobId"));
			info.setPlayerState(rs.getShort("PlayerState"));
			info.setNoviceProcess(rs.getInt("NoviceProcess"));
			info.setPlayerLv(rs.getInt("PlayerLv"));
			info.setFightStrength(rs.getInt("FightStrength"));
			info.setPlayerExp(rs.getInt("PlayerExp"));
			info.setCreateTime(rs.getInt("CreateTime"));
			info.setLoginTime(rs.getInt("LoginTime"));
			info.setLogoutTime(rs.getInt("LogoutTime"));
			info.setGuildId(rs.getLong("GuildId"));
			info.setStrBlackUser(rs.getString("BlackUser"));
			info.setOp(DBOption.NONE);
		}
		catch (Exception e)
		{
			info = null;
		}
		return info;
	}

	public PlayerInfo getPlayerByUserName(String userName)
	{
		Connection conn = getConn();
		if (conn == null)
		{
			return null;
		}

		PreparedStatement pstmt = null;
		ResultSet rs = null;
		PlayerInfo playerInfo = null;
		try
		{
			pstmt = conn.prepareStatement(selectNameSql);
			pstmt.setString(1, userName);
			rs = pstmt.executeQuery();
			while (rs.next())
			{
				playerInfo = getPlayerInfo(rs);
			}
		}
		catch (SQLException e)
		{
			Log.error("执行出错" + selectNameSql, e);
		}
		finally
		{
			closeConn(pstmt, rs);
		}

		return playerInfo;
	}

	public boolean deletePlayer(long userId)
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
			pstmt = conn.prepareStatement(deletePlayerSql);
			pstmt.setLong(1, userId);
			result = pstmt.executeUpdate() > -1;
		}
		catch (Exception ex)
		{
			Log.error("调用Sql语句   " + deletePlayerSql + "出错", ex);
		}
		finally
		{
			closeConn(conn, pstmt);
		}

		return result;
	}

	public void updatePlayerInfo(PlayerInfo info)
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
			long userId = info.getUserId();
			try
			{
				pstmt = conn.prepareStatement(updateSql);
				pstmt.setInt(1, info.getDiamond());
				pstmt.setInt(2, info.getGold());
				pstmt.setInt(3, info.getJobId());
				pstmt.setInt(4, info.getPlayerState());
				pstmt.setInt(5, info.getNoviceProcess());
				pstmt.setInt(6, info.getFightStrength());
				pstmt.setInt(7, info.getPlayerExp());
				pstmt.setInt(8, info.getCurrHP());
				pstmt.setInt(9, info.getCurrMP());
				pstmt.setFloat(10, info.getPosX());
				pstmt.setFloat(11, info.getPosY());
				pstmt.setFloat(12, info.getPosZ());
				pstmt.setFloat(13, info.getDirect());
				pstmt.setInt(14, info.getLoginTime());
				pstmt.setInt(15, info.getLogoutTime());
				pstmt.setInt(16, info.getResetTime());
				pstmt.setLong(17, info.getGuildId());
				pstmt.setString(18, info.getStrBlackUser());
				pstmt.setLong(19, userId);
				result = pstmt.executeUpdate() > -1;
			}
			catch (Exception ex)
			{
				Log.error("调用Sql语句   " + updateSql + "出错", ex);
			}
			finally
			{
				closeConn(conn, pstmt);
			}
			info.commitUpdate(userId, result);
		}
	}

	public void resetState()
	{
		Connection conn = getConn();
		if (conn == null)
		{
			return;
		}

		PreparedStatement pstmt = null;
		try
		{
			pstmt = conn.prepareStatement(resetStateSql);
			pstmt.executeUpdate();
		}
		catch (Exception ex)
		{
			Log.error("调用Sql语句   " + resetStateSql + "出错", ex);
		}
		finally
		{
			closeConn(conn, pstmt);
		}
	}
}
