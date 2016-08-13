package com.star.light.execaction;

import java.util.Queue;

public interface ActionQueue {
	
	ActionQueue getActionQueue();
	
	void enqueue(Action action);
	
	void enDelayQueue(DelayAction delayAction);
	
	void dequeue(Action action);
	
	void clear();
	
	Queue<Action> getQueue();
}
