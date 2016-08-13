package com.star.light.socket.cmd.friend;

import com.star.light.player.GamePlayer;
import com.star.light.protocol.Protocol;
import com.star.light.socket.PBMessage;
import com.star.light.socket.cmd.NetCmd;

import tbgame.pbmessage.GamePBMsg.DeleteFriendMsg;

public class DeleteFriend implements NetCmd {
	public void execute(GamePlayer player, PBMessage packet) throws Exception {
		DeleteFriendMsg netMsg = DeleteFriendMsg.parseFrom(packet.getMsgBody());
		int operType = netMsg.getOperType();
		long userId = netMsg.getUserId();
		if (operType == 1) { // 删除好友
			player.getFriendMgr().deleteFriend(userId);
		} else if (operType == 2) { // 删除仇敌
			player.getFriendMgr().deleteEnemy(userId);
		} else if (operType == 3) { // 删除战友
			player.getFriendMgr().deleteBattleFriend(userId);
		}

		packet.setCodeId(Protocol.S_C_DEL_FRIEND);
		player.sendPacket(packet);
	}
}