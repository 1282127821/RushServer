package com.friend;

import java.util.ArrayList;
import java.util.List;

import com.db.DBObject;
import com.db.DBOption;

public class FriendDBInfo extends DBObject {
	/**
	 * 好友列表
	 */
	private List<FriendInfo> friendGroup = new ArrayList<FriendInfo>();

	/**
	 * 好友上限个数
	 */
	private short friendCount = 30;

	/**
	 * 仇敌列表
	 */
	private List<FriendInfo> enemyGroup = new ArrayList<FriendInfo>();
	
	/**
	 * 仇敌上限个数
	 */
	private short enemyCount = 30;
	
	/**
	 * 战友列表
	 */
	private List<FriendInfo> battleFriendGroup = new ArrayList<FriendInfo>();
	
	/**
	 * 战友上限个数
	 */
	private short battleFriendCount = 30;
	
	public List<FriendInfo> getFriendGroup() {
		return friendGroup;
	}

	public void setFriendGroup(List<FriendInfo> friendGroup) {
		this.friendGroup = friendGroup;
		setOp(DBOption.UPDATE);
	}
	
	public void setStrFriendGroup(String strFriend) {
		if (!strFriend.equals("")) {
			String[] aryFriend = strFriend.split(",");
			for (int i = 0; i < aryFriend.length; ++i) {
				FriendInfo info = new FriendInfo();
				info.userId = Long.parseLong(aryFriend[i]);
				friendGroup.add(info);
			}
		}
	}
	
	public String getStrFriendGroup() {
		StringBuilder strFriend = new StringBuilder();
		if (friendGroup != null) {
			for (FriendInfo info : friendGroup) {
				strFriend.append(info.userId).append(",");
			}
		}
		
		return strFriend.toString();
	}
	
	public short getFriendCount() {
		return friendCount;
	}

	public void setFriendCount(short friendCount) {
		this.friendCount = friendCount;
		setOp(DBOption.UPDATE);
	}

	public List<FriendInfo> getEnemyGroup() {
		return enemyGroup;
	}

	public void setEnemyGroup(List<FriendInfo> enemyGroup) {
		this.enemyGroup = enemyGroup;
		setOp(DBOption.UPDATE);
	}

	public void setStrEnemyGroup(String strEnemy) {
		if (!strEnemy.equals("")) {
			String[] aryEnemy = strEnemy.split(",");
			for (int i = 0; i < aryEnemy.length; ++i) {
				FriendInfo info = new FriendInfo();
				info.userId = Long.parseLong(aryEnemy[i]);
				enemyGroup.add(info);
			}
		}
	}
	
	public String getStrEnemyGroup() {
		StringBuilder strEnemy = new StringBuilder();
		if (enemyGroup != null) {
			for (FriendInfo info  : enemyGroup) {
				strEnemy.append(info.userId).append(",");
			}
		}
		
		return strEnemy.toString();
	}
	
	public short getEnemyCount() {
		return enemyCount;
	}

	public void setEnemyCount(short enemyCount) {
		this.enemyCount = enemyCount;
		setOp(DBOption.UPDATE);
	}

	public List<FriendInfo> getBattleFriendGroup() {
		return battleFriendGroup;
	}

	public void setBattleFriendGroup(List<FriendInfo> battleFriendGroup) {
		this.battleFriendGroup = battleFriendGroup;
		setOp(DBOption.UPDATE);
	}
	
	public void setStrBattleSFriendGroup(String strBattleFriend) {
		if (!strBattleFriend.equals("")) {
			String[] aryBattleFriend = strBattleFriend.split(",");
			for (int i = 0; i < aryBattleFriend.length; ++i) {
				FriendInfo info = new FriendInfo();
				info.userId = Long.parseLong(aryBattleFriend[i]);
				battleFriendGroup.add(info);
			}
		}
	}
	
	public String getStrBattleFriendGroup() {
		StringBuilder strBattleFriend = new StringBuilder();
		if (battleFriendGroup != null) {
			for (FriendInfo info : battleFriendGroup) {
				strBattleFriend.append(info.userId).append(",");
			}
		}
		
		return strBattleFriend.toString();
	}
	
	public short getBattleFriendCount() {
		return battleFriendCount;
	}

	public void setBattleFriendCount(short battleFriendCount) {
		this.battleFriendCount = battleFriendCount;
		setOp(DBOption.UPDATE);
	}

	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append("battleFriendCount:		").append(battleFriendCount).append("\n");
		return sb.toString();
	}
}