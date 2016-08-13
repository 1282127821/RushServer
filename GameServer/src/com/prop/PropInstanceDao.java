package com.prop;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.db.DBOption;
import com.db.MainDBDao;
import com.util.GameLog;

public class PropInstanceDao extends MainDBDao {
	private static final String insertSql = "insert into tbl_iteminfo(Id, UserId, TemplateId, PosIndex, StackCount, BagType, GainTime, BlessLv, BlessAttribute, InlayCard, "
			+ "AttributeInfo) " + "values(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);";
	private static final String updateSql = "update tbl_iteminfo set TemplateId = ?, PosIndex = ?, StackCount = ?, BagType = ?, GainTime = ?, BlessLv = ?, BlessAttribute = ?, "
			+ "InlayCard = ?, AttributeInfo = ? where Id = ?;";
	private static final String selectSql = "select Id, TemplateId, PosIndex, StackCount, BagType, GainTime, BlessLv, BlessAttribute, InlayCard,"
			+ "AttributeInfo from tbl_iteminfo where UserId = ?;";
	private static final String deleteSql = "delete from tbl_iteminfo where Id = ?;";
	private static final String selectSqlByEquip = "select Id, TemplateId, PosIndex, StackCount, BagType, GainTime, BlessLv, BlessAttribute, InlayCard,"
			+ "AttributeInfo from tbl_iteminfo where UserId = ? and BagType = ?;";

	public void addPropInstance(long userId, PropInstance info) {
		if (info != null && info.beginAdd()) {
			Connection conn = openConn();
			if (conn == null) {
				return;
			}
			
			boolean result = false;
			PreparedStatement pstmt = null;
			try {
				pstmt = conn.prepareStatement(insertSql);
				pstmt.setLong(1, info.getId());
				pstmt.setLong(2, userId);
				pstmt.setInt(3, info.getTemplateId());
				pstmt.setInt(4, info.getPosIndex());
				pstmt.setInt(5, info.getStackCount());
				pstmt.setInt(6, info.getBagType());
				pstmt.setInt(7, info.getGainTime());
				pstmt.setInt(8, info.getBlessLv());
				pstmt.setInt(9, info.getBlessAttribute());
				pstmt.setString(10, info.getStrInlay());
				pstmt.setString(11, info.getStrAttribute());
				result = pstmt.executeUpdate() > -1;
			} catch (Exception ex) {
				GameLog.error("PropInstanceDao addPropInstance 调用Sql语句   " + insertSql + "出错", ex);
			} finally {
				closeConn(conn, pstmt);
			}
			info.commitAdd(userId, result);
		}
	}

	public void updateItemInfo(long userId, PropInstance info) {
		if (info != null && info.beginUpdate()) {
			Connection conn = openConn();
			if (conn == null) {
				return;
			}
			
			boolean result = false;
			PreparedStatement pstmt = null;
			try {
				pstmt = conn.prepareStatement(updateSql);
				pstmt.setInt(1, info.getTemplateId());
				pstmt.setInt(2, info.getPosIndex());
				pstmt.setInt(3, info.getStackCount());
				pstmt.setInt(4, info.getBagType());
				pstmt.setInt(5, info.getGainTime());
				pstmt.setInt(6, info.getBlessLv());
				pstmt.setInt(7, info.getBlessAttribute());
				pstmt.setString(8, info.getStrInlay());
				pstmt.setString(9, info.getStrAttribute());
				pstmt.setLong(10, info.getId());
				result = pstmt.executeUpdate() > -1;
			} catch (Exception ex) {
				GameLog.error("调用Sql语句   " + updateSql + "出错", ex);
			} finally {
				closeConn(conn, pstmt);
			}
			info.commitUpdate(userId, result);
		}
	}

	public boolean deleteItemInfo(long propId) {
		Connection conn = openConn();
		if (conn == null) {
			return false;
		}
			
		boolean result = false;
		PreparedStatement pstmt = null;
		try {
			pstmt = conn.prepareStatement(deleteSql);
			pstmt.setLong(1, propId);
			result = pstmt.executeUpdate() > -1;
		} catch (Exception ex) {
			GameLog.error("调用Sql语句   " + deleteSql + "出错", ex);
		} finally {
			closeConn(conn, pstmt);
		}
		
		return result;
	}

	public List<PropInstance> getAllItemInfo(long userId) {
		List<PropInstance> propList = new ArrayList<PropInstance>();
		Connection conn = openConn();
		if (conn == null) {
			return propList;
		}
		
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			pstmt = conn.prepareStatement(selectSql);
			pstmt.setLong(1, userId);
			rs = pstmt.executeQuery();
			propList = new ArrayList<PropInstance>();
			while (rs.next()) {
				PropInstance info = new PropInstance();
				info.setId(rs.getLong("Id"));
				info.setTemplateId(rs.getInt("TemplateId"));
				info.setPosIndex(rs.getShort("PosIndex"));
				info.setStackCount(rs.getInt("StackCount"));
				info.setBagType(rs.getShort("BagType"));
				info.setGainTime(rs.getInt("GainTime"));
				info.createEquipInstance();
				info.setBlessLv(rs.getShort("BlessLv"));
				info.setBlessAttribute(rs.getInt("BlessAttribute"));
				info.setStrInlay((rs.getString("InlayCard")));
				info.setStrAttribute(rs.getString("AttributeInfo"));
				info.setOp(DBOption.NONE);
				propList.add(info);
			}
		} catch (SQLException e) {
			propList = null;
			GameLog.error("执行出错" + selectSql, e);
		} finally {
			closeConn(pstmt, rs);
		}
		
		return propList;
	}

	public List<PropInstance> getTotalEquipInfo(long userId) {
		List<PropInstance> equipList = new ArrayList<PropInstance>();
		Connection conn = openConn();
		if (conn == null) {
			return equipList;
		}
		
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			pstmt = conn.prepareStatement(selectSqlByEquip);
			pstmt.setLong(1, userId);
			pstmt.setInt(1, BagType.EQUIP_FENCE);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				PropInstance info = new PropInstance();
				info.createEquipInstance();
				info.setId(rs.getLong("Id"));
				info.setTemplateId(rs.getInt("TemplateId"));
				info.setPosIndex(rs.getShort("PosIndex"));
				info.setStackCount(rs.getInt("StackCount"));
				info.setBagType(rs.getShort("BagType"));
				info.setGainTime(rs.getInt("GainTime"));
				info.setBlessLv(rs.getShort("BlessLv"));
				info.setBlessAttribute(rs.getInt("BlessAttribute"));
				info.setStrInlay((rs.getString("InlayCard")));
				info.setStrAttribute(rs.getString("AttributeInfo"));
				info.setOp(DBOption.NONE);
				equipList.add(info);
			}
		} catch (SQLException e) {
			GameLog.error("执行出错" + selectSqlByEquip, e);
		} finally {
			closeConn(pstmt, rs);
		}
		return equipList;
	}
}