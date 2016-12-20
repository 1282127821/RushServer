package com.skill;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import com.db.DBDao;
import com.db.DBOption;
import com.util.GameLog;

public class FightSkillInfoDao extends DBDao
{
	private static final String insertSkillSql = "insert into tbl_fightskill(SkillDBId, UserId, JobType, SkillId, SkillLv, IsActiveSkill) values(?, ?, ?, ?, ?, ?);";
	private static final String updateSkillSql = "update tbl_fightskill set SkillId = ?, SkillLv = ?, IsActiveSkill = ? where SkillDBId = ?;";
	private static final String selectSkillSql = "select SkillDBId, SkillId, SkillLv, IsActiveSkill from tbl_fightskill where UserId = ? and JobType = ?";

	private static final String insertSkillChainSql = "insert into tbl_skillchain (UserId, JobType, FightSkillChain) values(?, ?, ?);";
	private static final String updateSkillChainSql = "update tbl_skillchain set FightSkillChain = ? where UserId = ? and JobType = ?;";
	private static final String selectSkillChainSql = "select UserId, JobType, FightSkillChain from tbl_skillchain where UserId = ? and JobType = ?;";

	public void addFightSkillInfo(long userId, int jobType, FightSkillUnit info)
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
				pstmt = conn.prepareStatement(insertSkillSql);
				pstmt.setLong(1, info.getSkillDBId());
				pstmt.setLong(2, userId);
				pstmt.setInt(3, jobType);
				pstmt.setInt(4, info.getSkillId());
				pstmt.setInt(5, info.getSkillLv());
				pstmt.setBoolean(6, info.isActiveSkill());
				result = pstmt.executeUpdate() > -1;
			}
			catch (Exception ex)
			{
				GameLog.error("调用Sql语句   " + insertSkillSql + "出错", ex);
			}
			finally
			{
				closeConn(conn, pstmt);
			}
			info.commitAdd(userId, result);
		}
	}

	public void updateFightSkillInfo(long userId, FightSkillUnit info)
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
				pstmt = conn.prepareStatement(updateSkillSql);
				pstmt.setInt(1, info.getSkillId());
				pstmt.setInt(2, info.getSkillLv());
				pstmt.setBoolean(2, info.isActiveSkill());
				pstmt.setLong(2, info.getSkillDBId());
				result = pstmt.executeUpdate() > -1;
			}
			catch (Exception ex)
			{
				GameLog.error("调用Sql语句   " + updateSkillSql + "出错", ex);
			}
			finally
			{
				closeConn(conn, pstmt);
			}
			info.commitUpdate(userId, result);
		}
	}

	public void getFightSkillInfo(long userId, int jobType, List<FightSkillUnit> skillList)
	{
		Connection conn = getConn();
		if (conn == null)
		{
			return;
		}

		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try
		{
			pstmt = conn.prepareStatement(selectSkillSql);
			pstmt.setLong(1, userId);
			pstmt.setInt(2, jobType);
			rs = pstmt.executeQuery();
			while (rs.next())
			{
				FightSkillUnit info = new FightSkillUnit();
				info.setSkillDBId(rs.getLong("SkillDBId"));
				info.setSkillId(rs.getInt("SkillId"));
				info.setSkillLv(rs.getInt("SkillLv"));
				info.setActiveSkill(rs.getBoolean("IsActiveSkill"));
				info.setOp(DBOption.NONE);
				skillList.add(info);
			}
		}
		catch (SQLException e)
		{
			GameLog.error("执行出错" + selectSkillSql, e);
		}
		finally
		{
			closeConn(pstmt, rs);
		}
	}

	/**
	 * 插入玩家的技能链信息
	 */
	public void insertSkillChain(long userId, int jobType, SkillChainInfo info)
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
				pstmt = conn.prepareStatement(insertSkillChainSql);
				pstmt.setLong(1, userId);
				pstmt.setInt(2, jobType);
				pstmt.setString(3, info.getAllSkillChain());
				result = pstmt.executeUpdate() > -1;
			}
			catch (Exception ex)
			{
				GameLog.error("调用Sql语句   " + insertSkillChainSql + "出错", ex);
			}
			finally
			{
				closeConn(conn, pstmt);
			}
			info.commitAdd(userId, result);
		}
	}

	public void updateSkillChain(long userId, int jobType, SkillChainInfo info)
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
				pstmt = conn.prepareStatement(updateSkillChainSql);
				pstmt.setString(1, info.getAllSkillChain());
				pstmt.setLong(2, userId);
				pstmt.setInt(3, jobType);
				result = pstmt.executeUpdate() > -1;
			}
			catch (Exception ex)
			{
				GameLog.error("调用Sql语句   " + updateSkillChainSql + "出错", ex);
			}
			finally
			{
				closeConn(conn, pstmt);
			}
			info.commitUpdate(userId, result);
		}
	}

	public SkillChainInfo getSkillChain(long userId, int jobType)
	{
		Connection conn = getConn();
		if (conn == null)
		{
			return null;
		}

		PreparedStatement pstmt = null;
		ResultSet rs = null;
		SkillChainInfo info = null;
		try
		{
			pstmt = conn.prepareStatement(selectSkillChainSql);
			pstmt.setLong(1, userId);
			pstmt.setInt(2, jobType);
			rs = pstmt.executeQuery();
			while (rs.next())
			{
				info = new SkillChainInfo();
				info.setAllSkillChain(rs.getString("FightSkillChain"));
				info.setOp(DBOption.NONE);
			}
		}
		catch (SQLException e)
		{
			info = null;
			GameLog.error("执行出错" + selectSkillChainSql, e);
		}
		finally
		{
			closeConn(pstmt, rs);
		}
		return info;
	}
}