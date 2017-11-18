package com;

import com.netmsg.NetCmd;
import com.netmsg.NetMsgMgr;
import com.netmsg.PBMessage;
import com.player.GamePlayer;
import com.player.WorldMgr;
import com.util.Log;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;

public class GameServerHandler extends ChannelInboundHandlerAdapter
{
	private static final NetMsgMgr netInstance = NetMsgMgr.getInstance();

	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception
	{
		Log.info("有新的客户端连接进来，客户端的IP地址为:   " + ctx.channel().remoteAddress());
		ctx.fireChannelActive();
	}

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception
	{
		PBMessage packet = (PBMessage) msg;
		// TODO:LZGLZG网络消息协议应该在网关层被截获，能够到这里的必然是已有的
		NetCmd netCmd = netInstance.getNetCmd(packet.getMsgId());
		long userId = 0;// packet.getUserId();
		if (netCmd == null)
		{
			Log.error("not found cmd , code: 0x" + Integer.toHexString(packet.getMsgId()) + " , userId : " + userId);
			return;
		}

		if (userId > 0)
		{
			try
			{
				GamePlayer player = WorldMgr.getOnlinePlayer(userId);
				if (player == null)
				{
					player = WorldMgr.getPlayer(userId);
					if (player == null)
					{
						Log.error("code " + packet.getMsgId() + " not found player " + userId + ",can not continue execute.");
						return;
					}
				}
				player.enqueue(netCmd, packet);
			}
			catch (Exception e)
			{
				Log.error("packet has exception. " + packet.headerToStr(), e);
			}
		}
		else
		{
			// executor.enDefaultQueue(netCmd, packet);
			// GatewayLinkMgr.getInstance().addGameLinkedClient(ctx.channel(),
			// packet);
		}
	}

	@Override
	public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception
	{
		/* 心跳处理 */
		if (evt instanceof IdleStateEvent)
		{
			IdleStateEvent event = (IdleStateEvent) evt;
			if (event.state() == IdleState.READER_IDLE)
			{
				/* 读超时 */
				Log.info("READER_IDLE 读超时");
				ctx.disconnect();
			}
			else if (event.state() == IdleState.WRITER_IDLE)
			{
				/* 写超时 */
				Log.info("WRITER_IDLE 写超时");
			}
			else if (event.state() == IdleState.ALL_IDLE)
			{
				/* 总超时 */
				Log.info("ALL_IDLE 总超时");
			}
		}
	}

	@Override
	public void channelReadComplete(ChannelHandlerContext ctx) throws Exception
	{
		ctx.flush();
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
	{
		cause.printStackTrace();
		Log.error(toMessage("caught exception that close the connection to gateway disconnected. session : " + ctx.channel().remoteAddress().toString()),
				cause);
		ctx.close();
	}

	@Override
	public void channelInactive(ChannelHandlerContext ctx)
	{
		Log.info("客户端断开连接，客户端的IP地址为:   " + ctx.channel().remoteAddress().toString());
		ctx.close();

		// if (session.getAttribute(LinkedClient.KEY_CLIENT) != null) {
		// GatewayLinkMgr.getInstance().removeLinkedClient(session);
		// GameLog.error(toMessage("close the connection to gateway
		// disconnected. session : " + session.getRemoteAddress().toString()));
		// }
	}

	private String toMessage(String msg)
	{
		return "GameServerHandler " + msg;
	}
}
