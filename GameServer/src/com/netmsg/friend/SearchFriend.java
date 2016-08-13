package com.netmsg.friend;

import com.netmsg.NetCmd;
import com.netmsg.PBMessage;
import com.pbmessage.GamePBMsg.SearchFriendInfoMsg;
import com.player.GamePlayer;

public class SearchFriend implements NetCmd {
	public void execute(GamePlayer player, PBMessage packet) throws Exception {
		SearchFriendInfoMsg netMsg = SearchFriendInfoMsg.parseFrom(packet.getMsgBody());
		player.getFriendMgr().searchFriendByUserName(netMsg.getUserName());
	}
}