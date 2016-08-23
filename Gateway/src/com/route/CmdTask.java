package com.route;

import com.netmsg.PBMessage;
import com.user.NetCmd;
import com.util.GameLog;

import io.netty.channel.Channel;

/**
 * 执行cmd,将cmd封装成一个个cmdTask放置于线程池中执行
 */
public class CmdTask implements Runnable {
	private NetCmd netCmd;
	private Channel channel;
	private PBMessage packet;

	public CmdTask(NetCmd netCmd, Channel channel, PBMessage packet) {
		this.netCmd = netCmd;
		this.channel = channel;
		this.packet = packet;
	}

	@Override
	public void run() {
		try {
			netCmd.execute(channel, packet);
		} catch (Exception e) {
			GameLog.error("执行 command 异常, command : " + netCmd.toString() + ", packet : " + packet.toString(), e);
		}
	}
}
