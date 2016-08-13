package com.action.room;

import java.util.List;

import com.execaction.Action;
import com.pbmessage.GamePBMsg.PlayerMsg;
import com.pbmessage.GamePBMsg.RoomBossInfoMsg;
import com.pbmessage.GamePBMsg.RoomMsg;
import com.pbmessage.GamePBMsg.RoomPlayerInfoMsg;
import com.player.GamePlayer;
import com.player.PlayerMgr;
import com.protocol.S2CProtocol;
import com.room.BattleCamp;
import com.room.Room;
import com.room.RoomBossInfo;
import com.room.RoomMgr;
import com.table.CharacterInfoMgr;
import com.table.LevelStageInfo;
import com.table.LevelStageInfoMgr;
import com.table.MainTaskInfoMgr;
import com.team.Team;
import com.util.GameLog;

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
