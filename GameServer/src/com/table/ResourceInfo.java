package com.table;

import java.util.ArrayList;
import java.util.List;

import com.util.StringUtil;

public class ResourceInfo
{
	/**
	 * 资源Id
	 */
	public int resourceId;

	/**
	 * 资源数量
	 */
	public int count;

	public static final List<ResourceInfo> EMPTY_RESOURCE_LIST = new ArrayList<ResourceInfo>(0);

	public ResourceInfo(int resourceId, int count)
	{
		this.resourceId = resourceId;
		this.count = count;
	}

	/**
	 * 将资源类型数组转成字符串，数据库存储使用
	 */
	public static String getStringByResList(List<ResourceInfo> resourceList)
	{
		StringBuilder sb = new StringBuilder();
		for (ResourceInfo info : resourceList)
		{
			sb.append(info.resourceId).append(":").append(info.count).append(",");
		}
		return sb.toString();
	}

	/**
	 * 将字符串转成资源类型数组，程序中使用
	 */
	public static void getResListByStr(String resStr, List<ResourceInfo> resourceList)
	{
		if (!StringUtil.isNull(resStr))
		{
			String[] aryResourceInfo = resStr.split(",");
			for (int i = 0, len = aryResourceInfo.length; i < len; i++)
			{
				String[] aryInfo = aryResourceInfo[i].split(":");
				resourceList.add(new ResourceInfo(Integer.parseInt(aryInfo[0]), Integer.parseInt(aryInfo[1])));
			}
		}
	}
}
