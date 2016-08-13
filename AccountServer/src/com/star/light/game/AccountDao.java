package com.star.light.game;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import com.star.light.util.GameLog;
import com.star.light.util.TimeUtil;

public class AccountDao extends MainDBDao {
	private static final String insertSql = "insert into tbl_account(AccountId, AccountName, CreateTime, LoginIp, Imei, Model, Brand, GameId) value(?, ?, ?, ?, ?,?,1,?,?,?,?,?);";
	private static final String selectAccountNameSql = "select * from tbl_account where AccountName = ?;";
	private static final String forbidSql = "update tbl_account set LastLockoutDate = NOW(), ForbidReason = ?, IsForbid = ?, ForbidExpirtDate = ?, ForbidOperator = ? where AccountId = ?;";
	private static final String updateLoginSql = "update tbl_account set LastLoginDate = ?, LoginCount = ?, LoginIp = ?, DeleteCoolTime = ?,LastLogOutDate= ? where AccountId = ?;";

	public boolean addAccount(Account info) {
		Map<Integer, DbParameter> params = new HashMap<Integer, DbParameter>();
		params.put(1, new DbParameter(info.getAccountId()));
		params.put(2, new DbParameter(info.getAccountName()));
		params.put(3, new DbParameter(TimeUtil.getSysteCurTime()));
		params.put(4, new DbParameter(info.getLoginIP()));
		params.put(6, new DbParameter(TimeUtil.getSysteCurTime()));
		params.put(7, new DbParameter(info.getImei()));
		params.put(8, new DbParameter(info.getModel()));
		params.put(9, new DbParameter(info.getBrand()));
		params.put(10, new DbParameter(info.getGameId()));
		return execNoneQuery(insertSql, params);
	}

	public Account getAccount(String accountName) {
		Map<Integer, DbParameter> params = new HashMap<Integer, DbParameter>();
		params.put(1, new DbParameter(accountName));
		PreparedStatement pstmt = execQuery(selectAccountNameSql, params);
		return getAccount(pstmt);
	}

	private Account getAccount(PreparedStatement pstmt) {
		ResultSet rs = null;
		Account info = null;
		if (pstmt != null) {
			try {
				rs = pstmt.executeQuery();
				if (rs.last()) {
					info = new Account();
					info.setAccountId(rs.getLong("AccountId"));
					info.setAccountName(rs.getString("AccountName"));
					info.setCreateTime(rs.getInt("CreateTime"));
					info.setLoginTime(rs.getInt("LoginTime"));
					info.setLogoutTime(rs.getInt("LogoutTime"));
					info.setLoginCount(rs.getInt("LoginCount"));
					info.setLoginIP(rs.getString("LoginIp"));
					info.setForbidReason(rs.getString("ForbidReason"));
					info.setForbidExpireTime(rs.getInt("ForbidExpirtTime"));
					info.setModel(rs.getString("Model"));
					info.setBrand(rs.getString("Brand"));
					info.setGameId(rs.getInt("GameId"));
					info.setDelCDTime(rs.getInt("DelCDTime"));
				}
			} catch (SQLException e) {
				info = null;
				GameLog.error("执行出错", e);
			} finally {
				closeConn(pstmt, rs);
			}
		}
		return info;
	}

	public boolean updateLoginAccount(Account account) {
//		if (!TimeUtil.dateCompare(account.getLastLoginDate())) {
//			account.setLoginTime(TimeUtil.getSysCurSeconds());
//			account.setLoginCount(account.getLoginCount() + 1);
//		}
		Map<Integer, DbParameter> params = new HashMap<Integer, DbParameter>();
		params.put(1, new DbParameter(account.getLoginTime()));
		params.put(2, new DbParameter(account.getLoginCount()));
		params.put(3, new DbParameter(account.getLoginIP()));
		params.put(4, new DbParameter(account.getDelCDTime()));
		params.put(5, new DbParameter(account.getLogoutTime()));
		params.put(6, new DbParameter(account.getAccountId()));
		return execNoneQuery(updateLoginSql, params);
	}

	public boolean forbidAccount(long accountId, int forbidExpirtTime, String forbidReason, String forbidOperator) {
		Map<Integer, DbParameter> params = new HashMap<Integer, DbParameter>();
		params.put(1, new DbParameter(forbidExpirtTime));
		params.put(2, new DbParameter(forbidReason));
		params.put(3, new DbParameter(forbidOperator));
		params.put(4, new DbParameter(accountId));
		return execNoneQuery(forbidSql, params);
	}
}
