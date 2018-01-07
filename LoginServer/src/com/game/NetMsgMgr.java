package com.game;

import java.util.concurrent.ConcurrentHashMap;

import com.netmsg.account.AccountLoginCmd;
import com.netmsg.account.CreatePlayerCmd;
import com.netmsg.account.DelPlayerCmd;
import com.protocol.Protocol;
import com.util.Log;

/**
 * 网络消息管理器
 */
public final class NetMsgMgr {
	private ConcurrentHashMap<Short, NetMsg> netMsgMap = new ConcurrentHashMap<Short, NetMsg>();
	private static NetMsgMgr instance = new NetMsgMgr();

	public static NetMsgMgr getInstance() {
		return instance;
	}

	/**
	 * 加载网络消息集合
	 */
	public boolean init() {
		registerNetMsg(Protocol.C2S_ACCOUNT_LOGIN, new AccountLoginCmd());
		registerNetMsg(Protocol.C2S_CREATE_PLAYER, new CreatePlayerCmd());
		registerNetMsg(Protocol.C2S_DELETE_PLAYER, new DelPlayerCmd());
		return true;
	}

	private void registerNetMsg(short codeId, NetMsg netMsg) {
		if (netMsgMap.containsKey(codeId)) {
			Log.error("网络协议号重复:  0x: " + Integer.toHexString(codeId));
			System.exit(1);
		}

		netMsgMap.put(codeId, netMsg);
	}

	/**
	 * 缓存中获取命令
	 */
	public NetMsg getNetMsg(short codeId) {
		return netMsgMap.get(codeId);
	}
}
