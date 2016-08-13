package com.star.light.socket.cmd.player;

import java.util.Date;

import com.star.light.player.GamePlayer;
import com.star.light.socket.PBMessage;
import com.star.light.socket.cmd.NetCmd;
import com.star.light.util.GameLog;
import com.star.light.util.TimeUtil;

public class PlayerLoginReceiveCmd implements NetCmd {

	public void execute(GamePlayer player, PBMessage packet) throws Exception {
		long userId = player.getUserId();
		if (!player.isOnline()) {
			player.beginChanges();
			player.loadPersonData();
			player.setLoginTime(TimeUtil.getSysCurSeconds());
			player.refershData(new Date());
			player.commitChages(true);
			
			//发送黑名单列表给玩家
			player.sendTotalBlackUser();
			
			//发送所有邮件信息列表给客户端
			player.getMailMgr().sendTotalMail();
		} else {
			GameLog.error(userId + " " + player.getUserName() + "当前用户状态不正确  state " + player.getPlayerState());
		}
	}
}