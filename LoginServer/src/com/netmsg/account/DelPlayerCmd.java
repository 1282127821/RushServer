package com.netmsg.account;

import com.game.DaoMgr;
import com.game.NetMsg;
import com.game.PBMessage;
import com.pbmessage.GamePBMsg.PlayerInfoMsg;

import io.netty.channel.ChannelHandlerContext;

public class DelPlayerCmd implements NetMsg
{
	public void execute(ChannelHandlerContext ctx, PBMessage packet) throws Exception
	{
		PlayerInfoMsg netMsg = PlayerInfoMsg.parseFrom(packet.getMsgBody());
		// TODO:LZGLZG判断是否有删除的冷却时间
		boolean isSuccess = DaoMgr.playerInfoDao.deletePlayer(netMsg.getUserId());
		if (isSuccess)
		{

		}
	}
}
