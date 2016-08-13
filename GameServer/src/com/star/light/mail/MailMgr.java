package com.star.light.mail;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.star.light.BaseServer;
import com.star.light.db.DBOption;
import com.star.light.player.DaoMgr;
import com.star.light.player.DiamondChangeType;
import com.star.light.player.GameConst;
import com.star.light.player.GamePlayer;
import com.star.light.player.ItemChangeType;
import com.star.light.prop.BagType;
import com.star.light.protocol.Protocol;
import com.star.light.table.ResourceInfo;
import com.star.light.util.GameLog;
import com.star.light.util.TimeUtil;

import tbgame.pbmessage.GamePBMsg.MailAttachInfoMsg;
import tbgame.pbmessage.GamePBMsg.MailCommonMsg;
import tbgame.pbmessage.GamePBMsg.MailInfoListMsg;
import tbgame.pbmessage.GamePBMsg.MailInfoMsg;
import tbgame.pbmessage.GamePBMsg.MailTotalAttackMsg;

public class MailMgr {
	private GamePlayer player;

	/**
	 * 所有邮件列表 包括公告、系统、活动
	 */
	private List<MailInfo> mailList;

	public MailMgr(GamePlayer player) {
		this.player = player;
		this.mailList = new ArrayList<MailInfo>();
	}

	/**
	 * 从数据库中加载邮件列表
	 */
	public void loadFromDB() {
		DaoMgr.mailDao.getMailInfo(player.getUserId(), mailList);
		if (mailList.size() <= 0) {
			int[] aryItemId = new int[] { 31009, 31010, 31011, 31012, 31012, 31012, 31012, 31011, 31009, 31010, };
			int[] aryItemCount = new int[] { 10, 10, 10, 10, 10, 1, 1, 1, 1, 1 };
			for (int i = 0; i < 5; i++) {
				List<ResourceInfo> mailAttach = new ArrayList<ResourceInfo>(2);
				for (int j = 0; j < 2; j++) {
					mailAttach.add(new ResourceInfo(aryItemId[i * 2 + j], aryItemCount[i * 2 + j]));
				}
				addMail(MailType.ANNOUNCE, "公告邮件", "公告邮件的道具奖励", mailAttach);
				addMail(MailType.SYSTEM, "系统邮件", "系统邮件的道具奖励", mailAttach);
				addMail(MailType.ACTIVITY, "活动邮件", "活动邮件的道具奖励", mailAttach);
			}
		}
	}

	/**
	 * 添加一封邮件到List
	 */
	public MailInfo addMail(short mailType, String mailTitle, String mailContent, List<ResourceInfo> mailAttach) {
		if (getMailCountByType(mailType) >= GameConst.MAX_MAIL_COUNT) {
			deleteLongTimeMail();
		}

		MailInfo mailInfo = new MailInfo();
		mailInfo.setMailId(BaseServer.IDWORK.nextId());
		mailInfo.setMailTitle(mailTitle);
		mailInfo.setMailContent(mailContent);
		mailInfo.setMailAttach(mailAttach);
		mailInfo.setMailType(mailType);
		mailInfo.setMailState(MailState.NOTGET);
		mailInfo.setSendTime(TimeUtil.getSysCurSeconds());
		mailInfo.setOp(DBOption.INSERT);
		mailList.add(mailInfo);
		return mailInfo;
	}

	/**
	 * 发送邮件列表给客户端
	 */
	public void sendTotalMail() {
		MailInfoListMsg.Builder netMsg = MailInfoListMsg.newBuilder();
		for (MailInfo mailInfo : mailList) {
			netMsg.addMailInfo(packOneMailInfo(mailInfo));
		}
		player.sendPacket(Protocol.S_C_TOTAL_MAIL_LIST, netMsg);
	}

	/**
	 * 打包一封邮件
	 */
	private MailInfoMsg.Builder packOneMailInfo(MailInfo mailInfo) {
		MailInfoMsg.Builder infoMsg = MailInfoMsg.newBuilder();
		infoMsg.setMailId(mailInfo.getMailId());
		infoMsg.setMailTitle(mailInfo.getMailTitle());
		infoMsg.setMailBody(mailInfo.getMailContent());
		List<ResourceInfo> mailAttachList = mailInfo.getMailAttach();
		for (ResourceInfo info : mailAttachList) {
			MailAttachInfoMsg.Builder attachMsg = MailAttachInfoMsg.newBuilder();
			attachMsg.setItemId(info.resourceId);
			attachMsg.setItemCount(info.count);
			infoMsg.addMailAttachList(attachMsg);
		}
		infoMsg.setSendTime(mailInfo.getSendTime());
		infoMsg.setMailState(mailInfo.getMailState());
		infoMsg.setMailType(mailInfo.getMailType());
		return infoMsg;
	}

	public int getMailCountByType(short mailType) {
		int mailCount = 0;
		for (MailInfo info : mailList) {
			if (info.getMailType() == mailType) {
				mailCount++;
			}
		}

		return mailCount;
	}

	/**
	 * 根据邮件Id获得一封邮件
	 */
	private MailInfo getMailInfoById(long mailId) {
		for (MailInfo info : mailList) {
			if (info.getMailId() == mailId) {
				return info;
			}
		}
		return null;
	}

	/**
	 * 获取所有邮件的附件
	 */
	public void getTotalMailAttach(int mailType) {
		MailTotalAttackMsg.Builder netMsg = MailTotalAttackMsg.newBuilder();
		Iterator<MailInfo> iter = mailList.iterator();
		while (iter.hasNext()) {
			MailInfo mailInfo = iter.next();
			if (mailType != mailInfo.getMailType()) {
				continue;
			}
			
			int result = getMailAttach(mailInfo);
			if (result == 2) {
				netMsg.addMailIdList(mailInfo.getMailId());
				iter.remove();
			} else if (result == 1){
				player.sendTips(1018);
				break;
			}
		}

		player.sendPacket(Protocol.S_C_GET_ALL_MAIL_ATTACH, netMsg);
	}

	private int getMailAttach(MailInfo mailInfo) {
		if (mailInfo != null && mailInfo.getMailState() == MailState.NOTGET && mailInfo.getMailAttach().size() > 0) {
			List<ResourceInfo> mailAttach = mailInfo.getMailAttach();
			int freeCount = player.getPropMgr().getFreeGridCount(BagType.PACKAGE);
			if (mailAttach.size() > freeCount) {
				return 1;
			}

			// TODO:LZGLZG这里应该先把邮件附件给清空掉，避免在删除邮件失败的时候导致邮件的奖励仍然存在。
			mailInfo.setMailState(MailState.DELETE);
			player.getPropMgr().addPropOrRes(mailAttach, ItemChangeType.MAIL_ATTACH, DiamondChangeType.MAIL_ATTACH);
			mailAttach.clear();
			if (DaoMgr.mailDao.deleteMailInfo(mailInfo.getMailId())) {
				return 2;
			}
		}

		return 0;
	}

	/**
	 * 获取某封邮件的附件
	 */
	public void getOneMailAttach(long mailId) {
		MailInfo mailInfo = getMailInfoById(mailId);
		int result = getMailAttach(mailInfo);
		if (result == 2) {
			MailCommonMsg.Builder netMsg = MailCommonMsg.newBuilder();
			netMsg.setMailId(mailId);
			mailList.remove(mailInfo);
			player.sendPacket(Protocol.S_C_GET_ONE_MAIL_ATTACH, netMsg);
		} else if (result == 1) {
			player.sendTips(1031);
		}
	}

	private void deleteLongTimeMail() {
		List<MailInfo> copyList = new ArrayList<MailInfo>(mailList);
		int time = Integer.MAX_VALUE;
		MailInfo delMailInfo = null;
		for (MailInfo mailInfo : copyList) {
			if (mailInfo.getSendTime() < time && mailInfo.getMailState() != MailState.DELETE) {
				delMailInfo = mailInfo;
				time = mailInfo.getSendTime();
			}
		}
		
		if (delMailInfo != null) {
			mailList.remove(delMailInfo);
			DaoMgr.mailDao.deleteMailInfo(delMailInfo.getMailId());
		}
	}
	
	/**
	 * 从内存中卸载玩家的邮件数据
	 */
	public void unloadData() {
		// 过期
		List<MailInfo> copyList = new ArrayList<MailInfo>(mailList);
		Iterator<MailInfo> iter = copyList.iterator();
		while (iter.hasNext()) {
			MailInfo mailInfo = iter.next();
			if (mailInfo.isOverdue()) {
				iter.remove();
				DaoMgr.mailDao.deleteMailInfo(mailInfo.getMailId());
			}
		}
	}

	/**
	 * 将邮件信息保存到数据库
	 */
	public void saveToDB() {
		long userId = 0;
		try {
			userId = player.getUserId();
			List<MailInfo> copy = new ArrayList<MailInfo>(mailList);
			// TODO LZGLZG保存邮件的时候需要处理已经过期的邮件,即需要删掉过期邮件
			for (MailInfo info : copy) {
				if (info.getOp() == DBOption.INSERT) {
					DaoMgr.mailDao.addMailInfo(userId, info);
				}

				if (info.getOp() == DBOption.UPDATE) {
					DaoMgr.mailDao.updateMailInfo(userId, info);
				}
			}
		} catch (Exception e) {
			GameLog.error("保存玩家邮件信息出错, UserId:  " + userId, e);
		}
	}
}
