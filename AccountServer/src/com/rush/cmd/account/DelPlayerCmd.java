package com.rush.cmd.account;

import com.rush.game.DaoMgr;
import com.rush.game.NetMsg;
import com.rush.game.PBMessage;

import io.netty.channel.Channel;
import rush.pbmessage.GamePBMsg.PlayerInfoMsg;

public class DelPlayerCmd implements NetMsg {
	public void execute(Channel channel, PBMessage packet) throws Exception {
		PlayerInfoMsg netMsg = PlayerInfoMsg.parseFrom(packet.getMsgBody());
		//TODO:LZGLZG判断是否有删除的冷却时间
		boolean isSuccess = DaoMgr.playerInfoDao.deletePlayer(netMsg.getUserId());
		if (isSuccess) {
			
		}
	}
}
