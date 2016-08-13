package com.route;

import org.apache.mina.core.session.IoSession;

import com.netmsg.PBMessage;
import com.user.NetCmd;
import com.util.GameLog;

/**
 * 执行cmd,将cmd封装成一个个cmdTask放置于线程池中执行
 */
public class CmdTask implements Runnable {
	private NetCmd netCmd;
	private IoSession session;
	private PBMessage packet;

	public CmdTask(NetCmd netCmd, IoSession session, PBMessage packet) {
		this.netCmd = netCmd;
		this.session = session;
		this.packet = packet;
	}

	@Override
	public void run() {
		try {
			netCmd.execute(session, packet);
		} catch (Exception e) {
			GameLog.error("执行 command 异常, command : " + netCmd.toString() + ", packet : " + packet.toString(), e);
		}
	}
}
