package com.executor;

public abstract class AbstractAction extends AbstractTask
{
	public AbstractAction()
	{
		super(null);
	}

	/**
	 * 设置当前Action与自驱动队列的关联
	 */
	public void setActionQueue(CmdTaskQueue queue)
	{
		this.queue = queue;
	}
}
