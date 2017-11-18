package com.action.guild;

import com.executor.AbstractAction;
import com.guild.Guild;

/**
 * 同步公会成员的属性
 */
public class SyncGuildMemberInfo extends AbstractAction
{
	private Guild guild;
	private long userId;
	private short propertyType;
	private int propertyValue;

	public SyncGuildMemberInfo(Guild guild, long userId, short propertyType, int propertyValue)
	{
		this.guild = guild;
		this.userId = userId;
		this.propertyType = propertyType;
		this.propertyValue = propertyValue;
	}

	@Override
	public void execute()
	{
		guild.syncUpdateMemInfo(userId, propertyType, propertyValue);
	}
}
