package com.rush.game;

import java.util.concurrent.ConcurrentHashMap;

import com.rush.cmd.account.AccountLoginCmd;
import com.rush.cmd.account.CreatePlayerCmd;
import com.rush.cmd.account.DelPlayerCmd;
import com.rush.protocol.Protocol;
import com.rush.util.GameLog;

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
		registerNetMsg(Protocol.C_S_ACCOUNT_LOGIN, new AccountLoginCmd());
		registerNetMsg(Protocol.C_S_CREATE_PLAYER, new CreatePlayerCmd());
		registerNetMsg(Protocol.C_S_DELETE_PLAYER, new DelPlayerCmd());
		return true;
	}

	private void registerNetMsg(short codeId, NetMsg netMsg) {
		if (netMsgMap.containsKey(codeId)) {
			GameLog.error("网络协议号重复:  0x: " + Integer.toHexString(codeId));
			System.exit(0);
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
