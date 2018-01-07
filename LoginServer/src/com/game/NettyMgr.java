package com.game;

import java.net.InetSocketAddress;

import com.util.Log;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.WriteBufferWaterMark;
import io.netty.channel.epoll.Epoll;
import io.netty.channel.epoll.EpollEventLoopGroup;
import io.netty.channel.epoll.EpollServerSocketChannel;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

/**
 * netty管理器<br>
 * 用于快速启动netty服务.
 */
public class NettyMgr {
	private static ChannelFuture channelFuture;
	private static EventLoopGroup bossGroup;
	private static EventLoopGroup workGroup;

	public static boolean start(String address, int port, ChannelHandler handler) {
		boolean isEpoll = Epoll.isAvailable();
		int cpuNum = Runtime.getRuntime().availableProcessors();
		if (isEpoll) {
			bossGroup = new EpollEventLoopGroup(cpuNum);
			workGroup = new EpollEventLoopGroup(cpuNum * 2 + 1);
		} else {
			bossGroup = new NioEventLoopGroup(cpuNum);
			workGroup = new NioEventLoopGroup(cpuNum * 2 + 1);
		}

		try {
			ServerBootstrap bootstrap = new ServerBootstrap();
			bootstrap.group(bossGroup, workGroup);
			bootstrap.channel(isEpoll ? EpollServerSocketChannel.class : NioServerSocketChannel.class);
			bootstrap.childHandler(handler);

			// TIME_WAIT时可重用端口，服务器关闭后可立即重启，此时任何非期
			// 望数据到达，都可能导致服务程序反应混乱，不过这只是一种可能，事实上很不可能
			bootstrap.option(ChannelOption.SO_REUSEADDR, true);
			// 设置了ServerSocket类的SO_RCVBUF选项，就相当于设置了Socket对象的接收缓冲区大小，4KB
			bootstrap.option(ChannelOption.SO_RCVBUF, 1024 * 8);
			// 请求连接的最大队列长度，如果backlog参数的值大于操作系统限定的队列的最大长度，那么backlog参数无效
			bootstrap.option(ChannelOption.SO_BACKLOG, 128);

			// 使用内存池的缓冲区重用机制
			bootstrap.option(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT);

			// 当客户端发生断网或断电等非正常断开的现象，如果服务器没有设置SO_KEEPALIVE选项，则会一直不关闭SOCKET。具体的时间由OS配置
			bootstrap.childOption(ChannelOption.SO_KEEPALIVE, true);
			// 在调用close方法后，将阻塞n秒，让未完成发送的数据尽量发出，netty中这部分操作调用方法异步进行。我们的游戏业务没有这种需要，所以设置为0
			bootstrap.childOption(ChannelOption.SO_LINGER, 0);
			// 数据包不缓冲,立即发出
			bootstrap.childOption(ChannelOption.TCP_NODELAY, true);
			// 发送缓冲大小，默认8192
			bootstrap.childOption(ChannelOption.SO_SNDBUF, 1024 * 8);
			WriteBufferWaterMark writeMark = new WriteBufferWaterMark(1024 * 64, 1024 * 128);
			bootstrap.childOption(ChannelOption.WRITE_BUFFER_WATER_MARK, writeMark);
			channelFuture = bootstrap.bind(new InetSocketAddress(address, port));
			channelFuture.sync();

			Log.info(
					"Socket service start success address : " + address + "， port :" + port + ", isEpoll = " + isEpoll);
			return true;
		} catch (Exception e) {
			Log.error("init netty exception address = " + address + ", port = " + port, e);
			return false;
		}
	}

	public static void stop() {
		try {
			channelFuture.channel().close().syncUninterruptibly();
			channelFuture.channel().closeFuture().syncUninterruptibly();
			bossGroup.shutdownGracefully().syncUninterruptibly();
			workGroup.shutdownGracefully().syncUninterruptibly();
		} catch (Exception e) {
			Log.error("Netty stop exception : ", e);
		}
	}
}
