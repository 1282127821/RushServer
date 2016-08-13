package com.netmsg.mail;

import com.netmsg.NetCmd;
import com.netmsg.PBMessage;
import com.pbmessage.GamePBMsg.MailCommonMsg;
import com.player.GamePlayer;

public class GetAllMailAttach implements NetCmd {
	public void execute(GamePlayer player, PBMessage packet) throws Exception {
		MailCommonMsg netMsg = MailCommonMsg.parseFrom(packet.getMsgBody());
		player.getMailMgr().getTotalMailAttach(netMsg.getMailType());
	}
}