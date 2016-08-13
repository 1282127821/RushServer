package com.rush.mina;

import java.nio.ByteOrder;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.CumulativeProtocolDecoder;
import org.apache.mina.filter.codec.ProtocolDecoderOutput;

import com.rush.socket.PBMessage;
import com.rush.util.GameLog;

public class ProtobufStrictDecoder extends CumulativeProtocolDecoder {

	private boolean isStrict = false;

	public void setIsStrict(boolean isStrict) {
		this.isStrict = isStrict;
	}

	@Override
	protected boolean doDecode(IoSession session, IoBuffer in, ProtocolDecoderOutput out) throws Exception {
		if (in.remaining() < 4) {
			// 剩余不足4字节，不足以解析数据包头，暂不处理
			return false;
		}
		in.order(ByteOrder.LITTLE_ENDIAN);
		int header = 0;
		int packetLength = 0;
		int[] decryptKey = ProtobufStrictCodecFactory.getKeys();
		if (isStrict) {
			// 此处4字节头部的解码使用直接解码形式，规避频繁的对象创建
			int cipherByte1, cipherByte2, cipherByte3, cipherByte4;
			int plainByte1, plainByte2, plainByte3, plainByte4;
			int key;
			// 解密两字节header
			cipherByte1 = in.get() & 0xff;
			key = decryptKey[0];
			plainByte1 = (cipherByte1 ^ key) & 0xff;

			cipherByte2 = in.get() & 0xff;
			key = ((decryptKey[1] + cipherByte1) ^ 1) & 0xff;
			plainByte2 = ((cipherByte2 - cipherByte1) ^ key) & 0xff;

			header = (plainByte1 + (plainByte2 << 8));

			// 解密两字节length
			cipherByte3 = in.get() & 0xff;
			key = ((decryptKey[2] + cipherByte2) ^ 2) & 0xff;
			plainByte3 = ((cipherByte3 - cipherByte2) ^ key) & 0xff;

			cipherByte4 = in.get() & 0xff;
			key = ((decryptKey[3] + cipherByte3) ^ 3) & 0xff;
			plainByte4 = ((cipherByte4 - cipherByte3) ^ key) & 0xff;
			packetLength = (plainByte3 + (plainByte4 << 8));

		} else {
			header = in.getShort();
			packetLength = in.getShort();
		}
		// 预解密长度信息成功，回溯位置
		in.position(in.position() - 4);

		if (header != PBMessage.HEADER) {
			// 非数据包头部，跳过，继续解密
			in.position(in.position() + 1);
			GameLog.info("数据包头不匹配 hearder : " + header);
			return true;
		}

		if (packetLength < PBMessage.HDR_SIZE) {
			// 数据包长度错误，断开连接
			GameLog.error(String.format("error packet length: packetlength=%d Packet.HDR_SIZE=%d", packetLength, PBMessage.HDR_SIZE));
			GameLog.error(String.format("Disconnect the client:%s", session.getRemoteAddress()));
			session.closeNow();
		}

		if (in.remaining() < packetLength) { // 数据长度不足，等待下次接收
			return false;
		}

		// 读取数据并解密数据
		byte[] data = new byte[packetLength];
		in.get(data, 0, packetLength);
		if (isStrict) {
			data = decrypt(data, decryptKey);
		}

		PBMessage packet = PBMessage.buildPBMessage();
		IoBuffer headerBuf = IoBuffer.wrap(data, 0, PBMessage.HDR_SIZE);
		headerBuf.order(ByteOrder.LITTLE_ENDIAN);
		packet.readHeader(headerBuf);

		short checkSum = packet.calcChecksum(data);
		if (packet.getChecksum() == checkSum) {
			int bodyLen = packetLength - PBMessage.HDR_SIZE;
			if (bodyLen > 0) {
				byte[] setMsgBody = new byte[bodyLen];
				System.arraycopy(data, PBMessage.HDR_SIZE, setMsgBody, 0, bodyLen);
				packet.setMsgBody(setMsgBody);
			}
			out.write(packet);
		} else {
			GameLog.warn(String.format("数据包校验失败，数据包将被丢弃。协议ID 0x%x%n", packet.getCodeId()));
			GameLog.warn(String.format("校验和应为%d，实际接收校验和为%d%n", checkSum, packet.getChecksum()));
		}

		return true;
	}

	// 解密整段数据
	private byte[] decrypt(byte[] data, int[] decryptKey) throws Exception {
		if (data.length == 0)
			return data;

		if (decryptKey.length < 8)
			throw new Exception("The decryptKey must be 64bits length!");

		int length = data.length;
		int lastCipherByte;
		int plainText;
		int key;

		// 解密首字节
		lastCipherByte = data[0] & 0xff;
		data[0] ^= decryptKey[0];

		for (int index = 1; index < length; index++) {
			// 解密当前字节
			key = ((decryptKey[index & 0x7] + lastCipherByte) ^ index);
			plainText = (((data[index] & 0xff) - lastCipherByte) ^ key) & 0xff;

			// 更新变量值
			lastCipherByte = data[index] & 0xff;
			data[index] = (byte) plainText;
			decryptKey[index & 0x7] = (byte) (key & 0xff);
		}

		return data;
	}
}
