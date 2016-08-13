package com.star.light.socket.cmd.player;

import com.star.light.action.room.ExitRoomAction;
import com.star.light.player.GamePlayer;
import com.star.light.player.WorldMgr;
import com.star.light.room.FightStatus;
import com.star.light.room.Room;
import com.star.light.scene.SceneMgr;
import com.star.light.socket.PBMessage;
import com.star.light.socket.cmd.NetCmd;
import com.star.light.team.TeamMgr;
import com.star.light.util.GameLog;

public class PlayerLogoutCmd implements NetCmd {

	public void execute(GamePlayer player, PBMessage packet) {
		try {
			Room room = player.getRoom();
			if (room != null) {
				room.enqueue(new ExitRoomAction(room, player));
			}
			
			SceneMgr.getInstance().playerLeave(player);
			
			//玩家离开
			TeamMgr.getInstance().leaveTeam(player);
			// 状态改变
			player.setFightStatus(FightStatus.NONE);
		} catch (Exception e) {
			GameLog.error("玩家退出游戏 错误,UserId:  " + player.getUserId(), e);
		} finally {
			WorldMgr.logout(player);
		}
	}
}
