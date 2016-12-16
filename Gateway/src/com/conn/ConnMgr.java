package com.conn;

/**
 * 客户端连接管理
 **/
public final class ConnMgr
{
	private static ConnMgr connMgr = new ConnMgr();

	public static ConnMgr getInstance()
	{
		return connMgr;
	}

	public boolean init()
	{
		return reload();
	}

	public boolean reload()
	{
		// 连接到服务器端
		if (!ClientSet.init())
		{
			return false;
		}

		return loadClientConns();
	}

	/**
	 * 初始化客户端连接监听
	 */
	private boolean loadClientConns()
	{
		
		return true;
	}

	/**
	 * 停服清理网络连接
	 */
	public void stop()
	{
		
	}

	public void setIsRecord(boolean isRecord)
	{
		
	}
}