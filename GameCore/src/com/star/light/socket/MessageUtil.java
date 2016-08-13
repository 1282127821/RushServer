package com.star.light.socket;

import com.google.protobuf.AbstractMessage.Builder;
import com.google.protobuf.Message;

/**
 * 消息辅助类
 */
public final class MessageUtil {
	/**
	 * 消息创建
	 */
	public static PBMessage buildMessage(short code, Builder<?> messageBuilder) {
		return buildMessage(code, -1, messageBuilder.build());
	}

	/**
	 * 消息创建
	 */
	public static PBMessage buildMessage(short code, long userId, Message msg) {
		PBMessage response = new PBMessage(code, userId);
		response.setMessage(msg);
		return response;
	}
}
