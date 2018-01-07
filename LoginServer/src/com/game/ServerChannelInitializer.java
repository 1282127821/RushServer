package com.game;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;

/**
 * 服务器连接通道初始化<br>
 * 服务器间连接通讯后进行加密器和解密器等对象初始化.
 */
public class ServerChannelInitializer extends ChannelInitializer<SocketChannel> {
	private ChannelHandler handler;

	public ServerChannelInitializer(ChannelHandler handler) {
		this.handler = handler;
	}

	@Override
	protected void initChannel(SocketChannel ch) throws Exception {
		// 整个pipeline是同步处理，类似mina使用orderedthreadpoolexecutor线程池
		ChannelPipeline pipeline = ch.pipeline();
		pipeline.addLast("decoder", new PBDecoder());// 必须new,内含缓存，不可共享
		pipeline.addLast("encoder", new PBEncoder());
		pipeline.addLast("handler", handler);
	}
}
