package com;

import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IoSession;

import com.mina.LinkedClient;
import com.netmsg.CmdExecutor;
import com.netmsg.NetCmd;
import com.netmsg.NetMsgMgr;
import com.netmsg.PBMessage;
import com.player.GamePlayer;
import com.player.WorldMgr;
import com.util.GameLog;

public class GameServerHandler extends IoHandlerAdapter {
	public static CmdExecutor executor = new CmdExecutor(8, 64, 5, "GameServerHandler");
	private static NetMsgMgr netInstance = NetMsgMgr.getInstance();

	@Override
	public void sessionOpened(IoSession session) throws Exception {
		GameLog.info(toMessage("recieved a gateway connect from " + session.getRemoteAddress().toString()));
	}

	@Override
	public void sessionClosed(IoSession session) throws Exception {
		if (session.getAttribute(LinkedClient.KEY_CLIENT) != null) {
			GatewayLinkMgr.getInstance().removeLinkedClient(session);
			GameLog.error(toMessage("close the connection to gateway disconnected. session : " + session.getRemoteAddress().toString()));
		}
	}
	
	@Override
	public void messageReceived(IoSession session, Object message) throws Exception {
		PBMessage packet = (PBMessage) message;
		// TODO:LZGLZG网络消息协议应该在网关层被截获，能够到这里的必然是已有的
		NetCmd netCmd = netInstance.getNetCmd(packet.getCodeId());
		if (netCmd == null) {
			GameLog.error("not found cmd , code: 0x" + Integer.toHexString(packet.getCodeId()) + " , userId : " + packet.getUserId());
			return;
		}

		long userId = packet.getUserId();
		if (userId > 0) {
			try {
				GamePlayer player = WorldMgr.getOnlinePlayer(userId);
				if (player == null) {
					player = WorldMgr.getPlayer(userId);
					if (player == null) {
						GameLog.error("code " + packet.getCodeId() + " not found player " + userId + ",can not continue execute.");
						return;
					}
				}
				player.enqueue(netCmd, packet);
			} catch (Exception e) {
				GameLog.error("packet has exception. " + packet.headerToStr(), e);
			}
		} else {
//			executor.enDefaultQueue(netCmd, packet);
			GatewayLinkMgr.getInstance().addGameLinkedClient(session, packet);
		}
	}
	
	@Override
	public void exceptionCaught(IoSession session, Throwable cause) throws Exception {
		session.closeNow();
		GameLog.error(toMessage("caught exception that close the connection to gateway disconnected. session : " + session.getRemoteAddress().toString()), cause);
	}

	private String toMessage(String msg) {
		return "GameServerHandler " + msg;
	}
}
