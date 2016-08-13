package com.star.light.action.guild;

import com.star.light.execaction.Action;
import com.star.light.guild.GuildMgr;
import com.star.light.room.RoomMgr;

/**
 * 公会解散操作
 */
public class GuildDeleteAction extends Action {
	private long guildId;
	
	public GuildDeleteAction(long guildId) {
		super(RoomMgr.executor.getDefaultQueue());
		this.guildId = guildId;
	}
	
	@Override
	public void execute() {
		GuildMgr.getInstance().deleteGuild(guildId);
	}
}
