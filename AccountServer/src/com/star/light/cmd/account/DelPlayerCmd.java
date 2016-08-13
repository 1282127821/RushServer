package com.star.light.cmd.account;

import com.star.light.game.DaoMgr;
import com.star.light.game.NetMsg;
import com.star.light.game.PBMessage;

import io.netty.channel.Channel;
import tbgame.pbmessage.GamePBMsg.PlayerInfoMsg;

public class DelPlayerCmd implements NetMsg {
	public void execute(Channel channel, PBMessage packet) throws Exception {
		PlayerInfoMsg netMsg = PlayerInfoMsg.parseFrom(packet.getMsgBody());
		//TODO:LZGLZG判断是否有删除的冷却时间
		boolean isSuccess = DaoMgr.playerInfoDao.deletePlayer(netMsg.getUserId());
		if (isSuccess) {
			
		}
	}
}
