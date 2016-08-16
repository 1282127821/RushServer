package com.netmsg;

import com.google.protobuf.AbstractMessage.Builder;

/**
 * 消息辅助类
 */
public final class MessageUtil {
	/**
	 * 消息创建
	 */
	public static PBMessage buildMessage(short code, Builder<?> messageBuilder) {
		PBMessage response = new PBMessage(code, -1);
		response.setMessage(messageBuilder.build());
		return response;
	}
}
