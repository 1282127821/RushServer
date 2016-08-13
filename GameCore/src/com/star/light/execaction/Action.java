package com.star.light.execaction;

import com.star.light.util.GameLog;

public abstract class Action implements Runnable {
	private ActionQueue queue;
	protected long createTime;

	public Action(ActionQueue queue) {
		this.queue = queue;
		createTime = System.currentTimeMillis();
	}

	public ActionQueue getActionQueue() {
		return queue;
	}

	@Override
	public void run() {
		if (queue != null) {
			long start = System.currentTimeMillis();
			try {
				execute();
				long end = System.currentTimeMillis();
				long interval = end - start;
				long leftTime = end - createTime;
				if (interval >= 1000) {
					GameLog.warn("execute action : " + this.toString() + ", interval : " + interval + ", leftTime : " + leftTime + ", size : " + queue.getQueue().size());
				}
			} catch (Exception e) {
				GameLog.error("run action execute exception. action : " + this.toString(), e);
			} finally {
				queue.dequeue(this);
			}
		}
	}

	public abstract void execute();
}
