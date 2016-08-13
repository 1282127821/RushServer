package com.star.light.socket.cmd;

import java.util.Queue;

/**
 * CmdTask命令处理队列
 */
public interface CmdTaskQueue {
	
	CmdTaskQueue getCmdTaskQueue();
	
	void enqueue(CmdTask cmdTask);
	
	void dequeue(CmdTask cmdTask);
	
	Queue<CmdTask> getQueue();
	
	void clear();
}
