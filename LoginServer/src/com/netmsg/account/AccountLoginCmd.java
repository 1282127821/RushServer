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
import com.pbmessage.GamePBMsg.AttributeInfoMsg;
import com.pbmessage.GamePBMsg.KickOutPlayerMsg;
import com.pbmessage.GamePBMsg.PlayerInfoMsg;
import com.protocol.S2CProtocol;
import com.util.Log;
import com.util.TimeUtil;

import io.netty.channel.ChannelHandlerContext;

public class AccountLoginCmd implements NetMsg {
	public void execute(ChannelHandlerContext ctx, PBMessage packet) throws Exception {
		AccountLoginMsg netMsg = AccountLoginMsg.parseFrom(packet.getMsgBody());
		String accountName = netMsg.getAccountName();
		AccountInfo account = AccountMgr.getInstance().getOnlineAccount(accountName);
		if (account != null) {
			try {
				KickOutPlayerMsg.Builder kickMsg = KickOutPlayerMsg.newBuilder();
				kickMsg.setKickOutType(1);
				// account.sendToClient(MessageUtil.buildMessage(Protocol.S_C_KICK_PLAYER,
				// kickMsg));
			} catch (Exception e) {
				Log.error("挤在线玩家错误  accountName : " + accountName, e);
			} finally {
				// long clientId = (Long)
				// user.getSession().getAttribute(LinkedClient.KEY_ID);
				// UserMgr.removeOnline(clientId, user.getSession());
			}
		}

		// 创建账号
		String loginIp = ctx.channel().localAddress().toString();
		AccountInfo accountInfo = DaoMgr.accountDao.getAccount(accountName);
		if (accountInfo != null) {
			accountInfo.setLoginIP(loginIp);
			boolean result = DaoMgr.accountDao.updateLoginAccount(accountInfo);
			if (!result) {
				Log.error("更新用户信息失败");
				return;
			}
		} else {
			accountInfo = new AccountInfo();
			accountInfo.setAccountId(BaseServer.IDWORK.nextId());
			accountInfo.setAccountName(accountName);
			accountInfo.setLoginIP(loginIp);
			accountInfo.setCreateTime(TimeUtil.getSysCurSecond());
			if (!DaoMgr.accountDao.addAccount(accountInfo)) {
				Log.error("添加账号失败，账号名: " + accountName + "到数据库失败,请检查数据库是否正常?");
			}
		}

		accountLogin(ctx, accountInfo);
	}

	/**
	 * 显示玩家
	 */
	private void accountLogin(ChannelHandlerContext ctx, AccountInfo account) {
		AccountLoginResultMsg.Builder netMsg = AccountLoginResultMsg.newBuilder();
		int forbidExpireTime = account.getForbidExpireTime();
		// 检查玩家是否被禁号
		if (forbidExpireTime > 0 && TimeUtil.getSysCurSecond() < forbidExpireTime) {
			netMsg.setLoginResult(2);
			// 玩家禁号时间过期，解禁玩家
			boolean isForbid = DaoMgr.accountDao.forbidAccount(account.getAccountId(), forbidExpireTime,
					"玩家禁号时间过期，系统自动解封", "系统");
			if (!isForbid) {
				Log.error("玩家账号被封");
			}
			return;
		}

		long accountId = account.getAccountId();
		List<PlayerInfo> totalPlayerInfoList = DaoMgr.playerInfoDao.getTotalPlayerInfo(accountId);
		netMsg.setLoginResult(1);
		// TODO:LZGLZG 这里看是需要给网关的地址还是说直接登录的时候就只使用网关，由网关来转发给登录服和游戏服
		for (PlayerInfo playerInfo : totalPlayerInfoList) {
			PlayerInfoMsg.Builder playerInfoMsg = PlayerInfoMsg.newBuilder();
			playerInfoMsg.setUserId(playerInfo.getUserId());
			playerInfoMsg.setUserName(playerInfo.getUserName());
			playerInfoMsg.setPlayerLv(playerInfo.getPlayerLv());
			playerInfoMsg.setJobType(playerInfo.getJobType());
			playerInfoMsg.setFightStrength(playerInfo.getFightStrength());
			netMsg.addPlayerInfoList(playerInfoMsg);
		}

		PBMessage pbMsg = new PBMessage(S2CProtocol.S_C_LOGIN_GATEWAY);
		pbMsg.setMsgBody(netMsg.build().toByteArray());
		ctx.writeAndFlush(pbMsg);
		
		PlayerInfoMsg.Builder infoMsg = PlayerInfoMsg.newBuilder();
		infoMsg.setUserId(accountId);
		infoMsg.setUserName("zhongjiaxin");
		infoMsg.setPlayerLv(20);
		infoMsg.setJobType(1);
		infoMsg.setFightStrength(1000);
		pbMsg = new PBMessage(S2CProtocol.S_C_PLAYER_INFO);
		pbMsg.setMsgBody(infoMsg.build().toByteArray());
		ctx.writeAndFlush(pbMsg);

		AttributeInfoMsg.Builder attMsg = AttributeInfoMsg.newBuilder();
		attMsg.setAttributeType(5);
		attMsg.setAttributeValue(10000);
		pbMsg = new PBMessage(S2CProtocol.S_C_SYNC_MAIN_CITY_PLAYER);
		pbMsg.setMsgBody(infoMsg.build().toByteArray());
		ctx.writeAndFlush(pbMsg);
	}
}