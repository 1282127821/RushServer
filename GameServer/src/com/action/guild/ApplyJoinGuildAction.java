package com.action.guild;

import com.executor.AbstractAction;
import com.guild.Guild;
import com.player.GamePlayer;

/**
 * 申请加入公会操作
 */
public class ApplyJoinGuildAction extends AbstractAction
{
	private Guild guild;
	private GamePlayer player;

	public ApplyJoinGuildAction(Guild guild, GamePlayer player)
	{
		this.guild = guild;
		this.player = player;
	}

	@Override
	public void execute()
	{
		guild.applyJoinGuild(player);
	}
}
