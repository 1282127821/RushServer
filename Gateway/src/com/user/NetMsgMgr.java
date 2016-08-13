package com.user;

import java.util.concurrent.ConcurrentHashMap;

import com.netmsg.chat.BroadCastMsg;
import com.netmsg.user.DeletePlayerCmd;
import com.netmsg.user.KickoutPlayerCmd;
import com.netmsg.user.SyncServerTimeCmd;
import com.netmsg.user.UserLoginCmd;
import com.netmsg.user.UserNoticeCmd;
import com.netmsg.user.UserWaiteCmd;
import com.protocol.Protocol;
import com.util.GameLog;

/**
 * 网络消息管理器
 */
public final class NetMsgMgr {
	private ConcurrentHashMap<Short, NetCmd> netMsgMap = new ConcurrentHashMap<Short, NetCmd>();
	private static NetMsgMgr instance = new NetMsgMgr();

	public static NetMsgMgr getInstance() {
		return instance;
	}

	public boolean init() {
		registerNetMsg(Protocol.G_BROADCAST_SYSTEM_MSG, new BroadCastMsg());
		registerNetMsg(Protocol.G_DELETE_USER, new DeletePlayerCmd());
		registerNetMsg(Protocol.G_KICK_AWAY_PLAYER, new KickoutPlayerCmd());
		registerNetMsg(Protocol.G_SYNC_TIME, new SyncServerTimeCmd());
		registerNetMsg(Protocol.G_LOGIN_GATEWAY, new UserLoginCmd());
		registerNetMsg(Protocol.G_PLAYER_NOTICE, new UserNoticeCmd());
		registerNetMsg(Protocol.G_PLAYER_WAITE, new UserWaiteCmd());
		return true;
	}

	private void registerNetMsg(short codeId, NetCmd netCmd) {
		if (netMsgMap.containsKey(codeId)) {
			GameLog.error("网络协议号重复:  0x: " + Integer.toHexString(codeId));
			System.exit(0);
		}
		
		netMsgMap.put(codeId, netCmd);
	}

	public NetCmd getNetCmd(short codeId) {
		return netMsgMap.get(codeId);
	}
}
