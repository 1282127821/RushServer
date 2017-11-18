package com.executor;

import com.util.TimeUtil;

public abstract class AbstractDelayTask extends AbstractTask
{
	private long exeTime;

    public AbstractDelayTask(CmdTaskQueue queue, int delay)
    {
        super(queue);

        exeTime = TimeUtil.getSysCurTimeMillis() + delay;
    }

    public boolean checkDelayFinishAndExecute(long time)
    {
        if (time >= exeTime)
        {
            queue.add(this);
            return true;
        }

        return false;
    }
}
