package com.star.light;

import java.time.Clock;

import com.star.light.db.DBPoolMgr;
import com.star.light.guild.GuildMgr;
import com.star.light.player.DaoMgr;
import com.star.light.player.LoginMgr;
import com.star.light.player.WorldMgr;
import com.star.light.room.RoomMgr;
import com.star.light.schedule.MinListenMgr;
import com.star.light.schedule.TimerTaskMgr;
import com.star.light.socket.cmd.NetMsgMgr;
import com.star.light.table.BuffInfoMgr;
import com.star.light.table.CharacterInfoMgr;
import com.star.light.table.ConfigMgr;
import com.star.light.table.DirtyData;
import com.star.light.table.EquipAdvanceInfoMgr;
import com.star.light.table.EquipBlessEffectInfoMgr;
import com.star.light.table.EquipBlessInfoMgr;
import com.star.light.table.EquipForbiddenInfoMgr;
import com.star.light.table.EquipInlayInfoMgr;
import com.star.light.table.ExpInfoMgr;
import com.star.light.table.GuildLevelInfoMgr;
import com.star.light.table.ItemTemplateMgr;
import com.star.light.table.LevelAttributeInfoMgr;
import com.star.light.table.LevelStageInfoMgr;
import com.star.light.table.MainTaskInfoMgr;
import com.star.light.table.RewardInfoMgr;
import com.star.light.table.SkillTemplateMgr;
import com.star.light.util.GameLog;
import com.star.light.webservice.server.CastleWS;
import com.star.light.webservice.server.YiShiWSServer;

public final class GameServer extends BaseServer {
	private static GameServer gameServer = new GameServer();
	public static boolean gmIsOpen = false;

	public static GameServer getInstance() {
		return gameServer;
	}

	private boolean loadMemoryData() {
		LoginMgr.init();
		DaoMgr.init();
		GuildMgr.getInstance().loadAllGuild();
		return true;
	}

	/**
	 * 重新加载所有的策划配置
	 */
	public boolean reloadConfigData(String tname) {
		return initConfigData(tname);
	}

	/**
	 * 读取指定文件需传入该文件名
	 */
	public boolean initConfigData(String tname) {
		try {
			String designPath = BaseServer.serverCfgInfo.designcfg;
			if (!initComponent(ConfigMgr.getInstance().load(designPath + "t_s_config"), "Config配置数据")) {
				return false;
			}

			if (!initComponent(ItemTemplateMgr.getInstance().load(designPath + "t_s_items"), "物品数据")) {
				return false;
			}

			if (!initComponent(LevelStageInfoMgr.getInstance().load(designPath + "t_s_stage"), "关卡数据")) {
				return false;
			}

			if (!initComponent(RewardInfoMgr.getInstance().load(designPath + "t_s_drop"), "通用奖励掉落")) {
				return false;
			}

			if (!initComponent(SkillTemplateMgr.getInstance().load(designPath + "t_s_skill"), "主动技能信息")) {
				return false;
			}
				
			if (!initComponent(SkillTemplateMgr.getInstance().loadPassiveSkill(designPath + "t_s_passiveskill"), "被动技能信息")) {
				return false;
			}
				
			if (!initComponent(ExpInfoMgr.getInstance().load(designPath + "t_s_actorexp"), "经验信息表")) {
				return false;
			}
				
			if (!initComponent(LevelAttributeInfoMgr.getInstance().load(designPath + "t_s_jobprofile"), "等级属性表")) {
				return false;
			}
				
			if (!initComponent(MainTaskInfoMgr.getInstance().load(designPath + "t_s_mission"), "主线任务表")) {
				return false;
			}
				
			if (!initComponent(CharacterInfoMgr.getInstance().load(designPath + "t_s_character"), "角色表")) {
				return false;
			}
				
			if (!initComponent(BuffInfoMgr.getInstance().load(designPath + "t_s_buffData"), "Buff表")) {
				return false;
			}
				
			if (!initComponent(GuildLevelInfoMgr.getInstance().load(designPath + "t_s_clanlevel"), "公会等级经验表")) {
				return false;
			}
				
			if (!initComponent(EquipAdvanceInfoMgr.getInstance().load(designPath + "t_s_advancematerial"), "装备进阶表表")) {
				return false;
			}
				
			if (!initComponent(EquipInlayInfoMgr.getInstance().load(designPath + "t_s_setpart"), "装备镶嵌表")) {
				return false;
			}
				
			if (!initComponent(EquipForbiddenInfoMgr.getInstance().load(designPath + "t_s_eqpworkingfbd"), "装备禁用表")) {
				return false;
			}
				
			if (!initComponent(EquipBlessInfoMgr.getInstance().load(designPath + "t_s_blessmaterial"), "装备祝福材料表")) {
				return false;
			}
				
			if (!initComponent(EquipBlessEffectInfoMgr.getInstance().load(designPath + "t_s_blesseffect"), "装备祝福效果表")) {
				return false;
			}
				
			DirtyData.getInstance().initDirty(designPath + "dirty.txt");
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	@Override
	public boolean start() {
		if (!super.start()) {
			return false;
		}
		
		if (!initComponent(NetMsgMgr.getInstance().load(), "网络消息协议")) {
			return false;
		}
		
		int gmState = BaseServer.serverCfgInfo.isGM;
		if (gmState == 1) {
			gmIsOpen = true;
		}

		DBPoolMgr.getInstaqnce().initMainDB(BaseServer.serverCfgInfo.mainDb);
		DBPoolMgr.getInstaqnce().initLogDB(BaseServer.serverCfgInfo.logDb);

		if (!initConfigData("")) {
			return false;
		}

		if (!loadMemoryData()) {
			return false;
		}

		if (!initMina(new GameServerHandler(), BaseServer.serverCfgInfo.gameServer)) {
			return false;
		}

		YiShiWSServer.getInstance().start();
		TimerTaskMgr.init();
		MinListenMgr.getInstance().init();
		setTerminate(false);
		return true;
	}

	@Override
	public boolean stop() {
		CastleWS.isClose = true;
		WorldMgr.save();
		RoomMgr.getInstance().stop();
		MinListenMgr.getInstance().stop();
		return super.stop();
	}
	
	public static void main(String[] args) {
		if (args.length <= 0) {
			System.err.println("请输入配置文件地址路径...");
			return;
		}
		
		// TODO:LZGLZG 这里考虑使用相对路径的读取文件,不在使用
		//./Lib/deploy_config/CastleServerConfig.properties
		//./Lib/deploy_config/GatewayServerConfig.properties
		long time = Clock.systemDefaultZone().millis();
		configPath = args[0];
		BaseServer gameServer = GameServer.getInstance();
		if (!gameServer.start()) {
			GameLog.error("GameServer启动失败!");
			System.exit(1);
		}

		GameLog.info("GameServer启动成功，启动耗时: " + (Clock.systemDefaultZone().millis() - time));
	}
}

class ServerShutdownHook extends Thread {
	@Override
	public void run() {
		GameLog.info("ServerShutDown hook is running");
		GameServer.getInstance().stop();
		GameLog.info("ServerShutDown hook was run");
	}
}
