package com.netmsg.friend;

import com.netmsg.NetCmd;
import com.netmsg.PBMessage;
import com.pbmessage.GamePBMsg.DeleteFriendMsg;
import com.player.GamePlayer;
import com.protocol.Protocol;

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