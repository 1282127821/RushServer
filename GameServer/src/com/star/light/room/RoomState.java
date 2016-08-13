package com.star.light.room;

public interface RoomState {
	/**
	 * 未使用中
	 */
	int UNUSE = 1;
	
	/**
	 * 使用中
	 */
	int USEING = 2;
	
	/**
	 * 撮合中
	 */
	int COMPETEING = 3;
	
	/**
	 * 游戏中
	 */
	int PLAYING = 4;
}
