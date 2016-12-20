package com.guild;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.db.DBDao;
import com.db.DBOption;
import com.util.GameLog;

public class GuildDao extends DBDao
{
	private static final String insertSql = "insert into tbl_guild (GuildId, GuildName, GuildSlogan, GuildEmblem, GuildLv, TotalExp, IsAudit, CreateTime, IsExist) values(?, ?, ?, ?, ?, ?, ?, ?, ?);";
	private static final String updateSql = "update tbl_guild set GuildSlogan = ?, GuildLv = ?, TotalExp = ?, IsAudit = ? where GuildId = ?;";
	private static final String selectSql = "select GuildId, GuildName, GuildSlogan, GuildEmblem, GuildLv, TotalExp, IsAudit, CreateTime from tbl_guild where IsExist = true;";
	private static final String deleteSql = "update tbl_guild set IsExist = false where GuildId = ?;";

	public void addGuildInfo(long guildId, GuildInfo info)
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
				pstmt.setString(2, info.getGuildName());
				pstmt.setString(3, info.getGuildSlogan());
				pstmt.setShort(4, info.getGuildEmblem());
				pstmt.setShort(5, info.getGuildLv());
				pstmt.setInt(6, info.getTotalExp());
				pstmt.setBoolean(7, info.getIsAudit());
				pstmt.setInt(8, info.getCreateTime());
				pstmt.setBoolean(9, info.isExist());
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

	public void updateGuildInfo(long guildId, GuildInfo info)
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
				pstmt = conn.prepareStatement(updateSql);
				pstmt.setShort(1, info.getGuildLv());
				pstmt.setShort(2, info.getGuildLv());
				pstmt.setInt(3, info.getTotalExp());
				pstmt.setBoolean(4, info.getIsAudit());
				pstmt.setLong(5, guildId);
				result = pstmt.executeUpdate() > -1;
			}
			catch (Exception ex)
			{
				GameLog.error("调用Sql语句   " + updateSql + "出错", ex);
			}
			finally
			{
				closeConn(conn, pstmt);
			}
			info.commitUpdate(guildId, result);
		}
	}

	public List<GuildInfo> getAllGuildInfo()
	{
		List<GuildInfo> guildInfoList = new ArrayList<GuildInfo>();
		Connection conn = getConn();
		if (conn == null)
		{
			return guildInfoList;
		}

		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try
		{
			pstmt = conn.prepareStatement(selectSql);
			rs = pstmt.executeQuery();
			while (rs.next())
			{
				GuildInfo info = new GuildInfo();
				info.setGuildId(rs.getLong("GuildId"));
				info.setGuildName(rs.getString("GuildName"));
				info.setGuildSlogan(rs.getString("GuildSlogan"));
				info.setGuildEmblem(rs.getShort("GuildEmblem"));
				info.setGuildLv(rs.getShort("GuildLv"));
				info.setTotalExp(rs.getInt("TotalExp"));
				info.setIsAudit(rs.getBoolean("IsAudit"));
				info.setCreateTime(rs.getInt("CreateTime"));
				info.setOp(DBOption.NONE);
				guildInfoList.add(info);
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

		return guildInfoList;
	}

	public boolean deleteGuild(long guildId)
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
			pstmt = conn.prepareStatement(deleteSql);
			pstmt.setLong(1, guildId);
			result = pstmt.executeUpdate() > -1;
		}
		catch (Exception ex)
		{
			GameLog.error("调用Sql语句   " + deleteSql + "出错", ex);
		}
		finally
		{
			closeConn(conn, pstmt);
		}

		return result;
	}
}
