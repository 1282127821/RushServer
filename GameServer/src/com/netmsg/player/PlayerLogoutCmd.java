package com.netmsg.player;

import com.action.room.ExitRoomAction;
import com.netmsg.NetCmd;
import com.netmsg.PBMessage;
import com.player.GamePlayer;
import com.player.WorldMgr;
import com.room.FightStatus;
import com.room.Room;
import com.room.RoomMgr;
import com.scene.SceneMgr;
import com.team.TeamMgr;
import com.util.Log;

public class PlayerLogoutCmd implements NetCmd
{
	public void execute(GamePlayer player, PBMessage packet)
	{
		try
		{
			Room room = player.getRoom();
			if (room != null)
			{
				RoomMgr.getInstance().addAction(new ExitRoomAction(room, player));
			}

			SceneMgr.getInstance().playerLeave(player);

			// 玩家离开
			TeamMgr.getInstance().leaveTeam(player);
			// 状态改变
			player.setFightStatus(FightStatus.NONE);
		}
		catch (Exception e)
		{
			Log.error("玩家退出游戏 错误,UserId:  " + player.getUserId(), e);
		}
		finally
		{
			WorldMgr.logout(player);
		}
	}
}
