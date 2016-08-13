package com.star.light.socket.cmd.mail;

import com.star.light.player.GamePlayer;
import com.star.light.socket.PBMessage;
import com.star.light.socket.cmd.NetCmd;

import tbgame.pbmessage.GamePBMsg.MailCommonMsg;

public class GetOneMailAttach implements NetCmd {
	public void execute(GamePlayer player, PBMessage packet) throws Exception {
		MailCommonMsg mailMsg = MailCommonMsg.parseFrom(packet.getMsgBody());
		player.getMailMgr().getOneMailAttach(mailMsg.getMailId());
	}
}
