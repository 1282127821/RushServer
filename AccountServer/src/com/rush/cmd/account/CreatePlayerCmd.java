package com.rush.cmd.account;

import com.rush.BaseServer;
import com.rush.game.DBOption;
import com.rush.game.DaoMgr;
import com.rush.game.JobType;
import com.rush.game.NetMsg;
import com.rush.game.PBMessage;
import com.rush.game.PlayerInfo;
import com.rush.util.GameLog;
import com.rush.util.TimeUtil;

import io.netty.channel.Channel;
import rush.pbmessage.GamePBMsg.PlayerInfoMsg;

public class CreatePlayerCmd implements NetMsg {
	public void execute(Channel channel, PBMessage packet) throws Exception {
		PlayerInfoMsg netMsg = PlayerInfoMsg.parseFrom(packet.getMsgBody());
		long accountId = 0;//netMsg.getAccountId();
		String userName = netMsg.getUserName();
		int jobType = netMsg.getJobType();

		// 表示昵称过长或者含有脏字符
		// if (!DirtyData.getInstance().isIllegal(userName,
		// GameConst.MAX_NAME_LENGTH)) {
		// return;
		// }

		if (jobType < JobType.MAN || jobType > JobType.LOLI) {
			return;
		}

		try {
			PlayerInfo playerInfo = new PlayerInfo();
			playerInfo.setUserId(BaseServer.IDWORK.nextId());
			playerInfo.setAccountId(accountId);
			playerInfo.setUserName(userName);
			playerInfo.setJobId(jobType);
			playerInfo.setCreateTime(TimeUtil.getSysCurSeconds());
			playerInfo.setPlayerLv(1);
			playerInfo.setOp(DBOption.Insert);
			boolean result = DaoMgr.playerInfoDao.addPlayerInfo(playerInfo);
			if (result) {
				
			}
			// 返回给客户端创建角色的结果
		} catch (Exception e) {
			GameLog.error("创建角色失败. AccountId: " + accountId + ", UserName:  " + userName + ", jobType:  " + jobType, e);
		}
	}
}
