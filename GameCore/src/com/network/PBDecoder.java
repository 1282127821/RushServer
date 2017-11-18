package com.network;

import java.util.List;

import com.netmsg.PBMessage;
import com.util.Log;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;

/**
 * 数据包解码器
 */
public class PBDecoder extends MessageToMessageDecoder<ByteBuf> {
	/**
	 * 把二进制流解码为服务器使用的数据包格式
	 */
	@Override
	public void decode(ChannelHandlerContext ctx, ByteBuf msg, List<Object> out) throws Exception {
		if (msg.readableBytes() < PBMessage.HEADER_LEN) {
			return;
		}

		short msgLen = msg.readShort(); // 消息长度
		if (msgLen <= 0 || msgLen >= Short.MAX_VALUE) {
			Log.error("消息包的长度非法，消息长度为: " + msgLen);
			return;
		}

		// 数据还不够读取,等待下一次读取
		int bodyLen = msgLen - PBMessage.HEADER_LEN;
		if (bodyLen > msg.readableBytes()) {
			Log.error("消息包的包体内容长度不够");
			msg.resetReaderIndex();
			return;
		}

		// 消息内容
		PBMessage pbMsg = new PBMessage(msg.readShort());
		if (bodyLen > 0) {
			byte[] msgBody = new byte[bodyLen];
			msg.readBytes(msgBody);
			pbMsg.setMsgBody(msgBody);
		}

		out.add(pbMsg);
	}
}
