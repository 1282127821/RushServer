package com.game;

import com.util.Log;

import io.netty.channel.ChannelHandlerContext;

public class CmdTask implements Runnable
{
	private NetMsg netMsg;
	private ChannelHandlerContext channel;
	private PBMessage packet;

	public CmdTask(NetMsg netMsg, ChannelHandlerContext channel, PBMessage packet)
	{
		this.netMsg = netMsg;
		this.channel = channel;
		this.packet = packet;
	}

	@Override
	public void run()
	{
		try
		{
			netMsg.execute(channel, packet);
		}
		catch (Exception e)
		{
			Log.error("执行 command 异常, command : " + netMsg.toString() + ", packet : " + packet.toString(), e);
		}
	}
}
