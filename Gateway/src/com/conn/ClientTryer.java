package com.conn;

import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import com.network.LinkedClient;
import com.util.Log;

/**
 * 该类负责如果服务器端连接断开,尝试继续连接
 */
public final class ClientTryer {
	private ScheduledThreadPoolExecutor executor = new ScheduledThreadPoolExecutor(1);
	private static final ClientTryer inststance = new ClientTryer();
	public static ClientTryer getInstance() {
		return inststance;
	}

	/**
	 * 尝试连接
	 * 
	 * @param client  尝试连接的服务器       
	 * @param period  周期(秒)
	 * @param tryTimes  尝试次数
	 *            
	 */
	public void ctry(LinkedClient client, int period, int tryTimes) {
		TryRunner tryRunner = new TryRunner(client, tryTimes);
		ScheduledFuture<?> future = executor.scheduleAtFixedRate(tryRunner, period, period, TimeUnit.SECONDS);
		tryRunner.setFuture(future);
	}
}

/**
 * 连接,直到连接成功或者到达尝试次数为止
 */
class TryRunner implements Runnable {
	ScheduledFuture<?> future = null;
	int tryTimes = -1;
	LinkedClient client = null;

	public TryRunner(LinkedClient client, int tryTimes) {
		this.client = client;
		this.tryTimes = tryTimes;
	}

	public void setFuture(ScheduledFuture<?> future) {
		this.future = future;
	}

	public void run() {
		try {
			if (tryTimes > 0) {
				tryTimes--;
			}
			
			if (!client.isTry()) {
				future.cancel(true);
				Log.info("Try connect to " + client.getAddress() + ":" + client.getPort() + " cancel, tryRunner will exit.");
				return;
			}

			boolean result = client.connect();
			Log.info("try connect to server, address is " + client.getAddress() + ":" + client.getPort() + (result ? " succeed" : " failed") + ".");
			if (result) {
				future.cancel(true);
				Log.info("Try connect to " + client.getAddress() + ":" + client.getPort() + " succeed,tryRunner exit.");
				ClientSet.sendRegisterMsg(client.getType(), client);
			} else if (tryTimes == 0) {
				future.cancel(true);
				ClientSet.removeServerClient(client);
				Log.info("Try connect to " + client.getAddress() + " fail, tryRunner will exit.");
			} 
		} catch (Exception e) {
			Log.error("TryRunner has exception:", e);
		}
	}
}
