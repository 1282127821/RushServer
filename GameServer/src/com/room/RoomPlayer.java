package com.room;

import com.player.GamePlayer;

public class RoomPlayer {
	/**
	 * 玩家
	 */
	public GamePlayer player;

	/**
	 * 玩家下标
	 */
	public int roomIndex;

	/**
	 * 阵营
	 */
	public int roomCamp;
	
	/**
	 * 玩家的PVPId
	 */
	public int pvpId;
	
	/**
	 * 玩家的战斗状态
	 */
	public short pvpState;
	
	/**
	 * 玩家的血量
	 */
	public int playerHP;
	
	/**
	 * 玩家的护盾值
	 */
	public int playerShieldHP;
	
	/**
	 * 玩家是否死亡 true表示死亡  false表示活着
	 */
	public boolean isDead;
	
	/**
	 * 是否可被攻击
	 */
	public boolean isCanAttack; 
}
