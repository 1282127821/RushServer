package com.game;

import java.io.Serializable;
import java.util.Arrays;

import com.google.protobuf.Message;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

/**
 * 服务器和客户端,服务器和服务器直接数据传输的对象
 **/
public class PBMessage implements Serializable {
	private static final long serialVersionUID = 1L;
	public static final short HEADER_LEN = 17; // 这个数据主要是指包头的大小
	public static final short HEADER = 0x71ab;
	
	public static final String KEY = "LAOZHONGGU_RUSH_9999";
	public static final byte[] KEY_BYTE = KEY.getBytes();

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

	public void readHeader(ByteBuf in) {
		in.readShort();
		len = in.readShort();
		checksum = in.readShort();
		codeId = in.readShort();
		userId = in.readLong();
	}

	public void writeHeader(int len, ByteBuf out) {
		out.writeShort(PBMessage.HEADER);
		out.writeShort(len);
		out.writeShort(checksum);
		out.writeShort(codeId);
		out.writeLong(userId);
	}

	/**
	 * 只设置数据体
	 */
	public void writeBodyBytes(byte[] bodyBytes, int len) {
		ByteBuf out = Unpooled.buffer(len + HEADER_LEN);
		writeHeader(len + HEADER_LEN, out);
		out.writeBytes(bodyBytes, 0, len);
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

	/**
	 * 位加密
	 */
	public static byte[] encryptForDis (byte[] bytes){
		byte[] mes = new byte[KEY_BYTE.length+bytes.length];
		System.arraycopy(KEY_BYTE,0,mes,0,KEY_BYTE.length);
		System.arraycopy(bytes,0,mes,KEY_BYTE.length,bytes.length);

		byte buff;
		for(int i=0; i<mes.length; i+=5){
			if(i + 3 > mes.length - 1) break;
			buff = (byte) ~mes[i + 2];
			mes[i + 2] = mes[i + 3];
			mes[i + 3] = buff;
		}
		return mes;
	}

	/**
	 * 位解密
	 */
	public static byte[] decryptForDis (byte[] bytes){
		byte buff;
		for(int i=0; i<bytes.length; i+=5){
			if(i + 3 > bytes.length - 1) break;
			buff = bytes[i + 2];
			bytes[i + 2] = (byte) ~bytes[i + 3];
			bytes[i + 3] = buff;
		}
		byte[] mes = new byte[bytes.length-KEY_BYTE.length];
		System.arraycopy(bytes,KEY_BYTE.length,mes,0,bytes.length-KEY_BYTE.length);
		return mes;
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
