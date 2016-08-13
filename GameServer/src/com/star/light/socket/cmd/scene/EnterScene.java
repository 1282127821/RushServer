package com.star.light.socket.cmd.scene;

import com.star.light.player.GamePlayer;
import com.star.light.scene.SceneMgr;
import com.star.light.scene.SceneType;
import com.star.light.socket.PBMessage;
import com.star.light.socket.cmd.NetCmd;
import com.star.light.team.Team;

import tbgame.pbmessage.GamePBMsg.EnterSceneMsg;

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
