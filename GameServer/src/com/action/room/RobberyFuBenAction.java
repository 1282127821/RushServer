package com.action.room;

import java.util.List;

import com.execaction.Action;
import com.pbmessage.GamePBMsg.PlayerMsg;
import com.pbmessage.GamePBMsg.RoomBossInfoMsg;
import com.pbmessage.GamePBMsg.RoomInfoMsg;
import com.pbmessage.GamePBMsg.RoomMsg;
import com.pbmessage.GamePBMsg.RoomPlayerInfoMsg;
import com.player.GamePlayer;
import com.player.PlayerMgr;
import com.protocol.Protocol;
import com.room.BattleCamp;
import com.room.Room;
import com.room.RoomBossInfo;
import com.room.RoomMgr;
import com.room.RoomPlayer;
import com.room.RoomState;
import com.team.Team;

public class RobberyFuBenAction extends Action {
	private GamePlayer player;
	private RoomInfoMsg netMsg;

	public RobberyFuBenAction(GamePlayer player, RoomInfoMsg netMsg) {
		super(RoomMgr.executor.getDefaultQueue());
		this.player = player;
		this.netMsg = netMsg;
	}

	@Override
	public void execute() {
		Team team = player.getTeam();
		if (team != null && !team.isLeader(player.getUserId())) {
			player.sendTips(1017);
			return;
		}

		int roomId = netMsg.getRoomId();
		Room room = RoomMgr.getInstance().getRoom(roomId);
		if (room == null || room.roomState == RoomState.PLAYING || player.getRoom() != null) {
			return;
		}

		RoomMsg.Builder defenserRoomMsg = RoomMsg.newBuilder();
		defenserRoomMsg.setRoomId(roomId);
		defenserRoomMsg.setRoomStageId(room.roomStageId);
		RoomMsg.Builder attackRoomMsg = RoomMsg.newBuilder();
		attackRoomMsg.setRoomId(roomId);
		attackRoomMsg.setRoomStageId(room.roomStageId);
		List<RoomPlayer> roomPlayerList = room.getTotalRoomPlayer();
		int robberyCamp = BattleCamp.ATTACKER;
		for (RoomPlayer roomPlayer : roomPlayerList) {
			RoomPlayerInfoMsg.Builder roomPlayerInfoMsg = RoomPlayerInfoMsg.newBuilder();
			PlayerMsg.Builder playerInfoMsg = PlayerMgr.buildPlayerInfoMsg(roomPlayer.player.playerInfo);
			playerInfoMsg.setCurrHP(roomPlayer.playerHP);
			playerInfoMsg.setCurrShieldHp(roomPlayer.playerShieldHP);
			roomPlayerInfoMsg.setPlayerInfo(playerInfoMsg);
			roomPlayerInfoMsg.setRoomIndex(roomPlayer.roomIndex);
			robberyCamp = roomPlayer.roomCamp;
			roomPlayerInfoMsg.setRoomCamp(roomPlayer.roomCamp);
			roomPlayerInfoMsg.setPvpId(roomPlayer.pvpId);
			attackRoomMsg.addRoomPlayerList(roomPlayerInfoMsg);
		}

		int roomPlayerCount = room.getRoomPlayerCount();
		robberyCamp += 1;
		if (team != null) {
			List<GamePlayer> teamMemberList = team.getTeamMemberList();
			for (int i = 0; i < teamMemberList.size(); i++) {
				GamePlayer tempPlayer = teamMemberList.get(i);
				RoomPlayerInfoMsg.Builder roomPlayerInfo = RoomPlayerInfoMsg.newBuilder();
				PlayerMsg.Builder playerInfoMsg = PlayerMgr.buildPlayerInfoMsg(tempPlayer.playerInfo);
				roomPlayerInfo.setPlayerInfo(playerInfoMsg);
				roomPlayerInfo.setRoomIndex(roomPlayerCount + i);
				roomPlayerInfo.setRoomCamp(robberyCamp);
				int pvpId = RoomMgr.pvpId.addAndGet(1);
				roomPlayerInfo.setPvpId(pvpId);
				room.addPlayer(tempPlayer, robberyCamp, pvpId);
				defenserRoomMsg.addRoomPlayerList(roomPlayerInfo);
				attackRoomMsg.addRoomPlayerList(roomPlayerInfo);
			}
		} else {
			RoomPlayerInfoMsg.Builder roomPlayerInfo = RoomPlayerInfoMsg.newBuilder();
			PlayerMsg.Builder playerInfoMsg = PlayerMgr.buildPlayerInfoMsg(player.playerInfo);
			roomPlayerInfo.setPlayerInfo(playerInfoMsg);
			roomPlayerInfo.setRoomIndex(roomPlayerCount);
			roomPlayerInfo.setRoomCamp(robberyCamp);
			int pvpId = RoomMgr.pvpId.addAndGet(1);
			roomPlayerInfo.setPvpId(pvpId);
			room.addPlayer(player, robberyCamp, pvpId);
			defenserRoomMsg.addRoomPlayerList(roomPlayerInfo);
			attackRoomMsg.addRoomPlayerList(roomPlayerInfo);
		}

		roomPlayerList = room.getTotalRoomPlayer();
		for (RoomPlayer roomPlayer : roomPlayerList) {
			if (roomPlayer.roomCamp != robberyCamp) {
				roomPlayer.player.sendPacket(Protocol.S_C_ROBBERYED_INSTANCE, defenserRoomMsg);
			}
		}

		RoomBossInfoMsg.Builder bossInfoMsg = RoomBossInfoMsg.newBuilder();
		RoomBossInfo roomBossInfo = room.roomBossInfo;
		bossInfoMsg.setBossId(roomBossInfo.bossId);
		bossInfoMsg.setNodeId(roomBossInfo.nodeId);
		bossInfoMsg.setBossCamp(roomBossInfo.camp);
		bossInfoMsg.setBossPvpId(roomBossInfo.pvpId);
		bossInfoMsg.setBossHP(roomBossInfo.bossHP);
		attackRoomMsg.setBossInfo(bossInfoMsg);

		if (team != null) {
			List<GamePlayer> teamMemberList = team.getTeamMemberList();
			for (GamePlayer tempPlayer : teamMemberList) {
				tempPlayer.sendPacket(Protocol.S_C_ROBBERY_INSTANCE, attackRoomMsg);
			}
		} else {
			player.sendPacket(Protocol.S_C_ROBBERY_INSTANCE, attackRoomMsg);
		}

		room.roomState = RoomState.PLAYING;
	}
}