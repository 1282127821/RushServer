package com.netmsg.account;

import java.util.List;

import com.BaseServer;
import com.game.AccountInfo;
import com.game.AccountMgr;
import com.game.DaoMgr;
import com.game.NetMsg;
import com.game.PBMessage;
import com.game.PlayerInfo;
import com.pbmessage.GamePBMsg.AccountLoginMsg;
import com.pbmessage.GamePBMsg.AccountLoginResultMsg;
import com.pbmessage.GamePBMsg.KickOutPlayerMsg;
import com.pbmessage.GamePBMsg.PlayerInfoMsg;
import com.util.GameLog;
import com.util.StringUtil;
import com.util.TimeUtil;

import io.netty.channel.Channel;

public class AccountLoginCmd implements NetMsg {
	public void execute(Channel channel, PBMessage packet) throws Exception {
		AccountLoginMsg netMsg = AccountLoginMsg.parseFrom(packet.getMsgBody());
		String accountName = netMsg.getAccountName();
		AccountInfo account = AccountMgr.getInstance().getOnlineAccount(accountName);
		if (account != null) {
			try {
				KickOutPlayerMsg.Builder kickMsg = KickOutPlayerMsg.newBuilder();
				kickMsg.setKickOutType(1);
//				account.sendToClient(MessageUtil.buildMessage(Protocol.S_C_KICK_PLAYER, kickMsg));
			} catch (Exception e) {
				GameLog.error("挤在线玩家错误  accountName : " + accountName, e);
			} finally {
//				long clientId = (Long) user.getSession().getAttribute(LinkedClient.KEY_ID);
//				UserMgr.removeOnline(clientId, user.getSession());
			}
		}
		
		AccountMgr.getInstance().addAccount(accountName);
		
		// 平台密码
		String password = netMsg.getPassword();
		// gameId
		String gameId = netMsg.getGameId();
		// 渠道id
		String channelId = netMsg.getChannelId();
		// 服id
		String serverId = netMsg.getServerId();
		// 手机imei
		String imei = netMsg.getImei();
		// 手机型号
		String model = netMsg.getModel();
		// 手机品牌
		String brand = netMsg.getBrand();
		
		// 请求的ip
		String loginIp = channel.localAddress().toString();
		// if (!checkLoginIp(loginIp)) {
		// return;
		// }

		// 验证sdk登陆
		 boolean isSuccess = verifySDKLogin(gameId, channelId, serverId, accountName, password);
		 if (!isSuccess) {
			 return;
		 }

		// 手机imei 超过36截取多余的
		if (imei.length() > 36) {
			imei = imei.substring(0, 36);
		}
		
		// 手机型号 超过36截取多余的
		if (model.length() > 36) {
			model = model.substring(0, 36);
		}
		
		// 手机品牌 超过20截取多余的
		if (brand.length() > 20) {
			brand = brand.substring(0, 20);
		}

		// 创建账号
		AccountInfo dbAccount = getAccountInfo(accountName, loginIp, imei, model, brand, gameId);
		if (dbAccount != null) {
			accountLogin(dbAccount);
		}
	}

	/**
	 * SDK的验证
	 */
	private boolean verifySDKLogin(String gameid, String channelId, String serverId, String accountName, String password) {
		// 是否是正式环境
//		if (WebConfig.isFormalEnvironment) {
			// 成功返回示例：{"code":"100","msg":"success"}
			// 错误返回示例：{"code":"0","msg":"token error"}
//			int code = LongtuService.getInstance().verifyLogin(gameid, channelId, serverId, accountName, password);
//			if (code == 100) {
//				return true;
//			}
//			Log.error("sdk登陆验证错误" + ", gameId:" + gameid + ", channelId:" + channelId + ", serverId:" + serverId
//					+ ", accountName:" + accountName + ", password:" + password + ", code:" + code);
//			 return false;
//		} 
		
		return true;
	}

	/**
	 * 获取账号信息，如果不存在则往数据库里面插入一个新的账号信息
	 */
	public static AccountInfo getAccountInfo(String accountName, String loginIP, String imei, String model, String brand, String gameId) {
		AccountInfo info = DaoMgr.accountDao.getAccount(accountName);
		if (info != null) {
			info.setLoginIP(loginIP);
			boolean result = DaoMgr.accountDao.updateLoginAccount(info);
			if (!result) {
				GameLog.error("更新用户信息失败");
				return null;
			}
		} else {
			info = new AccountInfo();
			info.setAccountId(BaseServer.IDWORK.nextId());
			info.setAccountName(accountName);
			info.setLoginIP(loginIP);
			info.setImei(imei);
			info.setModel(model);
			info.setBrand(brand);
			if (StringUtil.isNotNullOrEmpty(gameId)) {
				info.setGameId(Integer.valueOf(gameId));
			}

			if (!DaoMgr.accountDao.addAccount(info)) {
				info = null;
				GameLog.error("添加账号失败，账号名: " + accountName + "到数据库失败,请检查数据库是否正常?");
			}
		}
		return info;
	}

	/**
	 * 显示玩家
	 */
	private void accountLogin(AccountInfo account) {
		AccountLoginResultMsg.Builder netMsg = AccountLoginResultMsg.newBuilder();
		int forbidExpireTime = account.getForbidExpireTime();
		// 检查玩家是否被禁号
		if (forbidExpireTime > 0 && TimeUtil.getSysCurSeconds() < forbidExpireTime) {
			netMsg.setLoginResult(2);
			// 玩家禁号时间过期，解禁玩家
			boolean isForbid = DaoMgr.accountDao.forbidAccount(account.getAccountId(), forbidExpireTime, "玩家禁号时间过期，系统自动解封", "系统");
			if (!isForbid) {
				GameLog.error("玩家账号被封");
			}
			return;
		}
			
		long accountId = account.getAccountId();
		List<PlayerInfo> totalPlayerInfoList = DaoMgr.playerInfoDao.getTotalPlayerInfo(accountId);
		netMsg.setLoginResult(1);
		netMsg.setDelCDTime(account.getDelCDTime());

		// NetConfigXml gateway =
		// NetConfig.getInstance().getNetConfigXml(ServerType.GATEWAY,
		// NetConfig.getInstance().getGateWayId(accountId));
		// if (playerInfos == null || playerInfos.size() == 0) {
		// return String.format("<?xml version=\"1.0\"
		// encoding=\"UTF-8\"?><Result value=\"%s\" userId=\"%s\" message=\"%s\"
		// content=\'%s\' Address=\"%s\" Port=\"%s\" />", result, accountId,
		// message, showPlayers.toString(), gateway.getAddress(),
		// gateway.getPort());
		// }

		for (PlayerInfo playerInfo : totalPlayerInfoList) {
			PlayerInfoMsg.Builder playerInfoMsg = PlayerInfoMsg.newBuilder();
			playerInfoMsg.setUserId(playerInfo.getUserId());
			playerInfoMsg.setUserName(playerInfo.getUserName());
			playerInfoMsg.setPlayerLv(playerInfo.getPlayerLv());
			playerInfoMsg.setJobType(playerInfo.getJobId());
			// playerInfoMsg.setFightStrength(value);
			netMsg.addPlayerInfoList(playerInfoMsg);
		}
	}
}