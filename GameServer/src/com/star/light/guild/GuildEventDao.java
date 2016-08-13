package com.star.light.guild;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.star.light.db.DBOption;
import com.star.light.db.MainDBDao;
import com.star.light.util.GameLog;
import com.star.light.util.TimeUtil;

public class GuildEventDao extends MainDBDao {
	private static final String insertEventSql = "insert into tbl_guildevent (GuildId, EventDesc, EventTime) values(?, ?, ?);";
	private static final String selectEventSql = "select GuildId, EventDesc, EventTime from tbl_guildevent;";
	private static final String deleteEventSql = "delete from tbl_guildevent where GuildId = ? and EventTime <= ?;";
	private static final String deleteAllEventSql = "delete from tbl_guildevent where GuildId = ?;";
	private static final String deleteEventByTimeSql = "delete from tbl_guildevent where EventTime <= ?;";

	public void addGuildEventInfo(long guildId, GuildEventInfo eventInfo) {
		if (eventInfo != null && eventInfo.beginAdd()) {
			Connection conn = openConn();
			if (conn == null) {
				return;
			}
			
			boolean result = false;
			PreparedStatement pstmt = null;
			try {
				pstmt = conn.prepareStatement(insertEventSql);
				pstmt.setLong(1, guildId);
				pstmt.setString(2, eventInfo.getEventDesc());
				pstmt.setInt(3, eventInfo.getEventTime());
				result = pstmt.executeUpdate() > -1;
			} catch (Exception ex) {
				GameLog.error("调用Sql语句   " + insertEventSql + "出错", ex);
			} finally {
				closeConn(conn, pstmt);
			}
			
			eventInfo.commitAdd(guildId, result);
		}
	}
	
	private void deleteOverTimeGuildEvent() {
		Connection conn = openConn();
		if (conn == null) {
			return;
		}
			
		PreparedStatement pstmt = null;
		try {
			pstmt = conn.prepareStatement(deleteEventByTimeSql);
			pstmt.setInt(1, TimeUtil.getSysCurSeconds() - 5 * TimeUtil.SECONDOFPERDAY);
			pstmt.executeUpdate();
		} catch (Exception ex) {
			GameLog.error("调用Sql语句   " + deleteEventByTimeSql + "出错", ex);
		} finally {
			closeConn(conn, pstmt);
		}
	}
	
	public Map<Long, List<GuildEventInfo>> getAllGuildEvent() {
		deleteOverTimeGuildEvent();
		
		Map<Long, List<GuildEventInfo>> guildEventMap = new HashMap<Long, List<GuildEventInfo>>();
		Connection conn = openConn();
		if (conn == null) {
			return guildEventMap;
		}
		
		ResultSet rs = null;
		PreparedStatement pstmt = null;
		try {
			pstmt = conn.prepareStatement(selectEventSql);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				long guildId = rs.getLong("GuildId");
				List<GuildEventInfo> guildEventList = guildEventMap.get(guildId);
				if (guildEventList == null) {
					guildEventList = new ArrayList<GuildEventInfo>();
					guildEventMap.put(guildId, guildEventList);
				}
				GuildEventInfo info = new GuildEventInfo();
				info.setEventDesc(rs.getString("EventDesc"));
				info.setEventTime(rs.getInt("EventTime"));
				info.setOp(DBOption.NONE);
				guildEventList.add(info);
			}
		} catch (SQLException e) {
			GameLog.error("执行出错" + selectEventSql, e);
		} finally {
			closeConn(pstmt, rs);
		}
		
		return guildEventMap;
	}

	public boolean deleteGuildEventInfo(long guildId, int eventTime) {
		Connection conn = openConn();
		if (conn == null) {
			return false;
		}
			
		boolean result = false;
		PreparedStatement pstmt = null;
		try {
			pstmt = conn.prepareStatement(deleteEventSql);
			pstmt.setLong(1, guildId);
			pstmt.setInt(2, eventTime);
			result = pstmt.executeUpdate() > -1;
		} catch (Exception ex) {
			GameLog.error("调用Sql语句   " + deleteEventSql + "出错", ex);
		} finally {
			closeConn(conn, pstmt);
		}
		
		return result;
	}
	
	public boolean deleteAllGuildEvent(long guildId) {
		Connection conn = openConn();
		if (conn == null) {
			return false;
		}
			
		boolean result = false;
		PreparedStatement pstmt = null;
		try {
			pstmt = conn.prepareStatement(deleteAllEventSql);
			pstmt.setLong(1, guildId);
			result = pstmt.executeUpdate() > -1;
		} catch (Exception ex) {
			GameLog.error("调用Sql语句   " + deleteAllEventSql + "出错", ex);
		} finally {
			closeConn(conn, pstmt);
		}
		
		return result;
	}
}
