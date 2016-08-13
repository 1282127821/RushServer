package com.action.guild;

import com.execaction.Action;
import com.guild.Guild;
import com.player.GamePlayer;

/**
 * 申请加入公会操作
 */
public class ApplyJoinGuildAction extends Action {
	private Guild guild;
	private GamePlayer player;
	
	public ApplyJoinGuildAction(Guild guild, GamePlayer player) {
		super(guild.getActionQueue());
		this.guild = guild;
		this.player = player;
	}
	
	@Override
	public void execute() {
		guild.applyJoinGuild(player);
	}
}
