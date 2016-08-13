package com.star.light.action.guild;

import com.star.light.execaction.Action;
import com.star.light.guild.GuildMgr;
import com.star.light.player.GamePlayer;
import com.star.light.room.RoomMgr;

/**
 * 公会创建操作
 */
public class GuildCreateAction extends Action {
	private GamePlayer player;
	private String guildName;
	private String guildSlogan;
	private int guildEmblem;

	public GuildCreateAction(GamePlayer player, String guildName, String guildSlogan, int guildEmblem) {
		super(RoomMgr.executor.getDefaultQueue());
		this.player = player;
		this.guildName = guildName;
		this.guildSlogan = guildSlogan;
		this.guildEmblem = guildEmblem;
	}

	@Override
	public void execute() {
		GuildMgr.getInstance().createGuild(player, guildName, guildSlogan, (short)guildEmblem);
	}
}
