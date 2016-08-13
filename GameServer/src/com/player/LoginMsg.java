package com.player;

public class LoginMsg {

	private int key; // 返回结果
	private int frontData; // 前面人数
	private int time;// 需要时间
	private int totalData;// 总排队人数

	public LoginMsg() {

	}

	public void setFrontData(int frontData) {
		this.frontData = frontData;
	}

	public int getFrontData() {
		return frontData;
	}

	public void setTime(int time) {
		this.time = time;
	}

	public int getTime() {
		return time;
	}

	public void setKey(int key) {
		this.key = key;
	}

	public int getKey() {
		return key;
	}

	public void setTotalData(int totalData) {
		this.totalData = totalData;
	}

	public int getTotalData() {
		return totalData;
	}

}
