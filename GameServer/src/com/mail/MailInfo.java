package com.mail;

import java.util.ArrayList;
import java.util.List;

import com.db.DBObject;
import com.db.DBOption;
import com.table.ResourceInfo;
import com.util.TimeUtil;

public class MailInfo extends DBObject {
	/**
	 * 邮件Id
	 */
	private long mailId;

	/**
	 * 邮件的标题，标题不超过十个汉字
	 */
	private String mailTitle;

	/**
	 * 邮件的内容，内容不超过128个汉字
	 */
	private String mailContent;
	
	/**
	 * 邮件类型
	 */
	private int mailType;

	/**
	 * 邮件状态 1未读 2已读 3已领取
	 */
	private int mailState;
	
	/**
	 * 发送邮件的时间
	 */
	private int sendTime;

	/**
	 * 邮件附件
	 */
	private List<ResourceInfo> mailAttach;

	/**
	 * 是否超过七天
	 */
	public boolean isOverdue() {
		return TimeUtil.getSysCurSeconds() - sendTime > 86400 * 7;
	}

	public List<ResourceInfo> getMailAttach() {
		return mailAttach;
	}
	
	public void setMailAttach(List<ResourceInfo> mailAttach) {
		this.mailAttach = mailAttach;
		setOp(DBOption.UPDATE);
	}
	
	public void setStrMailAttach(String attachInfo) {
		if (!"".equals(attachInfo)) {
			String[] aryMailAttach = attachInfo.split(",");
			mailAttach = new ArrayList<ResourceInfo>(aryMailAttach.length);
			for (int index = 0; index < aryMailAttach.length; ++index) {
				String[] aryProp = aryMailAttach[index].split(":");
				mailAttach.add(new ResourceInfo(Integer.parseInt(aryProp[0]), Integer.parseInt(aryProp[1])));
			}
		}
	}

	public String getStrMailAttach() {
		StringBuilder attachInfo = new StringBuilder();
		if (mailAttach != null) {
			for (ResourceInfo info : mailAttach) {
				attachInfo.append(info.resourceId).append(":").append(info.count).append(",");
			}
		}

		return attachInfo.toString();
	}

	public int getMailState() {
		return mailState;
	}

	public void setMailState(int mailState) {
		if (this.mailState != mailState) {
			this.mailState = mailState;
			setOp(DBOption.UPDATE);
		}
	}

	public long getMailId() {
		return mailId;
	}

	public void setMailId(long mailId) {
		this.mailId = mailId;
	}

	public int getSendTime() {
		return sendTime;
	}

	public void setSendTime(int sendTime) {
		this.sendTime = sendTime;
	}

	public String getMailTitle() {
		return mailTitle;
	}

	public void setMailTitle(String mailTitle) {
		this.mailTitle = mailTitle;
	}

	public String getMailContent() {
		return mailContent;
	}

	public void setMailContent(String mailContent) {
		this.mailContent = mailContent;
	}

	public int getMailType() {
		return mailType;
	}

	public void setMailType(int mailType) {
		this.mailType = mailType;
	}

	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append("mailId:		").append(mailId).append("\n");
		sb.append("mailTitle:		").append(mailTitle).append("\n");
		sb.append("mailContent:		").append(mailContent).append("\n");
		sb.append("mailAttach:		").append(getStrMailAttach()).append("\n");
		sb.append("mailState:		").append(mailState).append("\n");
		sb.append("mailType:		").append(mailType).append("\n");
		sb.append("sendTime:		").append(sendTime).append("\n");
		return sb.toString();
	}
}
