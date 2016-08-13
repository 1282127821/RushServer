package com.mina;

import java.io.IOException;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolEncoderAdapter;
import org.apache.mina.filter.codec.ProtocolEncoderOutput;

import com.google.protobuf.Message;
import com.netmsg.PBMessage;
import com.util.GameLog;

/**
 * 数据包编码器
 */
public class ProtobufEncoder extends ProtocolEncoderAdapter {
	public ProtobufEncoder() {
	}

	/**
	 * 对数据包进行二进制流编码
	 */
	public void encode(IoSession session, Object message, ProtocolEncoderOutput out) throws Exception {
		//TODO:LZGLZG 这里的编码可以考虑直接在上层转成byte即可，这里不再做任何操作，这样是为了解耦发送给服务器和客户端的包
		PBMessage rsp = (PBMessage) message;
		IoBuffer buffer = null;
		if (rsp.getMessage() != null) {
			buffer = encodeMessage(rsp);
		} else {
			buffer = encodeByte(rsp);
		}

		if (buffer == null) {// 丢弃这个数据包
			GameLog.error("code: 0x" + Integer.toHexString(rsp.getCodeId()) + ", over max length, UserId = " + rsp.getUserId());
			return;
		}

		buffer.flip();
		out.write(buffer);
	}

	/**
	 * 使用PB压缩的数据包格式编码
	 */
	private IoBuffer encodeMessage(PBMessage pbMessage) throws IOException {
		Message msg = pbMessage.getMessage();
		int size = PBMessage.HDR_SIZE;
		if (msg != null) {
			size = PBMessage.HDR_SIZE + msg.getSerializedSize(); // 数据长度
		}

		if (size > Short.MAX_VALUE) {
			return null;
		}

		IoBuffer buffer = IoBuffer.allocate(size);
		pbMessage.writeHeader(size, buffer); // 协议头部
		if (msg != null) {
			msg.writeTo(buffer.asOutputStream());
		}
		return buffer;
	}

	public static IoBuffer encodeByte(PBMessage pbMessage) throws IOException {
		byte[] bytes = pbMessage.getMsgBody();
		int size = PBMessage.HDR_SIZE;
		if (bytes != null) {
			size = PBMessage.HDR_SIZE + bytes.length;
		}

		if (size > Short.MAX_VALUE) {
			return null;
		}
		IoBuffer buffer = IoBuffer.allocate(size);
		pbMessage.writeHeader(size, buffer); // 协议头部
		if (bytes != null) {
			buffer.put(bytes);
		}

		return buffer;
	}
}