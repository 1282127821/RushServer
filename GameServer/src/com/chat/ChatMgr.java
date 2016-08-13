package com.chat;

import com.GatewayLinkMgr;
import com.pbmessage.GamePBMsg.TipInfoMsg;
import com.protocol.Protocol;

public class ChatMgr {
	private static ChatMgr instance = new ChatMgr();
	
	public static ChatMgr getInstance() {
		return instance;
	}
	
	/**
	 * 全服广播所有的系统消息
	 */
	public void broadcastSystemMsg(int tipId, Object... args) {
		TipInfoMsg.Builder netMsg = TipInfoMsg.newBuilder();
		netMsg.setTipId(tipId);
		for (Object arg : args) {
			netMsg.addContent(String.valueOf(arg));
		}
		GatewayLinkMgr.getInstance().sendAll(Protocol.G_BROADCAST_SYSTEM_MSG, netMsg);
	}
}
