package com.netmsg;

import java.util.LinkedList;
import java.util.Queue;

import com.util.GameLog;

/**
 * CmdTaskQueue基本功能实现
 */
public class AbstractCmdTaskQueue implements CmdTaskQueue {

	//TODO:LZGLZG这个阻塞队列是否可以考虑使用Java本身自带的呢？
	private Queue<CmdTask> queue;
	private CmdExecutor executor;
//	private BlockingQueue<CmdTask> queue = new LinkedBlockingQueue<CmdTask>();

	public AbstractCmdTaskQueue(CmdExecutor executor) {
		this.executor = executor;
		queue = new LinkedList<CmdTask>();
	}

	public CmdTaskQueue getCmdTaskQueue() {
		return this;
	}

	public void enqueue(CmdTask cmd) {
		boolean canExec = false;
		synchronized (queue) {
			queue.add(cmd);
			if (queue.size() == 1) {
				canExec = true;
			} else if (queue.size() > 1000) {
				GameLog.warn(cmd.toString() + " queue size : " + queue.size());
			}
		}

		if (canExec) {
			executor.execute(cmd);
		}
	}

	public void dequeue(CmdTask cmdTask) {
		CmdTask nextCmdTask = null;
		synchronized (queue) {
			if (queue.size() == 0) {
				GameLog.error("queue.size() is 0.");
			}
			CmdTask temp = queue.remove();
			if (temp != cmdTask) {
				GameLog.error("action queue error. temp " + temp.toString() + ", action : " + cmdTask.toString());
			}
			if (queue.size() != 0) {
				nextCmdTask = queue.peek();
			}
		}

		if (nextCmdTask != null) {
			executor.execute(nextCmdTask);
		}
	}

	public Queue<CmdTask> getQueue() {
		return queue;
	}

	public void clear() {
		synchronized (queue) {
			queue.clear();
		}
	}
}
