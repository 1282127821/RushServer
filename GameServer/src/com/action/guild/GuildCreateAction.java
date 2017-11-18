package com.action.guild;

import com.executor.AbstractAction;
import com.guild.GuildMgr;
import com.player.GamePlayer;

/**
 * 公会创建操作
 */
public class GuildCreateAction extends AbstractAction
{
	private GamePlayer player;
	private String guildName;
	private String guildSlogan;
	private int guildEmblem;

	public GuildCreateAction(GamePlayer player, String guildName, String guildSlogan, int guildEmblem)
	{
		this.player = player;
		this.guildName = guildName;
		this.guildSlogan = guildSlogan;
		this.guildEmblem = guildEmblem;
	}

	@Override
	public void execute()
	{
		GuildMgr.getInstance().createGuild(player, guildName, guildSlogan, (short) guildEmblem);
	}
}
