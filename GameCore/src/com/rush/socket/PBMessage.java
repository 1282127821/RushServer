package com.rush.socket;

import java.io.Serializable;
import java.util.Arrays;

import org.apache.mina.core.buffer.IoBuffer;

import com.google.protobuf.Message;

/**
 * 服务器和客户端,服务器和服务器直接数据传输的对象
 **/
public class PBMessage implements Serializable {
	private static final long serialVersionUID = 1L;
	public static final short HDR_SIZE = 16; // 这个数据主要是指包头的大小
	public static final short HEADER = 0x71ab;

	/** 以下字段如果有更改大小，请务必修改包头的大小 */
	/**
	 * 包头
	 */
	private short header = HEADER;

	/**
	 * 数据包长度
	 */
	private short len;

	/**
	 * 校验和(udp 包，该字段使用包序号)
	 */
	private short checksum;

	/**
	 * 协议号
	 */
	private short codeId;

	/**
	 * 玩家ID
	 */
	private long userId;

	/**
	 * 消息内容
	 */
	private byte[] msgBody;

	/**
	 * Protocol Buffer的Proto内容
	 */
	private Message message;

	private PBMessage() {
	}

	public PBMessage(short codeId) {
		this(codeId, -1);
	}

	public PBMessage(short codeId, long userId) {
		this.codeId = codeId;
		this.userId = userId;
	}

	public short getHeader() {
		return header;
	}

	public short getLen() {
		return len;
	}

	public void setLen(short len) {
		this.len = len;
	}

	public short getChecksum() {
		return checksum;
	}

	public void setChecksum(short checksum) {
		this.checksum = checksum;
	}

	public short getCodeId() {
		return codeId;
	}

	public void setCodeId(short codeId) {
		this.codeId = codeId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	public long getUserId() {
		return userId;
	}

	public byte[] getMsgBody() {
		return msgBody;
	}

	public void setMsgBody(byte[] msgBody) {
		this.msgBody = msgBody;
	}

	public Message getMessage() {
		return message;
	}

	public void setMessage(Message message) {
		this.message = message;
	}

	public void readHeader(IoBuffer in) {
		in.getShort();
		len = in.getShort();
		checksum = in.getShort();
		codeId = in.getShort();
		userId = in.getLong();
	}

	public void writeHeader(int len, IoBuffer out) {
		out.putShort(PBMessage.HEADER);
		out.putShort((short)len);
		out.putShort(checksum);
		out.putShort(codeId);
		out.putLong(userId);
	}

	/**
	 * 只设置数据体
	 */
	public void writeBodyBytes(byte[] bodyBytes, int len) {
		IoBuffer out = IoBuffer.allocate(len + HDR_SIZE);
		writeHeader(len + HDR_SIZE, out);
		out.put(bodyBytes, 0, len);
		out.flip();
		msgBody = out.array();
	}

	/**
	 * 创建空消息(避免外部实例化)
	 */
	public static PBMessage buildPBMessage() {
		return new PBMessage();
	}

	public short calcChecksum(byte[] data) {
		int val = 0x77;
		int i = 6;
		int size = data.length;
		while (i < size) {
			val += (data[i++] & 0xFF);
		}
		return (short) (val & 0x7F7F);
	}

	public void clearCheckSum() {
		checksum = 0;
	}

	public String headerToStr() {
		StringBuilder sb = new StringBuilder();
		sb.append("userId : ").append(userId);
		sb.append(", code : ").append(Integer.toHexString(codeId));
		sb.append(", len : ").append(len);
		sb.append(", checksum : ").append(checksum);
		return sb.toString();
	}

	public String detailToStr() {
		if (msgBody == null) {
			return null;
		}
		StringBuilder sb = new StringBuilder();
		for (byte b : msgBody) {
			sb.append(b + ", ");
		}
		return headerToStr() + ", content : [" + sb.toString() + "]";
	}

	@Override
	public String toString() {
		return "PBMessage [bytes=" + Arrays.toString(msgBody) + ", checksum=" + checksum + ", codeId=" + codeId + ", header=" + header + ", len=" + len + ", message=" + message + ", userId=" + userId
				+ "]";
	}
}
