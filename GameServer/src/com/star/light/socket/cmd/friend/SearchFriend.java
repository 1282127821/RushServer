package com.star.light.socket.cmd.friend;

import com.star.light.player.GamePlayer;
import com.star.light.socket.PBMessage;
import com.star.light.socket.cmd.NetCmd;

import tbgame.pbmessage.GamePBMsg.SearchFriendInfoMsg;

public class SearchFriend implements NetCmd {
	public void execute(GamePlayer player, PBMessage packet) throws Exception {
		SearchFriendInfoMsg netMsg = SearchFriendInfoMsg.parseFrom(packet.getMsgBody());
		player.getFriendMgr().searchFriendByUserName(netMsg.getUserName());
	}
}