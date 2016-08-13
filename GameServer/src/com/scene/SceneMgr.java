package com.scene;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.pbmessage.GamePBMsg.MainCityMoveMsg;
import com.player.GamePlayer;

public final class SceneMgr {
	private Map<Integer, Scene> sceneMap = new ConcurrentHashMap<Integer, Scene>();
	private Map<Long, Scene> teamSceneMap = new ConcurrentHashMap<Long, Scene>();
	private static SceneMgr instance = new SceneMgr();

	public static SceneMgr getInstance() {
		return instance;
	}

	public void enterScene(GamePlayer player, float posX, float posY, float posZ, float direct) {
		int sceneType = player.sceneType;
		if (!sceneMap.containsKey(sceneType)) {
			sceneMap.put(sceneType, new Scene(sceneType));
		}

		Scene scene = sceneMap.get(sceneType);
		scene.enterScene(player, posX, posY, posZ, direct);
	}

	public void enterTeamScene(GamePlayer player, float posX, float posY, float posZ, float direct) {
		long teamId = player.getTeamId();
		if (!teamSceneMap.containsKey(teamId)) {
			teamSceneMap.put(teamId, new Scene(SceneType.TEAM));
		}

		Scene scene = teamSceneMap.get(teamId);
		posX = -7.0f;
		posY = -0.0f;
		posZ = -7.0f;
		scene.enterScene(player, posX, posY, posZ, direct);
	}

	/**
	 * 玩家在主城内移动
	 */
	public void playerMove(GamePlayer player, MainCityMoveMsg.Builder netMsg) {
		Scene scene = null;
		if (player.sceneType == SceneType.MAIN_CITY) {
			scene = sceneMap.get(player.sceneType);
		} else {
			scene = teamSceneMap.get(player.getTeamId());
		}
		
		if (scene != null) 
			scene.playerMove(player, netMsg);
	}

	/**
	 * 玩家离开场景
	 */
	public void playerLeave(GamePlayer player) {
		Scene scene = null;
		if (player.sceneType == SceneType.MAIN_CITY) {
			scene = sceneMap.get(player.sceneType);
		} else {
			scene = teamSceneMap.get(player.getTeamId());
		}
		
		if (scene != null) {
			scene.playerLeave(player);
			player.sceneType = player.beforeSceneType;
		}
	}
	
	public void removeScene(Scene scene) {
		if (scene.getSceneType() == SceneType.TEAM) {
			scene = teamSceneMap.remove(scene);
		}
	}
}