package com.star.light.scene;

import com.star.light.player.GamePlayer;

/**
 * 场景玩家
 */
public class ScenePlayer {
	/**
	 * 玩家自身
	 */
	public GamePlayer gamePlayer;
	
	/**
	 * 玩家的X坐标位置
	 */
	public float posX;
	
	/**
	 * 玩家的Y坐标位置
	 */
	public float posY;
	
	/**
	 * 玩家的Z坐标位置
	 */
	public float posZ;
	
	/**
	 * 玩家的方向
	 */
	public float direct;
	
	public ScenePlayer(GamePlayer player, float posX, float posY, float posZ, float direct) {
		this.gamePlayer = player;
		this.posX = posX;
		this.posY = posY;
		this.posZ = posZ;
		this.direct = direct;
	}
}
