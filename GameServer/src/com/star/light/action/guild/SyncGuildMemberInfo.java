package com.star.light.action.guild;

import com.star.light.execaction.Action;
import com.star.light.guild.Guild;

/**
 * 同步公会成员的属性
 */
public class SyncGuildMemberInfo extends Action {
	private Guild guild;
	private long userId;
	private short propertyType;
	private int propertyValue;
	
	public SyncGuildMemberInfo(Guild guild, long userId, short propertyType, int propertyValue) {
		super(guild.getActionQueue());
		this.guild = guild;
		this.userId = userId;
		this.propertyType = propertyType;
		this.propertyValue = propertyValue;
	}
	
	@Override
	public void execute() {
		guild.syncUpdateMemInfo(userId, propertyType, propertyValue);
	}
}
