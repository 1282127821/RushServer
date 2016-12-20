package com.mail;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import com.db.DBDao;
import com.db.DBOption;
import com.util.GameLog;

public class MailDao extends DBDao
{
	private static final String insertSql = "insert into tbl_mail (MailId, UserId, MailTitle, MailContent, MailAttach, MailState, MailType, SendTime) values (?, ?, ?, ?, ?, ?, ?, ?);";
	private static final String updateSql = "update tbl_mail set  MailState = ? where MailId = ?;";
	private static final String selectSql = "select MailId, MailTitle, MailContent, MailAttach, MailState, MailType, SendTime from tbl_mail where UserId = ?;";
	private static final String deleteSql = "delete from tbl_mail where MailId = ?;";

	public void addMailInfo(long userId, MailInfo info)
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
				pstmt.setLong(1, info.getMailId());
				pstmt.setLong(2, userId);
				pstmt.setString(3, info.getMailTitle());
				pstmt.setString(4, info.getMailContent());
				pstmt.setString(5, info.getStrMailAttach());
				pstmt.setInt(6, info.getMailState());
				pstmt.setInt(7, info.getMailType());
				pstmt.setInt(8, info.getSendTime());
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
			info.commitAdd(userId, result);
		}
	}

	public void updateMailInfo(long userId, MailInfo info)
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
				pstmt.setInt(1, info.getMailState());
				pstmt.setLong(2, info.getMailId());
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
			info.commitUpdate(userId, result);
		}
	}

	public void getMailInfo(long userId, List<MailInfo> mailList)
	{
		Connection conn = getConn();
		if (conn == null)
		{
			return;
		}

		ResultSet rs = null;
		PreparedStatement pstmt = null;
		try
		{
			pstmt = conn.prepareStatement(selectSql);
			pstmt.setLong(1, userId);
			rs = pstmt.executeQuery();
			MailInfo info;
			while (rs.next())
			{
				info = new MailInfo();
				info.setMailId(rs.getLong("MailId"));
				info.setMailTitle(rs.getString("MailTitle"));
				info.setMailContent(rs.getString("MailContent"));
				info.setStrMailAttach(rs.getString("MailAttach"));
				info.setMailState(rs.getShort("MailState"));
				info.setMailType(rs.getShort("MailType"));
				info.setSendTime(rs.getInt("SendTime"));
				info.setOp(DBOption.NONE);
				mailList.add(info);
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
	}

	public boolean deleteMailInfo(long mailId)
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
			pstmt.setLong(1, mailId);
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