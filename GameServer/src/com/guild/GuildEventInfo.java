package com.guild;

import com.db.DBObject;

public class GuildEventInfo extends DBObject {
	/**
	 * 帮会事件的描述
	 */
	private String eventDesc;
	
	/**
	 * 产生该事件的时间
	 */
	private int eventTime;

	public String getEventDesc() {
		return eventDesc;
	}

	public void setEventDesc(String eventDesc) {
		this.eventDesc = eventDesc;
	}

	public int getEventTime() {
		return eventTime;
	}

	public void setEventTime(int eventTime) {
		this.eventTime = eventTime;
	}
	
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append("eventDesc:		").append(eventDesc).append("\n");
		sb.append("eventTime:		").append(eventTime).append("\n");
		return sb.toString();
	}
}
