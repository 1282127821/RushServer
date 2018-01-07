package com.netmsg.player;

import java.util.Date;

import com.netmsg.NetCmd;
import com.netmsg.PBMessage;
import com.player.GamePlayer;
import com.util.Log;
import com.util.TimeUtil;

public class PlayerLoginReceiveCmd implements NetCmd
{

	public void execute(GamePlayer player, PBMessage packet) throws Exception
	{
		long userId = player.getUserId();
		if (!player.isOnline())
		{
			player.beginChanges();
			player.loadPersonData();
			player.setLoginTime(TimeUtil.getSysCurSecond());
			player.refershData(new Date());
			player.commitChages(true);

			// 发送黑名单列表给玩家
			player.sendTotalBlackUser();

			// 发送所有邮件信息列表给客户端
			player.getMailMgr().sendTotalMail();
		}
		else
		{
			Log.error(userId + " " + player.getUserName() + "当前用户状态不正确  state " + player.getPlayerState());
		}
	}
}