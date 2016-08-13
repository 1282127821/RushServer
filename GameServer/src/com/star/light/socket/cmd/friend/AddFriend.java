package com.star.light.socket.cmd.friend;

import com.star.light.player.GamePlayer;
import com.star.light.socket.PBMessage;
import com.star.light.socket.cmd.NetCmd;

import tbgame.pbmessage.GamePBMsg.AddFriendMsg;

public class AddFriend implements NetCmd {
	public void execute(GamePlayer player, PBMessage packet) throws Exception {
		AddFriendMsg netMsg = AddFriendMsg.parseFrom(packet.getMsgBody());
		player.getFriendMgr().addFriend(netMsg.getUserId());
	}
}