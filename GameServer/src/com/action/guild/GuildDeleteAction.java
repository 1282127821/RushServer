package com.action.guild;

import com.execaction.Action;
import com.guild.GuildMgr;
import com.room.RoomMgr;

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
