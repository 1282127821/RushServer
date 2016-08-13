package com.star.light.game;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.star.light.util.GameLog;

public class PlayerInfoDao extends MainDBDao {
	private static final String insertSql = "insert into tbl_playerinfo(UserId, AccountId, UserName, JobId, CreateTime, PlayerLv) values(?, ?, ?, ?, ?, ?);";
	private static final String selectAllSql = "select * from tbl_playerinfo  where AccountId = ? and IsDelete = false";
	private static final String deletePlayerSql = "update tbl_playerinfo set IsDelete = true where UserId = ?;";
	
	public boolean addPlayerInfo(PlayerInfo info) {
		boolean result = false;
		if (info != null && info.beginAdd()) {
			Map<Integer, DbParameter> params = new HashMap<Integer, DbParameter>();
			long userId = info.getUserId();
			int index = 1;
			params.put(index++, new DbParameter(userId));
			params.put(index++, new DbParameter(info.getAccountId()));
			params.put(index++, new DbParameter(info.getUserName()));
			params.put(index++, new DbParameter(info.getJobId()));
			params.put(index++, new DbParameter(info.getCreateTime()));
			params.put(index++, new DbParameter(info.getPlayerLv()));
			result = execNoneQuery(insertSql, params);
			info.commitAdd(info.getUserId(), result);
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
//			info.setNoviceProcess(rs.getInt("NoviceProcess"));
			info.setPlayerLv(rs.getInt("PlayerLv"));
//			info.setFightStrength(rs.getInt("FightStrength"));
//			info.setExp(rs.getInt("PlayerExp"));
		} catch (Exception e) {
			info = null;
			GameLog.error("执行出错" + sql, e);
		}
		return info;
	}

	public List<PlayerInfo> getTotalPlayerInfo(long accoutId) {
		Map<Integer, DbParameter> params = new HashMap<Integer, DbParameter>();
		params.put(1, new DbParameter(accoutId));
		PreparedStatement pstmt = execQuery(selectAllSql, params);
		ResultSet rs = null;
		List<PlayerInfo> playerInfos = new ArrayList<PlayerInfo>();
		try {
			rs = pstmt.executeQuery();
			while (rs.next()) {
				playerInfos.add(getPlayerInfo(rs, selectAllSql));
			}
		} catch (SQLException e) {
			GameLog.error("执行出错" + selectAllSql, e);
		} finally {
			closeConn(pstmt, rs);
		}

		return playerInfos;
	}
	
	public boolean deletePlayer(long userId) {
		Map<Integer, DbParameter> params = new HashMap<Integer, DbParameter>();
		params.put(1, new DbParameter(userId));
		return execNoneQuery(deletePlayerSql, params);
	}
}
