package com;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

import com.game.AccountMgr;
import com.game.CmdTask;
import com.game.NetMsg;
import com.game.NetMsgMgr;
import com.game.PBMessage;
import com.util.Log;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.util.AttributeKey;

@Sharable
public class LoginServerHandler extends SimpleChannelInboundHandler<PBMessage> {
	private static final AttributeKey<Long> CHANNEL_ID = AttributeKey.valueOf("ChannelId");
	private static final AtomicLong PLAYER_SESSION = new AtomicLong(1000);

	private static LinkedBlockingQueue<Runnable> workQueue = new LinkedBlockingQueue<Runnable>();
	private static RejectedExecutionHandler handler = new ThreadPoolExecutor.DiscardPolicy();
	private static ThreadPoolExecutor pool = new ThreadPoolExecutor(4, 8, 5, TimeUnit.MINUTES, workQueue, handler);

	private static final NetMsgMgr netInstance = NetMsgMgr.getInstance();

	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		Channel channel = ctx.channel();
		Log.info("有新的客户端连接进来，客户端的IP地址为:   " + channel.remoteAddress());
		long channelId = PLAYER_SESSION.getAndIncrement();
		channel.attr(CHANNEL_ID).set(channelId);
		AccountMgr.getInstance().addChannel(channelId, channel);
		ctx.fireChannelActive();
	}

	@Override
	public void channelRead0(ChannelHandlerContext ctx, PBMessage packet) throws Exception {
		short msgId = packet.getMsgId();
		NetMsg netMsg = netInstance.getNetMsg(msgId);
		if (netMsg == null) {
			Log.error("not found cmd , code: 0x" + Integer.toHexString(msgId));
			return;
		}

		try {
			pool.execute(new CmdTask(netMsg, ctx, packet));
		} catch (Exception e) {
			e.printStackTrace();
			Log.error("msgId:  " + msgId + ", 执行出现异常:", e);
		}
	}

	@Override
	public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
		/* 心跳处理 */
		if (evt instanceof IdleStateEvent) {
			IdleStateEvent event = (IdleStateEvent) evt;
			if (event.state() == IdleState.READER_IDLE) {
				/* 读超时 */
				Log.info("READER_IDLE 读超时");
				ctx.disconnect();
			} else if (event.state() == IdleState.WRITER_IDLE) {
				/* 写超时 */
				Log.info("WRITER_IDLE 写超时");
			} else if (event.state() == IdleState.ALL_IDLE) {
				/* 总超时 */
				Log.info("ALL_IDLE 总超时");
			}
		}
	}

	@Override
	public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
		ctx.flush();
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
		cause.printStackTrace();
		ctx.close();
	}

	@Override
	public void channelInactive(ChannelHandlerContext ctx) {
		Log.info("客户端断开连接，客户端的IP地址为:   " + ctx.channel().remoteAddress());
		ctx.close();
	}
}