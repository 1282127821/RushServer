package com.star.light.action.guild;

import com.star.light.execaction.Action;
import com.star.light.guild.Guild;
import com.star.light.player.GamePlayer;

/**
 * 公会捐献操作
 */
public class GuildDonateAction extends Action {
	private Guild guild;
	private GamePlayer player;
	private int contribution;
	private int donateType;
	
	public GuildDonateAction(Guild guild, GamePlayer player, int contribution, int donateType) {
		super(guild.getActionQueue());
		this.guild = guild;
		this.player = player;
		this.contribution = contribution;
		this.donateType = donateType;
	}
	
	@Override
	public void execute() {
		guild.addContribution(player, contribution, donateType);
	}
}
