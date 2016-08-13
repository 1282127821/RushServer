package com.webservice;

import javax.jws.WebService;

import com.BaseServer;
import com.db.DBOption;
import com.player.DaoMgr;
import com.player.GamePlayer;
import com.player.LoginMgr;
import com.player.LoginMsg;
import com.player.PlayerInfo;
import com.player.WorldMgr;
import com.protocol.Protocol;
import com.util.GameLog;
import com.util.TimeUtil;

@WebService
public class CastleWS {
	public static boolean isClose = false;

	/**
	 * 账号登录登录
	 */
	public int loginAccount(long accountId, String key) {
		// 登陆已关闭
		if (isClose)
			return 2;

		try {
			LoginMgr.createOnline(key.toUpperCase(), accountId); // 通知castle在线
			return 0;
		} catch (Exception e) {
			GameLog.error("userId:" + accountId + " not found!", e);
		}
		return 1;
	}

	/**
	 * 角色登录
	 */
	public int loginUser(long userId) {
		try {
			if (isClose)
				return 2;
			GamePlayer player = WorldMgr.getPlayer(userId);
			if (player != null) {
				return 0;
			}
		} catch (Exception e) {
			GameLog.error("playerId:" + userId, e);
		}
		return 1;
	}

	/**
	 * 创建角色
	 */
	public long createPlayer(long accountId, String userName, int jobId, String site) {
		try {
			long userId = BaseServer.IDWORK.nextId();
			PlayerInfo playerInfo = new PlayerInfo();
			playerInfo.setUserId(userId);
			playerInfo.setAccountId(accountId);
			playerInfo.setUserName(userName);
			playerInfo.setJobId(jobId);
			playerInfo.setCreateTime(TimeUtil.getSysCurSeconds());
			playerInfo.setPlayerLv(1);
			playerInfo.setOp(DBOption.INSERT);
			boolean result = DaoMgr.playerInfoDao.addPlayerInfo(playerInfo);
			if (result) {
				return userId;
			}
		} catch (Exception e) {
			GameLog.error(String.format("accountId:%s userName: %s jobId %s", accountId, userName, jobId), e);
		}
		return -1;
	}

	/**
	 * 删除角色
	 */
	public boolean deletePlayer(long userId, long accountId) {
		try {
			GamePlayer player = WorldMgr.getPlayer(userId);
			if (player.isOnline()) {
				player.sendPacket(Protocol.G_DELETE_USER, null);
			}
			
			return DaoMgr.playerInfoDao.deletePlayer(userId);
		} catch (Exception e) {
			GameLog.error(String.format("删除角色错误 accountId:%s userid %s", accountId, userId), e);
		}
		return true;
	}

	// 检测登陆
	public LoginMsg checkLogin(String name) {
		return LoginMgr.checkLogin(name);
	}

	// 检测在线
	public boolean checkOnline(long userId, String key) {
		return LoginMgr.checkOnline(userId, key);
	}
}
