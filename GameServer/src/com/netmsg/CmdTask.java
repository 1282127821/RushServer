package com.netmsg;

import java.time.Clock;

import com.netmsg.PBMessage;
import com.player.GamePlayer;
import com.util.GameLog;

public class CmdTask implements Runnable {
	private CmdTaskQueue queue;
	private GamePlayer player;
	private PBMessage packet;
	private NetCmd netCmd;
	protected long createTime;

	public CmdTask(GamePlayer player, NetCmd netCmd, PBMessage packet, CmdTaskQueue queue) {
		this.player = player;
		this.netCmd = netCmd;
		this.packet = packet;
		this.queue = queue;
		createTime = Clock.systemDefaultZone().millis();
	}

	@Override
	public void run() {
		if (queue != null) {
			long start = Clock.systemDefaultZone().millis();
			try {
				netCmd.execute(player, packet);
				long end = Clock.systemDefaultZone().millis();
				long interval = end - start;
				long leftTime = end - createTime;
				if (interval >= 1000) {
					GameLog.warn("execute action : " + this.toString() + ", interval : " + interval + ", leftTime : " + leftTime + ", size : " + queue.getQueue().size());
				}
			} catch (Exception e) {
				GameLog.error("执行netCmd异常, netCmd : " + netCmd.toString() + ", packet : " + packet.toString(), e);
			} finally {
				queue.dequeue(this);
			}
		}
	}

	@Override
	public String toString() {
		return netCmd.toString() + ", packet : " + packet.headerToStr();
	}
}
