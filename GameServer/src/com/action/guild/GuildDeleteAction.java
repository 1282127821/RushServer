package com.action.guild;

import com.executor.AbstractAction;
import com.guild.GuildMgr;

/**
 * 公会解散操作
 */
public class GuildDeleteAction extends AbstractAction
{
	private long guildId;

	public GuildDeleteAction(long guildId)
	{
		this.guildId = guildId;
	}

	@Override
	public void execute()
	{
		GuildMgr.getInstance().deleteGuild(guildId);
	}
}
