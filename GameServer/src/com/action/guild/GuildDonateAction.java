package com.action.guild;

import com.executor.AbstractAction;
import com.guild.Guild;
import com.player.GamePlayer;

/**
 * 公会捐献操作
 */
public class GuildDonateAction extends AbstractAction
{
	private Guild guild;
	private GamePlayer player;
	private int contribution;
	private int donateType;

	public GuildDonateAction(Guild guild, GamePlayer player, int contribution, int donateType)
	{
		this.guild = guild;
		this.player = player;
		this.contribution = contribution;
		this.donateType = donateType;
	}

	@Override
	public void execute()
	{
		guild.addContribution(player, contribution, donateType);
	}
}
