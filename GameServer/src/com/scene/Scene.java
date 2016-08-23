package com.scene;

import java.util.ArrayList;
import java.util.List;

import com.netmsg.PBMessage;
import com.pbmessage.GamePBMsg.LeaveMainCityMsg;
import com.pbmessage.GamePBMsg.MainCityMoveMsg;
import com.pbmessage.GamePBMsg.PlayerMsg;
import com.pbmessage.GamePBMsg.ScenePlayerInfoMsg;
import com.pbmessage.GamePBMsg.ScenePlayerListMsg;
import com.player.GamePlayer;
import com.player.PlayerInfo;
import com.protocol.Protocol;

public class Scene {
	/**
	 * 场景类型
	 */
	private int sceneType;

	/**
	 * 场景玩家列表
	 */
	private List<ScenePlayer> scenePlayerList;

	public Scene(int sceneType) {
		scenePlayerList = new ArrayList<ScenePlayer>();
		this.sceneType = sceneType;
	}

	public int getSceneType() {
		return sceneType;
	}

	public void addScenePlayer(ScenePlayer scenePlayer) {

	}

	public ScenePlayer getPlayerFromScene(long userId) {
		for (ScenePlayer player : scenePlayerList) {
			if (player.gamePlayer.getUserId() == userId) {
				return player;
			}
		}

		return null;
	}

	public void delPlayer(long userId) {
		ScenePlayer scenePlayer = getPlayerFromScene(userId);
		if (scenePlayer != null) {
			scenePlayerList.remove(scenePlayer);
			if (scenePlayerList.size() == 0) {
				SceneMgr.getInstance().removeScene(this);
			}
		}
	}
	
	/**
	 * 玩家进入场景
	 */
	public void enterScene(GamePlayer player, float posX, float posY, float posZ, float direct) 
	{
		long userId = player.getUserId();
		if (getPlayerFromScene(userId) != null)
		{
			return;
		}
		
		ScenePlayerInfoMsg.Builder selfScenePlayerMsg = ScenePlayerInfoMsg.newBuilder();
		PlayerMsg.Builder selfMsg = PlayerMsg.newBuilder();
		player.initPlayerMsg(selfMsg, true);
		selfScenePlayerMsg.setPlayerMsg(selfMsg);
		PlayerInfo playerInfo = player.playerInfo;
		player.sceneType = sceneType;
		playerInfo.setPosX(posX);
		playerInfo.setPosY(posY);
		playerInfo.setPosZ(posZ);
		playerInfo.setDirect(direct);
		selfScenePlayerMsg.setPosX(posX);
		selfScenePlayerMsg.setPosY(posY);
		selfScenePlayerMsg.setPosZ(posZ);
		selfScenePlayerMsg.setDirect(direct);
		ScenePlayerListMsg.Builder otherScenePlayerListMsg = ScenePlayerListMsg.newBuilder();
		for (ScenePlayer scenePlayer : scenePlayerList)
		{
			GamePlayer mainCityPlayer = scenePlayer.gamePlayer;
			if (mainCityPlayer != null)
			{
				ScenePlayerInfoMsg.Builder otherScenePlayerMsg = ScenePlayerInfoMsg.newBuilder();
				PlayerMsg.Builder otherMsg = PlayerMsg.newBuilder();
				mainCityPlayer.initPlayerMsg(otherMsg, true);
				otherScenePlayerMsg.setPlayerMsg(otherMsg);
				otherScenePlayerMsg.setPosX(scenePlayer.posX);
				otherScenePlayerMsg.setPosY(scenePlayer.posY);
				otherScenePlayerMsg.setPosZ(scenePlayer.posZ);
				otherScenePlayerMsg.setDirect(scenePlayer.direct);
				otherScenePlayerListMsg.addPlayerList(otherScenePlayerMsg);
				mainCityPlayer.sendPacket(Protocol.S_C_PLAYER_ENTER_MAIN_CITY, selfScenePlayerMsg);
			}
		}
		
		ScenePlayer scenePlayer = new ScenePlayer(player, posX, posY, posZ, playerInfo.getDirect());
		scenePlayerList.add(scenePlayer);
		player.sendPacket(Protocol.S_C_SYNC_MAIN_CITY_PLAYER, otherScenePlayerListMsg);
	}
	
	/**
	 * 场景消息同步管理，根据userId来决定是否不给该玩家发送同步消息。
	 */
	public void sendSyncMsg(long userId, short code, PBMessage netMsg) {
		for (ScenePlayer scenePlayer : scenePlayerList) {
			if (scenePlayer == null) {
				continue;
			}
			
			GamePlayer gamePlayer = scenePlayer.gamePlayer;
			if (gamePlayer.getUserId() != userId) {
				netMsg.setMsgId(code);
				gamePlayer.sendPacket(netMsg);
			}
		}
	}

	/**
	 * 玩家在场景内移动
	 */
	public void playerMove(GamePlayer player, MainCityMoveMsg.Builder netMsg)
	{
		for (ScenePlayer playerInfo : scenePlayerList)
		{
			GamePlayer sceneplayer = playerInfo.gamePlayer;
			if (sceneplayer != null && sceneplayer.getUserId() != player.getUserId())
			{
				sceneplayer.sendPacket(Protocol.S_C_MAIN_CITY_MOVE, netMsg);
			}
		}
	}
	
	/**
	 * 玩家离开场景
	 */
	public void playerLeave(GamePlayer player) 
	{
		LeaveMainCityMsg.Builder netMsg = LeaveMainCityMsg.newBuilder();
		long userId = player.getUserId();
		netMsg.setUserId(userId);
		for (ScenePlayer playerInfo : scenePlayerList)
		{
			GamePlayer gamePlayer = playerInfo.gamePlayer;
			if (gamePlayer != null && gamePlayer.getUserId() != player.getUserId())
			{
				gamePlayer.sendPacket(Protocol.S_C_PLAYER_LEAVE_MAIN_CITY, netMsg);
				LeaveMainCityMsg.Builder otherNetMsg = LeaveMainCityMsg.newBuilder();
				otherNetMsg.setUserId(gamePlayer.getUserId());
				player.sendPacket(Protocol.S_C_PLAYER_LEAVE_MAIN_CITY, otherNetMsg);
			}
		}
		
		delPlayer(userId);
	}
}
