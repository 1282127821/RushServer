package com.star.light.action.room;

import java.util.List;

import com.star.light.execaction.Action;
import com.star.light.player.GamePlayer;
import com.star.light.player.PlayerMgr;
import com.star.light.protocol.S2CProtocol;
import com.star.light.room.BattleCamp;
import com.star.light.room.Room;
import com.star.light.room.RoomBossInfo;
import com.star.light.room.RoomMgr;
import com.star.light.table.CharacterInfoMgr;
import com.star.light.table.LevelStageInfo;
import com.star.light.table.LevelStageInfoMgr;
import com.star.light.table.MainTaskInfoMgr;
import com.star.light.team.Team;
import com.star.light.util.GameLog;

import tbgame.pbmessage.GamePBMsg.PlayerMsg;
import tbgame.pbmessage.GamePBMsg.RoomBossInfoMsg;
import tbgame.pbmessage.GamePBMsg.RoomMsg;
import tbgame.pbmessage.GamePBMsg.RoomPlayerInfoMsg;

public class RoomCreateAction extends Action {
	private GamePlayer player;

	public RoomCreateAction(GamePlayer player) {
		super(RoomMgr.executor.getDefaultQueue());
		this.player = player;
	}

	@Override
	public void execute() {
		Room room = RoomMgr.getInstance().createPVPRoom();
		if (room == null) {
			GameLog.error("创建房间失败");
			return;
		}

		room.start(player.getUserName());
		RoomMsg.Builder roomMsg = RoomMsg.newBuilder();
		roomMsg.setRoomId(room.getRoomId());

		Team team = player.getTeam();
		if (team != null) {
			List<GamePlayer> teamMemberList = team.getTeamMemberList();
			for (int i = 0; i < teamMemberList.size(); i++) {
				GamePlayer tempPlayer = teamMemberList.get(i);
				RoomPlayerInfoMsg.Builder roomPlayerInfo = RoomPlayerInfoMsg.newBuilder();
				PlayerMsg.Builder playerInfoMsg = PlayerMgr.buildPlayerInfoMsg(tempPlayer.playerInfo);
				roomPlayerInfo.setPlayerInfo(playerInfoMsg);
				roomPlayerInfo.setRoomIndex(i);
				roomPlayerInfo.setRoomCamp(BattleCamp.DEFENCER);
				int pvpId = RoomMgr.pvpId.addAndGet(1);
				roomPlayerInfo.setPvpId(pvpId);
				room.addPlayer(tempPlayer, BattleCamp.DEFENCER, pvpId);
				roomMsg.addRoomPlayerList(roomPlayerInfo);
			}
		} else {
			RoomPlayerInfoMsg.Builder roomPlayerInfo = RoomPlayerInfoMsg.newBuilder();
			PlayerMsg.Builder playerInfoMsg = PlayerMgr.buildPlayerInfoMsg(player.playerInfo);
			roomPlayerInfo.setPlayerInfo(playerInfoMsg);
			roomPlayerInfo.setRoomIndex(0);
			roomPlayerInfo.setRoomCamp(BattleCamp.DEFENCER);
			int pvpId = RoomMgr.pvpId.addAndGet(1);
			roomPlayerInfo.setPvpId(pvpId);
			room.addPlayer(player, BattleCamp.DEFENCER, pvpId);
			roomMsg.addRoomPlayerList(roomPlayerInfo);
		}

		RoomBossInfoMsg.Builder bossInfoMsg = RoomBossInfoMsg.newBuilder();
		RoomBossInfo roomBossInfo = new RoomBossInfo();
		int roomStageId = MainTaskInfoMgr.getInstance().getMainTaskInfo(player.curTaskId).stageId;
		LevelStageInfo stageInfo = LevelStageInfoMgr.getInstance().getLevelStageInfo(roomStageId);
		roomBossInfo.bossId = stageInfo.monsterId[0];
		roomBossInfo.nodeId = stageInfo.monsterNodeId[0];
		roomBossInfo.camp = BattleCamp.BOSS;
		roomBossInfo.pvpId = RoomMgr.pvpId.addAndGet(1);
		roomBossInfo.bossHP = CharacterInfoMgr.getInstance().getCharacterInfo(roomBossInfo.bossId).charHP;
		roomBossInfo.bossMasterId = player.getUserId();
		room.roomStageId = roomStageId;
		roomMsg.setRoomStageId(room.roomStageId);
		room.roomBossInfo = roomBossInfo;
		bossInfoMsg.setBossId(roomBossInfo.bossId);
		bossInfoMsg.setNodeId(roomBossInfo.nodeId);
		bossInfoMsg.setBossCamp(roomBossInfo.camp);
		bossInfoMsg.setBossPvpId(roomBossInfo.pvpId);
		bossInfoMsg.setBossHP(roomBossInfo.bossHP);
		bossInfoMsg.setBossMasterId(roomBossInfo.bossMasterId);
		roomMsg.setBossInfo(bossInfoMsg);

		if (team != null) {
			List<GamePlayer> teamMemberList = team.getTeamMemberList();
			for (GamePlayer teamPlayer : teamMemberList) {
				teamPlayer.sendPacket(S2CProtocol.S_C_ENTER_INSTANCE, roomMsg);
				for (GamePlayer friendPlayer : teamMemberList) {
					long friendUserId = friendPlayer.getUserId();
					if (friendUserId != teamPlayer.getUserId()) {
						teamPlayer.getFriendMgr().addBattleFriend(friendUserId);
					}
				}
			}
		} else {
			player.sendPacket(S2CProtocol.S_C_ENTER_INSTANCE, roomMsg);
		}
	}
}
