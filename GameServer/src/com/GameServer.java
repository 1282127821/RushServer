package com;

import java.time.Clock;

import com.db.DBPoolMgr;
import com.guild.GuildMgr;
import com.netmsg.NetMsgMgr;
import com.network.PBDecoder;
import com.network.PBEncoder;
import com.player.DaoMgr;
import com.player.LoginMgr;
import com.player.WorldMgr;
import com.room.RoomMgr;
import com.schedule.MinListenMgr;
import com.schedule.TimerTaskMgr;
import com.table.BuffInfoMgr;
import com.table.CharacterInfoMgr;
import com.table.ConfigMgr;
import com.table.EquipAdvanceInfoMgr;
import com.table.EquipBlessEffectInfoMgr;
import com.table.EquipBlessInfoMgr;
import com.table.EquipForbiddenInfoMgr;
import com.table.EquipInlayInfoMgr;
import com.table.ExpInfoMgr;
import com.table.GuildLevelInfoMgr;
import com.table.ItemTemplateMgr;
import com.table.LevelAttributeInfoMgr;
import com.table.LevelStageInfoMgr;
import com.table.MainTaskInfoMgr;
import com.table.RewardInfoMgr;
import com.table.SkillTemplateMgr;
import com.util.DirtyData;
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

public final class GameServer extends BaseServer
{
	private static GameServer gameServer = new GameServer();
	public static boolean gmIsOpen = false;

	public static GameServer getInstance()
	{
		return gameServer;
	}

	private boolean loadMemoryData()
	{
		LoginMgr.init();
		DaoMgr.init();
		GuildMgr.getInstance().loadAllGuild();
		return true;
	}

	/**
	 * 重新加载所有的策划配置
	 */
	public boolean reloadConfigData(String tname)
	{
		return initConfigData(tname);
	}

	/**
	 * 读取指定文件需传入该文件名
	 */
	public boolean initConfigData(String tname)
	{
		try
		{
			String designPath = serverCfgInfo.designcfgPath;
			if (!initComponent(ConfigMgr.getInstance().load(designPath + "t_s_config"), "Config配置数据"))
			{
				return false;
			}

			if (!initComponent(ItemTemplateMgr.getInstance().load(designPath + "t_s_items"), "物品数据"))
			{
				return false;
			}

			if (!initComponent(LevelStageInfoMgr.getInstance().load(designPath + "t_s_stage"), "关卡数据"))
			{
				return false;
			}

			if (!initComponent(RewardInfoMgr.getInstance().load(designPath + "t_s_drop"), "通用奖励掉落"))
			{
				return false;
			}

			if (!initComponent(SkillTemplateMgr.getInstance().load(designPath + "t_s_skill"), "主动技能信息"))
			{
				return false;
			}

			if (!initComponent(SkillTemplateMgr.getInstance().loadPassiveSkill(designPath + "t_s_passiveskill"), "被动技能信息"))
			{
				return false;
			}

			if (!initComponent(ExpInfoMgr.getInstance().load(designPath + "t_s_actorexp"), "经验信息表"))
			{
				return false;
			}

			if (!initComponent(LevelAttributeInfoMgr.getInstance().load(designPath + "t_s_jobprofile"), "等级属性表"))
			{
				return false;
			}

			if (!initComponent(MainTaskInfoMgr.getInstance().load(designPath + "t_s_mission"), "主线任务表"))
			{
				return false;
			}

			if (!initComponent(CharacterInfoMgr.getInstance().load(designPath + "t_s_character"), "角色表"))
			{
				return false;
			}

			if (!initComponent(BuffInfoMgr.getInstance().load(designPath + "t_s_buffData"), "Buff表"))
			{
				return false;
			}

			if (!initComponent(GuildLevelInfoMgr.getInstance().load(designPath + "t_s_clanlevel"), "公会等级经验表"))
			{
				return false;
			}

			if (!initComponent(EquipAdvanceInfoMgr.getInstance().load(designPath + "t_s_advancematerial"), "装备进阶表表"))
			{
				return false;
			}

			if (!initComponent(EquipInlayInfoMgr.getInstance().load(designPath + "t_s_setpart"), "装备镶嵌表"))
			{
				return false;
			}

			if (!initComponent(EquipForbiddenInfoMgr.getInstance().load(designPath + "t_s_eqpworkingfbd"), "装备禁用表"))
			{
				return false;
			}

			if (!initComponent(EquipBlessInfoMgr.getInstance().load(designPath + "t_s_blessmaterial"), "装备祝福材料表"))
			{
				return false;
			}

			if (!initComponent(EquipBlessEffectInfoMgr.getInstance().load(designPath + "t_s_blesseffect"), "装备祝福效果表"))
			{
				return false;
			}

			DirtyData.init(designPath + "dirty.txt");
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return false;
		}
		return true;
	}

	private boolean initNetwork()
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
							p.addLast(new GameServerHandler());
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

	@Override
	public boolean start(String configPath)
	{
		if (!super.start(configPath))
		{
			return false;
		}

		if (!initComponent(NetMsgMgr.getInstance().load(), "网络消息协议"))
		{
			return false;
		}

		gmIsOpen = serverCfgInfo.isGM;

		DBPoolMgr.getInstance().initDBPool(serverCfgInfo.mainDb);

		if (!initConfigData(""))
		{
			return false;
		}

		if (!loadMemoryData())
		{
			return false;
		}

		if (!initNetwork())
		{
			return false;
		}

		TimerTaskMgr.init();
		MinListenMgr.getInstance().init();
		setTerminate(false);
		return true;
	}

	public boolean stop()
	{
		WorldMgr.save();
		RoomMgr.getInstance().stop();
		MinListenMgr.getInstance().stop();
		return true;
	}

	public static void main(String[] args)
	{
		String configPath = "../Lib/server.json";
		if (args.length > 1)
		{
			configPath = args[0];
		}

		long time = Clock.systemDefaultZone().millis();
		BaseServer gameServer = GameServer.getInstance();
		if (!gameServer.start(configPath))
		{
			GameLog.error("GameServer启动失败!");
			System.exit(1);
		}

		GameLog.info("GameServer启动成功，启动耗时: " + (Clock.systemDefaultZone().millis() - time));
	}
}

class ServerShutdownHook extends Thread
{
	@Override
	public void run()
	{
		GameLog.info("ServerShutDown hook is running");
		GameServer.getInstance().stop();
		GameLog.info("ServerShutDown hook was run");
	}
}
