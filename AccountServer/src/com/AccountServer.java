package com;

import com.db.DBPoolMgr;
import com.game.NetMsgMgr;
import com.game.PBDecoder;
import com.game.PBEncoder;
import com.game.TimerTaskMgr;
import com.util.GameLog;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.timeout.IdleStateHandler;

public class AccountServer extends BaseServer
{
	private static AccountServer accountServer = new AccountServer();

	public static AccountServer getInstance()
	{
		return accountServer;
	}

	public boolean init()
	{
		// PropertyConfigurator.configure(Config.getPath("log4j.path")); //
		// 初始化log日志
		if (!initComponent(NetMsgMgr.getInstance().init(), "网络消息协议"))
		{
			return false;
		}

		initNet();
		// NetConfig.getInstance().init();
		DBPoolMgr.getInstaqnce().initMainDB(BaseServer.serverCfgInfo.mainDb);
		TimerTaskMgr.init();
		return true;
	}

	private boolean initNet()
	{
		EventLoopGroup bossGroup = new NioEventLoopGroup();
		EventLoopGroup workerGroup = new NioEventLoopGroup();
		int netPort = 1001;
		try
		{
			ServerBootstrap b = new ServerBootstrap();
			b.group(bossGroup, workerGroup).channel(NioServerSocketChannel.class).handler(new LoggingHandler(LogLevel.INFO))
					.childHandler(new ChannelInitializer<SocketChannel>()
					{
						@Override
						public void initChannel(SocketChannel ch) throws Exception
						{
							ChannelPipeline p = ch.pipeline();
							p.addLast(new LoggingHandler(LogLevel.INFO));
							p.addLast(new PBDecoder());
							p.addLast(new PBEncoder());
							p.addLast(new IdleStateHandler(60 * 2, 0, 0));
							p.addLast(new AccountServerHandler());
						}
					});
			GameLog.info("启动登录服务器成功, port : " + netPort);
			Channel ch = b.bind(netPort).sync().channel();
			ch.closeFuture().sync();
		}
		catch (Exception e)
		{
			GameLog.error("启动登录服务器失败, port : " + netPort, e);
		}
		finally
		{
			workerGroup.shutdownGracefully();
			bossGroup.shutdownGracefully();
		}
		return true;
	}

	public boolean stop()
	{
		setTerminate(true);
		System.exit(0);
		return true;
	}

	public static void main(String[] args)
	{
		if (args.length < 1)
		{
			System.err.println("请输入配置文件地址路径");
			return;
		}

		configPath = args[0];
		if (!AccountServer.getInstance().init())
		{
			GameLog.error("AccountServer启动失败!");
			System.exit(1);
		}
		GameLog.info("AccountServer启动成功!");
	}
}
