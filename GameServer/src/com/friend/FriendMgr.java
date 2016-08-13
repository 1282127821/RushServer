package com.friend;

import java.util.List;

import com.db.DBOption;
import com.pbmessage.GamePBMsg.FriendMsg;
import com.pbmessage.GamePBMsg.PlayerMsg;
import com.pbmessage.GamePBMsg.SearchNearUserInfoMsg;
import com.player.DaoMgr;
import com.player.GamePlayer;
import com.player.PlayerInfo;
import com.player.WorldMgr;
import com.protocol.Protocol;
import com.util.GameLog;

public class FriendMgr {
	private GamePlayer player;

	private FriendDBInfo friendDBInfo;

	public FriendMgr(GamePlayer player) {
		this.player = player;
	}

	private void getFriendInfoFromDB(FriendInfo friendInfo) {
		GamePlayer gamePlayer = WorldMgr.getPlayer(friendInfo.userId);
		friendInfo.userName = gamePlayer.getUserName();
		friendInfo.jobId = gamePlayer.getJobId();
		friendInfo.playerLv = gamePlayer.getPlayerLv();
		friendInfo.playerState = gamePlayer.getPlayerState();
		friendInfo.guildId = gamePlayer.getGuildId();
		friendInfo.teamId = gamePlayer.getTeamId();
	}

	public void loadFromDB() {
		friendDBInfo = DaoMgr.friendDao.getFriendDBInfo(player.getUserId());
		if (friendDBInfo == null) {
			friendDBInfo = new FriendDBInfo();
			friendDBInfo.setOp(DBOption.INSERT);
		}

		for (FriendInfo friendInfo : friendDBInfo.getFriendGroup()) {
			getFriendInfoFromDB(friendInfo);
		}

		for (FriendInfo friendInfo : friendDBInfo.getEnemyGroup()) {
			getFriendInfoFromDB(friendInfo);
		}

		for (FriendInfo friendInfo : friendDBInfo.getBattleFriendGroup()) {
			getFriendInfoFromDB(friendInfo);
		}
	}

	private PlayerMsg.Builder packOneFriendInfo(FriendInfo friendInfo) {
		PlayerMsg.Builder playerMsg = PlayerMsg.newBuilder();
		playerMsg.setUserId(friendInfo.userId);
		playerMsg.setUserName(friendInfo.userName);
		playerMsg.setJobId(friendInfo.jobId);
		playerMsg.setPlayerLv(friendInfo.playerLv);
		playerMsg.setPlayerState(friendInfo.playerState);
		playerMsg.setGuildId(friendInfo.guildId);
		playerMsg.setTeamId(friendInfo.teamId);
		return playerMsg;
	}

	public void viewTotalFriendInfo() {
		FriendMsg.Builder netMsg = FriendMsg.newBuilder();
		for (FriendInfo friendInfo : friendDBInfo.getFriendGroup()) {
			netMsg.addFriendList(packOneFriendInfo(friendInfo));
		}
		netMsg.setFriendCount(friendDBInfo.getFriendCount());

		for (FriendInfo friendInfo : friendDBInfo.getEnemyGroup()) {
			netMsg.addEnemyList(packOneFriendInfo(friendInfo));
		}
		netMsg.setEnemyCount(friendDBInfo.getEnemyCount());

		for (FriendInfo friendInfo : friendDBInfo.getBattleFriendGroup()) {
			netMsg.addBattleFriendList(packOneFriendInfo(friendInfo));
		}
		netMsg.setBattleFriendCount(friendDBInfo.getBattleFriendCount());

		player.sendPacket(Protocol.S_C_VIEW_FRIEND_INFO, netMsg);
	}

	public FriendInfo getFriendInfo(long userId, List<FriendInfo> friendList) {
		for (FriendInfo friendInfo : friendList) {
			if (friendInfo.userId == userId) {
				return friendInfo;
			}
		}

		return null;
	}

	public FriendInfo getFriendInfo(String userName, List<FriendInfo> friendList) {
		for (FriendInfo friendInfo : friendList) {
			if (friendInfo.userName == userName) {
				return friendInfo;
			}
		}

		return null;
	}

	/**
	 * 添加一个好友
	 */
	public void addFriend(long userId) {
		List<FriendInfo> friendGroup = friendDBInfo.getFriendGroup();
		if (friendGroup.size() >= friendDBInfo.getFriendCount()) {
			player.sendTips(1012);
			return;
		}

		FriendInfo friendInfo = getFriendInfo(userId, friendGroup);
		if (friendInfo != null) {
			player.sendTips(1010);
			return;
		}
		
		GamePlayer gamePlayer = WorldMgr.getPlayer(userId);
		if (gamePlayer == null) {
			return;
		}

		friendInfo = new FriendInfo();
		friendInfo.userId = gamePlayer.getUserId();
		friendInfo.userName = gamePlayer.getUserName();
		friendInfo.jobId = gamePlayer.getJobId();
		friendInfo.playerLv = gamePlayer.getPlayerLv();
		friendInfo.playerState = gamePlayer.getPlayerState();
		friendInfo.guildId = gamePlayer.getGuildId();
		friendInfo.teamId = gamePlayer.getTeamId();
		friendGroup.add(friendInfo);
		friendDBInfo.setOp(DBOption.UPDATE);
		player.sendPacket(Protocol.S_C_ADD_FRIEND, packOneFriendInfo(friendInfo));
	}

	/**
	 * 添加一个仇敌
	 */
	public void addEnemy(long userId) {
		List<FriendInfo> enemyGroup = friendDBInfo.getEnemyGroup();
		FriendInfo friendInfo = getFriendInfo(userId, enemyGroup);
		if (friendInfo == null) {
			GamePlayer gamePlayer = WorldMgr.getPlayer(userId);
			if (gamePlayer == null) {
				return;
			}

			if (enemyGroup.size() >= friendDBInfo.getEnemyCount()) {
				enemyGroup.remove(0);
			}

			friendInfo = new FriendInfo();
			friendInfo.userId = gamePlayer.getUserId();
			friendInfo.userName = gamePlayer.getUserName();
			friendInfo.jobId = gamePlayer.getJobId();
			friendInfo.playerLv = gamePlayer.getPlayerLv();
			friendInfo.playerState = gamePlayer.getPlayerState();
			friendInfo.guildId = gamePlayer.getGuildId();
			friendInfo.teamId = gamePlayer.getTeamId();
			enemyGroup.add(friendInfo);
			friendDBInfo.setOp(DBOption.UPDATE);
		}
	}

	/**
	 * 添加一个战友
	 */
	public void addBattleFriend(long userId) {
		List<FriendInfo> battleFriendGroup = friendDBInfo.getBattleFriendGroup();
		FriendInfo friendInfo = getFriendInfo(userId, battleFriendGroup);
		if (friendInfo == null) {
			GamePlayer gamePlayer = WorldMgr.getPlayer(userId);
			if (gamePlayer == null) {
				return;
			}

			if (battleFriendGroup.size() >= friendDBInfo.getBattleFriendCount()) {
				battleFriendGroup.remove(0);
			}

			friendInfo = new FriendInfo();
			friendInfo.userId = gamePlayer.getUserId();
			friendInfo.userName = gamePlayer.getUserName();
			friendInfo.jobId = gamePlayer.getJobId();
			friendInfo.playerLv = gamePlayer.getPlayerLv();
			friendInfo.playerState = gamePlayer.getPlayerState();
			friendInfo.guildId = gamePlayer.getGuildId();
			friendInfo.teamId = gamePlayer.getTeamId();
			battleFriendGroup.add(friendInfo);
			friendDBInfo.setOp(DBOption.UPDATE);
		}
	}

	/**
	 * 删除一个好友
	 */
	public void deleteFriend(long userId) {
		FriendInfo friendInfo = getFriendInfo(userId, friendDBInfo.getFriendGroup());
		if (friendInfo != null) {
			friendDBInfo.getFriendGroup().remove(friendInfo);
			friendDBInfo.setOp(DBOption.UPDATE);
		}
	}

	/**
	 * 删除一个仇敌
	 */
	public void deleteEnemy(long userId) {
		FriendInfo friendInfo = getFriendInfo(userId, friendDBInfo.getEnemyGroup());
		if (friendInfo != null) {
			friendDBInfo.getEnemyGroup().remove(friendInfo);
			friendDBInfo.setOp(DBOption.UPDATE);
		}
	}

	/**
	 * 删除一个战友
	 */
	public void deleteBattleFriend(long userId) {
		FriendInfo friendInfo = getFriendInfo(userId, friendDBInfo.getBattleFriendGroup());
		if (friendInfo != null) {
			friendDBInfo.getBattleFriendGroup().remove(friendInfo);
			friendDBInfo.setOp(DBOption.UPDATE);
		}
	}

	/**
	 * 根据角色的名字来搜索某个玩家
	 */
	public void searchFriendByUserName(String userName) {
		if (userName == null || userName.equals("")) {
			return;
		}

		FriendInfo friendInfo = getFriendInfo(userName, friendDBInfo.getFriendGroup());
		if (friendInfo != null) {
			player.sendTips(1010);
			return;
		}

		PlayerInfo playerInfo = DaoMgr.playerInfoDao.getPlayerByUserName(userName);
		if (playerInfo == null) {
			player.sendTips(1011);
			return;
		}

		PlayerMsg.Builder netMsg = PlayerMsg.newBuilder();
		netMsg.setUserId(playerInfo.getUserId());
		netMsg.setUserName(playerInfo.getUserName());
		netMsg.setJobId(playerInfo.getJobId());
		netMsg.setPlayerLv(playerInfo.getPlayerLv());
		netMsg.setPlayerState(playerInfo.getPlayerState());
		netMsg.setGuildId(playerInfo.getGuildId());
		player.sendPacket(Protocol.S_C_SEARCH_FRIEND, netMsg);
	}

	/**
	 * 搜索附近的玩家，用来添加好友
	 */
	public void searchNearUser() {
		int playerLv = player.getPlayerLv();
		List<GamePlayer> onlineGamePlayer = WorldMgr.getOnlineList();
		SearchNearUserInfoMsg.Builder netMsg = SearchNearUserInfoMsg.newBuilder();
		for (GamePlayer gamePlayer : onlineGamePlayer) {
			FriendInfo friendInfo = getFriendInfo(gamePlayer.getUserId(), friendDBInfo.getFriendGroup());
			if (friendInfo != null || gamePlayer.getUserId() == player.getUserId()) {
			    continue;
			}
			
			int otherPlayerLv = gamePlayer.getPlayerLv();
			if (otherPlayerLv >= playerLv - 10 && otherPlayerLv <= playerLv + 10) {
				PlayerMsg.Builder playerMsg = PlayerMsg.newBuilder();
				playerMsg.setUserId(gamePlayer.getUserId());
				playerMsg.setUserName(gamePlayer.getUserName());
				playerMsg.setJobId(gamePlayer.getJobId());
				playerMsg.setPlayerLv(gamePlayer.getPlayerLv());
				playerMsg.setPlayerState(gamePlayer.getPlayerState());
				playerMsg.setGuildId(gamePlayer.getGuildId());
				playerMsg.setTeamId(gamePlayer.getTeamId());
				netMsg.addUserInfoList(playerMsg);
				if (netMsg.getUserInfoListCount() >= 10) {
					break;
				}
			}
		}

		player.sendPacket(Protocol.S_C_VIEW_NEAR_USER, netMsg);
	}

	public void unloadData() {
		friendDBInfo = null;
	}

	public void saveToDB() {
		long userId = player.getUserId();
		try {
			if (friendDBInfo.getOp() == DBOption.INSERT) {
				DaoMgr.friendDao.insertFriendDBInfo(userId, friendDBInfo);
			} else if (friendDBInfo.getOp() == DBOption.UPDATE) {
				DaoMgr.friendDao.updateFriendDBInfo(userId, friendDBInfo);
			}
		} catch (Exception e) {
			GameLog.error("保存玩家技能信息出错, UserId:  " + userId, e);
		}
	}
}
