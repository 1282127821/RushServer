package com.network;

import java.util.List;

import com.netmsg.PBMessage;
import com.util.GameLog;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageEncoder;

/**
 * 数据包编码器
 */
public class PBEncoder extends MessageToMessageEncoder<PBMessage> {
	/**
	 * 对数据包进行二进制流编码
	 */
	@Override
	public void encode(ChannelHandlerContext ctx, PBMessage msg, List<Object> out) throws Exception {
		int size = PBMessage.HEADER_LEN;
		byte[] bytes = msg.getMsgBody();
		if (bytes != null) {
			size = PBMessage.HEADER_LEN + bytes.length;
		}

		if (size > Short.MAX_VALUE) {
			GameLog.error("msgId : " + msg.getMsgId() + ", over max length");
			return;
		}

		// TODO:lzg熟悉Netty之后，这里可以考虑使用内存池的方式，担心需要经过测试是否会产生内存泄漏。
		ByteBuf buffer = Unpooled.buffer(size);
		buffer.writeShort(msg.getMsgId());
		buffer.writeShort(size);
		if (bytes != null) {
			buffer.writeBytes(bytes);
		}
		out.add(buffer);
	}
}