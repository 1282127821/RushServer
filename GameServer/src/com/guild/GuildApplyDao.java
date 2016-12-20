package com.guild;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.db.DBDao;
import com.db.DBOption;
import com.util.GameLog;

public class GuildApplyDao extends DBDao
{
	private static final String insertSql = "insert into tbl_guildapply (GuildId, UserId, ApplyTime) values(?, ?, ?);";
	private static final String selectSql = "select tp.UserId, tg.GuildId, UserName, JobId, PlayerLv, VipLv, FightStrength, ApplyTime from tbl_playerinfo as tp "
			+ "inner join tbl_guildapply as tg on tp.UserId = tg.UserId;";
	private static final String deleteApplySql = "delete from tbl_guildapply where GuildId = ? and UserId = ?;";
	private static final String deleteAllApplySql = "delete from tbl_guildapply where GuildId = ?;";

	public void addGuildApplyInfo(long guildId, GuildApplyInfo info)
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
				pstmt = conn.prepareStatement(insertSql);
				pstmt.setLong(1, guildId);
				pstmt.setLong(2, info.getUserId());
				pstmt.setInt(3, info.getApplyTime());
				result = pstmt.executeUpdate() > -1;
			}
			catch (Exception ex)
			{
				GameLog.error("调用Sql语句   " + insertSql + "出错", ex);
			}
			finally
			{
				closeConn(conn, pstmt);
			}
			info.commitAdd(guildId, result);
		}
	}

	public Map<Long, List<GuildApplyInfo>> getAllGuildApply()
	{
		Map<Long, List<GuildApplyInfo>> guildApplyMap = new HashMap<Long, List<GuildApplyInfo>>();
		Connection conn = getConn();
		if (conn == null)
		{
			return guildApplyMap;
		}

		ResultSet rs = null;
		PreparedStatement pstmt = null;
		try
		{
			pstmt = conn.prepareStatement(selectSql);
			rs = pstmt.executeQuery();
			while (rs.next())
			{
				long guildId = rs.getLong("GuildId");
				List<GuildApplyInfo> guildApplyList = guildApplyMap.get(guildId);
				if (guildApplyList == null)
				{
					guildApplyList = new ArrayList<GuildApplyInfo>();
					guildApplyMap.put(guildId, guildApplyList);
				}

				GuildApplyInfo info = new GuildApplyInfo();
				info.setUserId(rs.getLong("UserId"));
				info.setUserName(rs.getString("UserName"));
				info.setJobId(rs.getInt("JobId"));
				info.setPlayerLv(rs.getInt("PlayerLv"));
				info.setVipLv(rs.getInt("VipLv"));
				info.setFightStrength(rs.getInt("FightStrength"));
				info.setApplyTime(rs.getInt("ApplyTime"));
				info.setOp(DBOption.NONE);
				guildApplyList.add(info);
			}
		}
		catch (SQLException e)
		{
			GameLog.error("执行出错" + selectSql, e);
		}
		finally
		{
			closeConn(pstmt, rs);
		}

		return guildApplyMap;
	}

	public boolean deleteGuildApplyInfo(long guildId, long userId)
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
			pstmt = conn.prepareStatement(deleteApplySql);
			pstmt.setLong(1, guildId);
			pstmt.setLong(2, userId);
			result = pstmt.executeUpdate() > -1;
		}
		catch (Exception ex)
		{
			GameLog.error("调用Sql语句   " + deleteApplySql + "出错", ex);
		}
		finally
		{
			closeConn(conn, pstmt);
		}

		return result;
	}

	public boolean deleteAllGuildApplyInfo(long guildId)
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
			pstmt = conn.prepareStatement(deleteAllApplySql);
			pstmt.setLong(1, guildId);
			result = pstmt.executeUpdate() > -1;
		}
		catch (Exception ex)
		{
			GameLog.error("调用Sql语句   " + deleteAllApplySql + "出错", ex);
		}
		finally
		{
			closeConn(conn, pstmt);
		}

		return result;
	}
}
