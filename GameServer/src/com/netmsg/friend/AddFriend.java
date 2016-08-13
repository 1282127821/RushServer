package com.netmsg.friend;

import com.netmsg.NetCmd;
import com.netmsg.PBMessage;
import com.pbmessage.GamePBMsg.AddFriendMsg;
import com.player.GamePlayer;

public class AddFriend implements NetCmd {
	public void execute(GamePlayer player, PBMessage packet) throws Exception {
		AddFriendMsg netMsg = AddFriendMsg.parseFrom(packet.getMsgBody());
		player.getFriendMgr().addFriend(netMsg.getUserId());
	}
}