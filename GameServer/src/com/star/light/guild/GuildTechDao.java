package com.star.light.guild;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import com.star.light.db.MainDBDao;
import com.star.light.util.GameLog;

public class GuildTechDao extends MainDBDao {
	private static final String insertSql = "insert into tbl_guildtech (UserId, TechId, TechLv, Contribution) values(?, ?, ?, ?);";
	private static final String updateSql = "update tbl_guildtech set TechLv = ?, Contribution = ? where UserId = ? and TechId = ?;";
	private static final String selectSql = "select TechId, TechLv, Contribution from tbl_guildtech where UserId = ?;";

	public void addGuildTeachInfo(long userId, GuildTechInfo info) {
		if (info != null && info.beginAdd()) {
			Connection conn = openConn();
			if (conn == null) {
				return;
			}
			
			boolean result = false;
			PreparedStatement pstmt = null;
			try {
				pstmt = conn.prepareStatement(insertSql);
				pstmt.setLong(1, userId);
				pstmt.setInt(2, info.getTechId());
				pstmt.setLong(3, info.getTechLv());
				pstmt.setLong(4, info.getContribution());
				result = pstmt.executeUpdate() > -1;
			} catch (Exception ex) {
				GameLog.error("调用Sql语句   " + insertSql + "出错", ex);
			} finally {
				closeConn(conn, pstmt);
			}
			info.commitAdd(userId, result);
		}
	}
	
	public void updateGuildTeachInfo(long userId, GuildTechInfo info) {
		if (info != null && info.beginUpdate()) {
			Connection conn = openConn();
			if (conn == null) {
				return;
			}
			
			boolean result = false;
			PreparedStatement pstmt = null;
			try {
				pstmt = conn.prepareStatement(updateSql);
				pstmt.setInt(1, info.getTechLv());
				pstmt.setInt(2, info.getContribution());
				pstmt.setLong(3, userId);
				pstmt.setInt(4, info.getTechId());
				result = pstmt.executeUpdate() > -1;
			} catch (Exception ex) {
				GameLog.error("调用Sql语句   " + updateSql + "出错", ex);
			} finally {
				closeConn(conn, pstmt);
			}
			info.commitUpdate(userId, result);
		}
	}
	
	public void getGuildTeach(long userId, List<GuildTechInfo> guildTechList) {
		Connection conn = openConn();
		if (conn == null) {
			return;
		}
		
		ResultSet rs = null;
		PreparedStatement pstmt = null;
		try {
			pstmt = conn.prepareStatement(selectSql);
			pstmt.setLong(1, userId);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				GuildTechInfo info = new GuildTechInfo();
				info.setTechId(rs.getInt("TechId"));
				info.setTechLv(rs.getShort("TechLv"));
				info.setContribution(rs.getInt("Contribution"));
				guildTechList.add(info);
			}
		} catch (SQLException e) {
			GameLog.error("执行出错" + selectSql, e);
		} finally {
			closeConn(pstmt, rs);
		}
	}
}