package com.netmsg.scene;

import com.netmsg.NetCmd;
import com.netmsg.PBMessage;
import com.pbmessage.GamePBMsg.EnterSceneMsg;
import com.player.GamePlayer;
import com.scene.SceneMgr;
import com.scene.SceneType;
import com.team.Team;

public class EnterScene implements NetCmd {

	public void execute(GamePlayer player, PBMessage packet) throws Exception {
		EnterSceneMsg netMsg = EnterSceneMsg.parseFrom(packet.getMsgBody());
		int sceneType = netMsg.getSceneType();
		player.beforeSceneType = sceneType;
		Team team = player.getTeam();
		if (sceneType == SceneType.MAIN_CITY) {
			player.sceneType = SceneType.MAIN_CITY;
			SceneMgr.getInstance().enterScene(player, netMsg.getPosX(), netMsg.getPosY(), netMsg.getPosZ(),
					netMsg.getDirect());
		} else if (team != null) {
			player.sceneType = SceneType.TEAM;
			SceneMgr.getInstance().enterTeamScene(player, netMsg.getPosX(), netMsg.getPosY(), netMsg.getPosZ(),
					netMsg.getDirect());
		} else if (sceneType == SceneType.LOUNGE) {
			player.sceneType = SceneType.LOUNGE;
		}

		// 修改队伍状态
		if (team != null && team.isLeader(player.getUserId())) {
			team.setTeamState(Team.TeamState.ts_wait);
		}
	}
}
