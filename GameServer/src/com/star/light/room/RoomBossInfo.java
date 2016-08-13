package com.star.light.room;

public class RoomBossInfo {
	/**
	 * BOSS的Id
	 */
	public int bossId;

	/**
	 * BOSS的阵营
	 */
	public int camp;

	/**
	 * BOSS的PVPId
	 */
	public int pvpId;

	/**
	 * BOSS节点Id
	 */
	public int nodeId;

	/**
	 * BOSS的血量
	 */
	public int bossHP;

	/**
	 * BOSS的归属玩家Id
	 */
	public long bossMasterId;

	/**
	 * BOSS是否死亡 true表示死亡 false表示活着
	 */
	public boolean isDead;
}
