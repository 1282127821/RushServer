package com.game;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.db.MainDBDao;
import com.util.GameLog;

public class PlayerInfoDao extends MainDBDao {
	private static final String insertSql = "insert into tbl_playerinfo(UserId, AccountId, UserName, JobId, CreateTime, PlayerLv) values(?, ?, ?, ?, ?, ?);";
	private static final String selectAllSql = "select * from tbl_playerinfo  where AccountId = ? and IsDelete = false";
	private static final String deletePlayerSql = "update tbl_playerinfo set IsDelete = true where UserId = ?;";
	
	public boolean addPlayerInfo(PlayerInfo info) {
		Connection conn = openConn();
		if (conn == null) {
			return false;
		}
		
		boolean result = false;
		PreparedStatement pstmt = null;
		long userId = info.getUserId();
		try {
			pstmt = conn.prepareStatement(insertSql);
			pstmt.setLong(1, userId);
			pstmt.setLong(2, info.getAccountId());
			pstmt.setString(3, info.getUserName());
			pstmt.setInt(4, info.getJobId());
			pstmt.setInt(5, info.getCreateTime());
			pstmt.setInt(6, info.getPlayerLv());
			result = pstmt.executeUpdate() > -1;
		} catch (Exception ex) {
			GameLog.error("调用Sql语句   " + insertSql + "出错", ex);
		} finally {
			closeConn(conn, pstmt);
		}
		return result;
	}
	
	private PlayerInfo getPlayerInfo(ResultSet rs, String sql) {
		PlayerInfo info;
		try {
			info = new PlayerInfo();
			info.setUserId(rs.getLong("UserId"));
			info.setAccountId(rs.getLong("accountId"));
			info.setUserName(rs.getString("UserName"));
			info.setJobId(rs.getInt("JobId"));
			info.setPlayerLv(rs.getInt("PlayerLv"));
		} catch (Exception e) {
			info = null;
			GameLog.error("执行出错" + sql, e);
		}
		return info;
	}

	public List<PlayerInfo> getTotalPlayerInfo(long accoutId) {
		List<PlayerInfo> playerInfoList = new ArrayList<PlayerInfo>();
		Connection conn = openConn();
		if (conn == null) {
			return playerInfoList;
		}
		
		ResultSet rs = null;
		PreparedStatement pstmt = null;
		try {
			pstmt = conn.prepareStatement(selectAllSql);
			pstmt.setLong(1, accoutId);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				playerInfoList.add(getPlayerInfo(rs, selectAllSql));
			}
		} catch (SQLException e) {
			GameLog.error("执行出错" + selectAllSql, e);
		} finally {
			closeConn(pstmt, rs);
		}

		return playerInfoList;
	}
	
	public boolean deletePlayer(long userId) {
		Connection conn = openConn();
		if (conn == null) {
			return false;
		}
			
		boolean result = false;
		PreparedStatement pstmt = null;
		try {
			pstmt = conn.prepareStatement(deletePlayerSql);
			pstmt.setLong(1, userId);
			result = pstmt.executeUpdate() > -1;
		} catch (Exception ex) {
			GameLog.error("调用Sql语句   " + deletePlayerSql + "出错", ex);
		} finally {
			closeConn(conn, pstmt);
		}
		
		return result;
	}
}
