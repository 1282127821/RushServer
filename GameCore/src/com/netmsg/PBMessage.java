package com.netmsg;

import java.io.Serializable;
import java.util.Arrays;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

/**
 * 服务器和客户端,服务器和服务器直接数据传输的对象
 **/
public class PBMessage implements Serializable
{
	private static final long serialVersionUID = 1L;
	public static final short HEADER_LEN = 4; // 这个数据主要是指包头的大小

	public static final String KEY = "LAOZHONGGU_RUSH_123456";
	public static final byte[] KEY_BYTE = KEY.getBytes();

	/** 以下字段如果有更改大小，请务必修改包头的大小 */
	/**
	 * 数据包长度
	 */
	private short len;

	/**
	 * 协议号
	 */
	private short msgId;

	/**
	 * 消息内容
	 */
	private byte[] msgContent;

	public PBMessage(short msgId)
	{
		this.msgId = msgId;
	}

	public short getLen()
	{
		return len;
	}

	public void setLen(short len)
	{
		this.len = len;
	}

	public short getMsgId()
	{
		return msgId;
	}

	public void setMsgId(short msgId)
	{
		this.msgId = msgId;
	}

	public byte[] getMsgBody()
	{
		return msgContent;
	}

	public void setMsgBody(byte[] msgContent)
	{
		this.msgContent = msgContent;
	}

	public void readHeader(ByteBuf in)
	{
		len = in.readShort();
		msgId = in.readShort();
	}

	public void writeHeader(int len, ByteBuf out)
	{
		out.writeShort(len);
		out.writeShort(msgId);
	}

	/**
	 * 只设置数据体
	 */
	public void writeBodyBytes(byte[] msgData, int len)
	{
		ByteBuf out = Unpooled.buffer(len + HEADER_LEN);
		out.writeShort(len + HEADER_LEN);
		out.writeShort(msgId);
		out.writeBytes(msgData, 0, len);
		this.msgContent = out.array();
	}

	public short calcChecksum(byte[] data)
	{
		int val = 0x77;
		int i = 6;
		int size = data.length;
		while (i < size)
		{
			val += (data[i++] & 0xFF);
		}
		return (short) (val & 0x7F7F);
	}

	public String headerToStr()
	{
		StringBuilder sb = new StringBuilder();
		sb.append(", msgId : ").append(Integer.toHexString(msgId));
		sb.append(", len : ").append(len);
		return sb.toString();
	}

	/**
	 * 位加密
	 */
	public static byte[] encryptForDis(byte[] bytes)
	{
		byte[] mes = new byte[KEY_BYTE.length + bytes.length];
		System.arraycopy(KEY_BYTE, 0, mes, 0, KEY_BYTE.length);
		System.arraycopy(bytes, 0, mes, KEY_BYTE.length, bytes.length);

		byte buff;
		for (int i = 0; i < mes.length; i += 5)
		{
			if (i + 3 > mes.length - 1)
				break;
			buff = (byte) ~mes[i + 2];
			mes[i + 2] = mes[i + 3];
			mes[i + 3] = buff;
		}
		return mes;
	}

	/**
	 * 位解密
	 */
	public static byte[] decryptForDis(byte[] bytes)
	{
		byte buff;
		for (int i = 0; i < bytes.length; i += 5)
		{
			if (i + 3 > bytes.length - 1)
				break;
			buff = bytes[i + 2];
			bytes[i + 2] = (byte) ~bytes[i + 3];
			bytes[i + 3] = buff;
		}
		byte[] mes = new byte[bytes.length - KEY_BYTE.length];
		System.arraycopy(bytes, KEY_BYTE.length, mes, 0, bytes.length - KEY_BYTE.length);
		return mes;
	}

	public String detailToStr()
	{
		if (msgContent == null)
		{
			return "";
		}

		StringBuilder sb = new StringBuilder();
		for (byte b : msgContent)
		{
			sb.append(b + ", ");
		}
		return headerToStr() + ", content : [" + sb.toString() + "]";
	}

	@Override
	public String toString()
	{
		return "PBMessage [bytes=" + Arrays.toString(msgContent) + ", msgId = " + msgId + ", len = " + len + "]";
	}
}
