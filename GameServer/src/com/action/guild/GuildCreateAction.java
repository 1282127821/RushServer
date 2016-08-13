package com.action.guild;

import com.execaction.Action;
import com.guild.GuildMgr;
import com.player.GamePlayer;
import com.room.RoomMgr;

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
