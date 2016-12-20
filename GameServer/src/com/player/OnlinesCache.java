package com.player;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import com.util.GameLog;
import com.util.StringUtil;
import com.util.TimeUtil;

public class OnlinesCache extends AbstractCache
{
	private ConcurrentHashMap<Long, PlayerKey> onlineMap;

	/**
	 * @param scanInterval
	 *            同步在线人数间隔时长
	 * @param clearInterval
	 *            清除在线列表间隔时长
	 * @param isOpen
	 */
	public OnlinesCache(int scanInterval, int clearInterval, boolean isOpen)
	{
		super(scanInterval, clearInterval, isOpen);
		onlineMap = new ConcurrentHashMap<Long, PlayerKey>();
	}

	@Override
	public void add(Object para1, Object para2)
	{
		String key = (String) para1;
		long accountId = (Long) para2;
		PlayerKey info = new PlayerKey(accountId, key, TimeUtil.getSysCurSeconds());
		onlineMap.put(accountId, info);
		debugMsg(Long.toString(accountId), key, "注册用户");
	}

	public String getKey(long accountId)
	{
		PlayerKey info = onlineMap.get(accountId);
		return info != null ? info.getKey() : "";
	}

	// 在线用户扫描:用户同步在线用户人数
	@Override
	public void scan(boolean isPass)
	{
		if (!getIsOpen())
			return;

		if (TimeUtil.getSysCurSeconds() - getLastScanTime() > getScanInterval())
		{
			setLastScanTime(TimeUtil.getSysCurSeconds());
			try
			{
				setCurrentData(WorldMgr.getOnlineCount());
			}
			catch (Exception e)
			{
				GameLog.error("同步服务器在线人数出错!", e);
			}
		}
	}

	@Override
	public void clear()
	{
		if (TimeUtil.getSysCurSeconds() - getLastClearTime() > getClearInterval())
		{
			if (onlineMap == null || onlineMap.size() <= 0)
			{
				return;
			}

			List<PlayerKey> infos = new ArrayList<PlayerKey>();
			infos.addAll(onlineMap.values());
			for (PlayerKey info : infos)
			{
				if (info != null && timeOutClear(info.getLastDate()))
				{
					remove(info);
				}
			}

			setLastClearTime(TimeUtil.getSysCurSeconds());
		}
	}

	@Override
	public Object check(Object para1, Object para2)
	{
		long userId = (Long) para1;
		String key = (String) para2;
		try
		{
			if (StringUtil.isNull(key) || userId <= 0)	
				return false;
			key = key.toUpperCase();
			PlayerKey playerKey = onlineMap.get(userId);
			if (playerKey != null && playerKey.getKey().equalsIgnoreCase(key))
			{
				playerKey.setLastDate(TimeUtil.getSysCurSeconds());
				return true;
			}
			return false;
		}
		catch (Exception e)
		{
			String msg = String.format("较验失败UserId:%s,Key:%s", userId, key);
			GameLog.error(msg, e);
		}
		return false;
	}

	@Override
	public boolean update(Object para1, Object para2)
	{
		return false;
	}

	@Override
	public boolean remove(Object para1)
	{
		Long name = (Long) para1;
		if (onlineMap.get(name) != null)
		{
			onlineMap.remove(name);
			debugMsg(Long.toString(name), "", "删除在线");
		}
		return true;
	}

	@Override
	public void vip(Object para1)
	{

	}

	@Override
	public int getManCount()
	{
		return this.getCurrentData();
	}

	@Override
	public void reset()
	{

	}
}