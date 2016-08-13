package com.rush.mina;

import java.io.IOException;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolEncoderAdapter;
import org.apache.mina.filter.codec.ProtocolEncoderOutput;

import com.google.protobuf.Message;
import com.rush.socket.PBMessage;
import com.rush.util.GameLog;

public class ProtobufStrictEncoder extends ProtocolEncoderAdapter {

	private boolean isRecord = false;
	private boolean isStrict = false;

	public void setIsRecord(boolean isRecord) {
		this.isRecord = isRecord;
	}

	public void setIsStrict(boolean isStrict) {
		this.isStrict = isStrict;
	}

	@Override
	public void encode(IoSession session, Object message, ProtocolEncoderOutput out) throws Exception {
		try {
			// 若存在不同线程给同一玩家发送数据的情况，因此加密过程需要同步处理
			PBMessage pbMsg = (PBMessage) message;
			IoBuffer buffer = null;
			if (pbMsg.getMessage() != null) {
				buffer = encodeMessage(pbMsg);
				if (pbMsg.getMsgBody() != null) {
					GameLog.error("pbMsg.getMsgBody() is not null");
				}
			} else {
				buffer = encodeByte(pbMsg);
				if (pbMsg.getMessage() != null) {
					GameLog.error("pbMsg.getMessage() is not null");
				}
			}

			if (buffer == null) {// 丢弃这个数据包
				GameLog.error("code : " + pbMsg.getCodeId() + ", over max length, UserId = " + pbMsg.getUserId());
				return;
			}

			if (isStrict) {
				buffer.flip();
				int lastCipherByte = 0;

				int[] encryptKey = ProtobufStrictCodecFactory.getKeys();
				byte[] plainText = buffer.array();
				short checkSum = pbMsg.calcChecksum(plainText);
				plainText[4] = (byte) (checkSum >> 8);
				plainText[5] = (byte) checkSum;

				int length = plainText.length;
				IoBuffer cipherBuffer = IoBuffer.allocate(length);

				StringBuilder str = null;
				if (isRecord) {
					str = new StringBuilder();
					str.append("packet record ==");
					str.append("userId : ").append(pbMsg.getUserId()).append(" , ");
					str.append("code : ").append(Integer.toHexString(pbMsg.getCodeId())).append(" , ");
					str.append("pblen : ").append(pbMsg.getLen()).append(" , ");
					str.append("length : ").append(length).append(" , \n");
					str.append(toHexDump("Old key:", encryptKey, 0, encryptKey.length));
					str.append(toHexDump("Old Bytes:", plainText, 0, plainText.length));
				}

				// 加密首字节
				lastCipherByte = (byte) ((plainText[0] ^ encryptKey[0]) & 0xff);
				cipherBuffer.put((byte) lastCipherByte);

				// 循环加密
				int keyIndex = 0;
				for (int i = 1; i < length; i++) {
					keyIndex = i & 0x7;
					encryptKey[keyIndex] = ((encryptKey[keyIndex] + lastCipherByte) ^ i) & 0xff;
					lastCipherByte = (((plainText[i] ^ encryptKey[keyIndex]) & 0xff) + lastCipherByte) & 0xff;
					cipherBuffer.put((byte) lastCipherByte);
				}

				if (isRecord) {
					str.append("\n");
					str.append(toHexDump("Encrpted Bytes:", cipherBuffer.array(), 0, cipherBuffer.position()));
					str.append(toHexDump("Encrpted Keys", encryptKey, 0, encryptKey.length));
					str.append("Encrpted Keys hasdcode:").append(encryptKey.hashCode());
					GameLog.error(str.toString());
				}

				out.write(cipherBuffer.flip());
			} else {
				short checkSum = pbMsg.calcChecksum(buffer.array());
				byte byte1 = (byte) (checkSum >> 8);
				byte byte2 = (byte) checkSum;
				buffer.put(4, byte1);
				buffer.put(5, byte2);
				buffer.flip();
				out.write(buffer);
			}
		} catch (Exception ex) {
			GameLog.error("catch error for encoding packet:", ex);
			throw ex;
		}
	}

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

	private IoBuffer encodeByte(PBMessage pbMessage) throws IOException {
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

	public static String toHexDump(String description, int[] dump, int start, int count) {
		String hexDump = "";
		if (description != null) {
			hexDump += description;
			hexDump += "\n";
		}
		int end = start + count;
		for (int i = start; i < end; i += 16) {
			String text = "";
			String hex = "";

			for (int j = 0; j < 16; j++) {
				if (j + i < end) {
					int val = dump[j + i];
					if (val < 0)
						val = (val + 256) & 0xFF;
					if (val < 16) {
						hex += "0" + Integer.toHexString(val) + " ";
					} else {
						hex += Integer.toHexString(val) + " ";
					}

					if (val >= 32 && val <= 127) {
						text += (char) val;
					} else {
						text += ".";
					}
				} else {
					hex += "   ";
					text += " ";
				}
			}
			hex += "  ";
			hex += text;
			hex += '\n';
			hexDump += hex;
		}
		return hexDump;
	}

	public static String toHexDump(String description, byte[] dump, int start, int count) {
		int[] temps = new int[dump.length];
		System.arraycopy(dump, 0, temps, 0, dump.length);
		return toHexDump(description, temps, start, count);
	}
}
