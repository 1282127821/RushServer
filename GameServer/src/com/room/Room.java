package com.room;

import java.util.ArrayList;
import java.util.List;

import com.netmsg.MessageUtil;
import com.netmsg.PBMessage;
import com.pbmessage.GamePBMsg.PvpBattleResultMsg;
import com.pbmessage.GamePBMsg.RoomPlayerInfoMsg;
import com.pbmessage.GamePBMsg.SyncPVPAIMsg;
import com.pbmessage.GamePBMsg.SyncPVPCommonMsg;
import com.pbmessage.GamePBMsg.SyncPVPMasterChangeMsg;
import com.player.GameConst;
import com.player.GamePlayer;
import com.player.PlayerMgr;
import com.protocol.Protocol;
import com.team.Team;

public class Room
{
	/**
	 * 房间id
	 */
	public int roomId;

	/**
	 * 房间玩家列表
	 */
	public List<RoomPlayer> roomPlayerList;

	/**
	 * 房间BOSS的信息
	 */
	public RoomBossInfo roomBossInfo;

	/**
	 * 房间名字
	 */
	public String roomName;

	/**
	 * 房间状态
	 */
	public int roomState;

	/**
	 * 房间的关卡Id，根据任务Id来定
	 */
	public int roomStageId;

	/**
	 * 是否已经播放了场景动画
	 */
	public boolean isStartScene;

	/**
	 * 是否播放了入场动画
	 */
	public boolean isEnterAnimation;

	public int getRoomId()
	{
		return roomId;
	}

	public String getRoomName()
	{
		return roomName;
	}

	public List<RoomPlayer> getTotalRoomPlayer()
	{
		return roomPlayerList;
	}

	public Room(int roomId)
	{
		this.roomId = roomId;
		this.roomState = RoomState.UNUSE;
	}

	public int getRoomState()
	{
		return roomState;
	}

	public void start(String roomName)
	{
		roomPlayerList = new ArrayList<RoomPlayer>(GameConst.ROOM_MAX_CAPACITY);
		this.roomName = roomName;
	}

	/**
	 * 房间加入一个玩家
	 */
	public boolean addPlayer(GamePlayer player, int roomCamp, int pvpId)
	{
		player.setRoom(this);
		RoomPlayer roomPlayer = new RoomPlayer();
		roomPlayer.player = player;
		roomPlayer.roomCamp = roomCamp;
		roomPlayer.roomIndex = 1;
		roomPlayer.pvpId = pvpId;
		roomPlayer.playerHP = player.playerInfo.getCurrHP();
		roomPlayerList.add(roomPlayer);
		player.setFightStatus(FightStatus.MULPVP);
		return true;
	}

	/**
	 * 房间移除一个玩家
	 */
	public void removePlayer(GamePlayer player)
	{
		for (RoomPlayer roomPlayer : roomPlayerList)
		{
			if (roomPlayer.player.getUserId() == player.getUserId())
			{
				player.setRoom(null);
				roomPlayerList.remove(roomPlayer);
				break;
			}
		}

		if (roomPlayerList.size() == 0)
		{
			RoomMgr.getInstance().removePVPRoom(this);
		}

		player.setFightStatus(FightStatus.NONE);
	}

	public void readyForPVPPK(GamePlayer player)
	{
		long userId = player.getUserId();
		RoomPlayer selfRoomPlayer = getRoomPlayer(userId);
		if (selfRoomPlayer == null)
		{
			return;
		}

		selfRoomPlayer.pvpState = FightStatus.READY;
		SyncPVPCommonMsg.Builder netMsg = SyncPVPCommonMsg.newBuilder();
		netMsg.setPvpId(selfRoomPlayer.pvpId);
		for (RoomPlayer roomPlayer : roomPlayerList)
		{
			if (roomPlayer.roomCamp != selfRoomPlayer.roomCamp)
			{
				roomPlayer.player.sendPacket(Protocol.S_C_PVP_CANCEL_INVINCE, netMsg);
			}
		}
	}

	/**
	 * 获取房间存在玩家的个数
	 */
	public int getRoomPlayerCount()
	{
		return roomPlayerList.size();
	}

	public RoomPlayer getRoomPlayer(long userId)
	{
		for (RoomPlayer roomPlayer : roomPlayerList)
		{
			if (roomPlayer.player.getUserId() == userId)
			{
				return roomPlayer;
			}
		}
		return null;
	}

	public RoomPlayer getRoomPlayer(long userId, int battleCamp)
	{
		RoomPlayer roomPlayer = getRoomPlayer(userId, battleCamp);
		if (roomPlayer.roomCamp != battleCamp)
		{
			return roomPlayer;
		}

		return null;
	}

	/**
	 * 判断是否满员
	 */
	public boolean isRoomFull()
	{
		return roomPlayerList.size() >= GameConst.ROOM_MAX_CAPACITY;
	}

	/**
	 * 发送PVP过程中各种需要同步的消息，根据userId来决定是否不给该玩家发送同步消息。
	 */
	public void sendSyncMsg(long userId, short code, PBMessage netMsg)
	{
		for (RoomPlayer roomPlayer : roomPlayerList)
		{
			GamePlayer roomGamePlayer = roomPlayer.player;
			if (roomGamePlayer.getUserId() != userId && roomPlayer.pvpState == FightStatus.READY)
			{
				netMsg.setMsgId(code);
				roomGamePlayer.sendPacket(netMsg);
			}
		}
	}

	/**
	 * 更新房间状态
	 */
	public void updateRoomState(int state)
	{
		roomState = state;
	}

	/**
	 * 离开房间
	 */
	public void exitRoom(GamePlayer player)
	{
		long userId = player.getUserId();
		RoomPlayer roomPlayer = getRoomPlayer(userId);
		if (roomPlayer == null)
		{
			return;
		}

		RoomPlayerInfoMsg.Builder netMsg = RoomPlayerInfoMsg.newBuilder();
		netMsg.setPvpId(roomPlayer.pvpId);
		sendSyncMsg(0, Protocol.S_C_LEAVE_INSTANCE, MessageUtil.buildMessage(Protocol.S_C_LEAVE_INSTANCE, netMsg));
		roomPlayer.isDead = true;
		isPVPEnd();
		removePlayer(player);
		if (userId == roomBossInfo.bossMasterId)
		{
			for (RoomPlayer roomOtherPlayer : roomPlayerList)
			{
				SyncPVPMasterChangeMsg.Builder masterMsg = SyncPVPMasterChangeMsg.newBuilder();
				long newMasterId = roomOtherPlayer.player.getUserId();
				roomBossInfo.bossMasterId = newMasterId;
				masterMsg.setMasterId(newMasterId);
				sendSyncMsg(0, Protocol.S_C_PVP_MASTER_CHANGE, MessageUtil.buildMessage(Protocol.S_C_PVP_MASTER_CHANGE, masterMsg));
				break;
			}
		}

		boolean isCanRobbery = true;
		for (int i = 0; i < roomPlayerList.size() - 1; i++)
		{
			for (int j = i + 1; j < roomPlayerList.size(); j++)
			{
				if (roomPlayerList.get(i).roomCamp != roomPlayerList.get(j).roomCamp)
				{
					isCanRobbery = false;
				}
			}
		}

		if (isCanRobbery)
		{
			roomState = RoomState.USEING;
		}

		startPVPAI();
	}

	public void startPVPAI()
	{
		if (isStartScene)
		{
			boolean isCanStartAI = false;
			for (RoomPlayer roomPlayer : roomPlayerList)
			{
				if (roomPlayer.isCanAttack)
				{
					isCanStartAI = true;
					break;
				}
			}

			SyncPVPAIMsg.Builder netMsg = SyncPVPAIMsg.newBuilder();
			netMsg.setIsStartAI(isCanStartAI);
			for (RoomPlayer roomPlayer : roomPlayerList)
			{
				if (roomPlayer.player.getUserId() == roomBossInfo.bossMasterId)
				{
					roomPlayer.player.sendPacket(Protocol.S_C_STAR_PVP_AI, netMsg);
					break;
				}
			}
		}
	}

	/**
	 * 判断PVP是否已经结束了
	 */
	public void isPVPEnd()
	{
		int defenceCamp = 0;
		int attackCamp = 0;
		if (roomPlayerList.size() > 0)
		{
			attackCamp = roomPlayerList.get(0).roomCamp;
			for (RoomPlayer roomPlayer : roomPlayerList)
			{
				if (attackCamp != roomPlayer.roomCamp)
				{
					defenceCamp = roomPlayer.roomCamp;
				}
			}
		}

		boolean isAttackDie = true;
		for (RoomPlayer roomPlayer : roomPlayerList)
		{
			if (attackCamp == roomPlayer.roomCamp && !roomPlayer.isDead)
			{
				isAttackDie = false;
				continue;
			}
		}

		boolean isDefenderDie = true;
		for (RoomPlayer roomPlayer : roomPlayerList)
		{
			if (defenceCamp == roomPlayer.roomCamp && !roomPlayer.isDead)
			{
				isDefenderDie = false;
				continue;
			}
		}

		// BOSS赢了
		int winCamp = 0;
		if (!roomBossInfo.isDead && isAttackDie && isDefenderDie)
		{
			winCamp = BattleCamp.BOSS;
		}
		else if (roomBossInfo.isDead && !isAttackDie && isDefenderDie)
		{
			winCamp = attackCamp;
		}
		else if (roomBossInfo.isDead && isAttackDie && !isDefenderDie)
		{
			winCamp = defenceCamp;
		}

		if (winCamp != 0)
		{
			if (winCamp >= BattleCamp.ATTACKER)
			{ // 如果入侵方赢了则把入侵方的玩家添加为防守方的仇敌
				for (RoomPlayer attackPlayer : roomPlayerList)
				{
					if (attackPlayer.roomCamp == BattleCamp.DEFENCER)
					{
						continue;
					}

					for (RoomPlayer defencePlayer : roomPlayerList)
					{
						if (defencePlayer.roomCamp == BattleCamp.DEFENCER)
						{
							defencePlayer.player.getFriendMgr().addEnemy(attackPlayer.player.getUserId());
						}
					}
				}
			}

			sendPvpResult(winCamp, defenceCamp);
			sendPvpResult(winCamp, attackCamp);
			RoomMgr.getInstance().removePVPRoom(this);
		}
	}

	private void sendPvpResult(int winCamp, int battleCamp)
	{
		PvpBattleResultMsg.Builder netMsg = null;
		for (RoomPlayer roomPlayer : roomPlayerList)
		{
			if (roomPlayer.roomCamp != battleCamp)
			{
				continue;
			}

			if (netMsg == null)
			{
				netMsg = PvpBattleResultMsg.newBuilder();
				Team team = roomPlayer.player.getTeam();
				if (team != null)
				{
					List<GamePlayer> teamMemberList = team.getTeamMemberList();
					for (GamePlayer tempPlayer : teamMemberList)
					{
						netMsg.addPvpPlayerList(PlayerMgr.buildPlayerInfoMsg(tempPlayer.playerInfo));
					}
				}
			}

			int isWin = winCamp == roomPlayer.roomCamp ? 1 : 2;
			netMsg.setIsWin(isWin);
			if (isWin == 1)
			{
				roomPlayer.player.giveStageReward(1001, netMsg);
			}
			roomPlayer.player.sendPacket(Protocol.S_C_PVP_RESULT, netMsg);
			netMsg.clearRewardItemIdList();
			roomPlayer.player.setRoom(null);
		}
	}
}
