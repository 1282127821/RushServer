package com.guild;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.db.DBOption;
import com.db.MainDBDao;
import com.util.GameLog;

public class GuildMemberDao extends MainDBDao {
	private static final String insertSql = "insert into tbl_guildmember (GuildId, UserId, GuildPower, Contribution) values(?, ?, ?, ?);";
	private static final String updateSql = "update tbl_guildmember set GuildPower = ?, Contribution = ? where GuildId = ? and UserId = ?;";
	private static final String selectSql = "select tp.UserId, tg.GuildId, UserName, PlayerLv, JobId, FightStrength, LogoutTime, VipLv, GuildPower, Contribution from tbl_playerinfo as tp "
			+ "inner join tbl_guildmember as tg on tp.UserId = tg.UserId;";
	private static final String deleteSql = "delete from tbl_guildmember where UserId = ?;";

	public void addGuildMemberInfo(long guildId, GuildMemberInfo info) {
		if (info != null && info.beginAdd()) {
			Connection conn = openConn();
			if (conn == null) {
				return;
			}
			
			boolean result = false;
			PreparedStatement pstmt = null;
			try {
				pstmt = conn.prepareStatement(insertSql);
				pstmt.setLong(1, guildId);
				pstmt.setLong(2, info.getUserId());
				pstmt.setInt(3, info.getPower());
				pstmt.setInt(4, info.getContribution());
				result = pstmt.executeUpdate() > -1;
			} catch (Exception ex) {
				GameLog.error("调用Sql语句   " + insertSql + "出错", ex);
			} finally {
				closeConn(conn, pstmt);
			}
			info.commitAdd(guildId, result);
		}
	}

	public void updateMemberInfo(long guildId, GuildMemberInfo info) {
		if (info != null && info.beginUpdate()) {
			Connection conn = openConn();
			if (conn == null) {
				return;
			}
			
			boolean result = false;
			PreparedStatement pstmt = null;
			try {
				pstmt = conn.prepareStatement(updateSql);
				pstmt.setInt(1, info.getPower());
				pstmt.setInt(2, info.getContribution());
				pstmt.setLong(3, guildId);
				pstmt.setLong(4, info.getUserId());
				result = pstmt.executeUpdate() > -1;
			} catch (Exception ex) {
				GameLog.error("调用Sql语句   " + updateSql + "出错", ex);
			} finally {
				closeConn(conn, pstmt);
			}
			info.commitUpdate(guildId, result);
		}
	}

	public Map<Long, List<GuildMemberInfo>> getAllGuildMember() {
		Map<Long, List<GuildMemberInfo>> guildMemMap = new HashMap<Long, List<GuildMemberInfo>>();
		Connection conn = openConn();
		if (conn == null) {
			return guildMemMap;
		}
		
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			pstmt = conn.prepareStatement(selectSql);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				long guildId = rs.getLong("GuildId");
				List<GuildMemberInfo> guildMemList = guildMemMap.get(guildId);
				if (guildMemList == null) {
					guildMemList = new ArrayList<GuildMemberInfo>();
					guildMemMap.put(guildId, guildMemList);
				}
				GuildMemberInfo info = new GuildMemberInfo();
				info.setUserId(rs.getLong("UserId"));
				info.setUserName(rs.getString("UserName"));
				info.setPlayerLv(rs.getInt("PlayerLv"));
				info.setJobId(rs.getInt("JobId"));
				info.setFightStrength(rs.getInt("FightStrength"));
				info.setLogoutTime(rs.getInt("LogoutTime"));
				info.setVipLv(rs.getShort("VipLv"));
				info.setPower(rs.getShort("GuildPower"));
				info.setContribution(rs.getInt("Contribution"));
				info.setOp(DBOption.NONE);
				guildMemList.add(info);
			}
		} catch (SQLException e) {
			GameLog.error("执行出错" + selectSql, e);
		} finally {
			closeConn(pstmt, rs);
		}

		return guildMemMap;
	}

	public boolean deleteGuildMember(long userId) {
		Connection conn = openConn();
		if (conn == null) {
			return false;
		}
			
		boolean result = false;
		PreparedStatement pstmt = null;
		try {
			pstmt = conn.prepareStatement(deleteSql);
			pstmt.setLong(1, userId);
			result = pstmt.executeUpdate() > -1;
		} catch (Exception ex) {
			GameLog.error("调用Sql语句   " + deleteSql + "出错", ex);
		} finally {
			closeConn(conn, pstmt);
		}
		
		return result;
	}
}
