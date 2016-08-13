package com.rush.mina;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.CumulativeProtocolDecoder;
import org.apache.mina.filter.codec.ProtocolDecoderOutput;

import com.rush.socket.PBMessage;
import com.rush.util.GameLog;

/**
 * 数据包解码器
 */
public class ProtobufDecoder extends CumulativeProtocolDecoder {
	
	public ProtobufDecoder() {

	}
	
	/**
	 * 把二进制流解码为服务器使用的数据包格式
	 */
	@Override
	public boolean doDecode(IoSession session, IoBuffer in, ProtocolDecoderOutput out) throws Exception {
		if (in.remaining() < PBMessage.HDR_SIZE) {
			return false;
		}

		IoBuffer newBuf = in.slice();
		short headerFlag = newBuf.getShort();
		if (PBMessage.HEADER != headerFlag) {
			GameLog.error("Illegal client request, can not match header flag. drop a packet,close connection.");
			return false;
		}

		//长度
		int lenght = newBuf.getShort();
		if (lenght <= 0 || lenght >= Short.MAX_VALUE) {
			// 非法的数据长度
			GameLog.error("Message Length Invalid Length = " + lenght + ", drop this Message.");
			return true;
		}

		//数据还不够读取,等待下一次读取
		if (lenght > in.remaining()) { 
			return false;
		}

		PBMessage packet = PBMessage.buildPBMessage();
		packet.readHeader(in);

		// BODY
		int bodyLen = lenght - PBMessage.HDR_SIZE;
		if (bodyLen > 0) {
			byte[] bytes = new byte[bodyLen];
			in.get(bytes, 0, bodyLen);
			packet.setMsgBody(bytes);
		}
		
		out.write(packet);
		return true;
	}
}
