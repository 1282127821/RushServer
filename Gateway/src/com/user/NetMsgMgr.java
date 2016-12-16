package com.user;

import java.util.concurrent.ConcurrentHashMap;

/**
 * 网络消息管理器
 */
public final class NetMsgMgr
{
	private ConcurrentHashMap<Short, NetCmd> netMsgMap = new ConcurrentHashMap<Short, NetCmd>();
	private static NetMsgMgr instance = new NetMsgMgr();

	public static NetMsgMgr getInstance()
	{
		return instance;
	}

	public boolean init()
	{

		return true;
	}

//	private void registerNetMsg(short codeId, NetCmd netCmd)
//	{
//		if (netMsgMap.containsKey(codeId))
//		{
//			GameLog.error("网络协议号重复:  0x: " + Integer.toHexString(codeId));
//			System.exit(0);
//		}
//
//		netMsgMap.put(codeId, netCmd);
//	}

	public NetCmd getNetCmd(short codeId)
	{
		return netMsgMap.get(codeId);
	}
}
