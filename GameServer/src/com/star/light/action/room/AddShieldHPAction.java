package com.star.light.action.room;

import com.star.light.execaction.Action;
import com.star.light.player.GamePlayer;
import com.star.light.protocol.Protocol;
import com.star.light.room.Room;
import com.star.light.room.RoomPlayer;
import com.star.light.socket.MessageUtil;
import com.star.light.table.BuffInfoMgr;

import tbgame.pbmessage.GamePBMsg.SyncPVPCommonMsg;

public class AddShieldHPAction extends Action {
	private Room room;
	private GamePlayer player;
	private int operType;

	public AddShieldHPAction(Room room, GamePlayer player, int operType) {
		super(room.getActionQueue());
		this.room = room;
		this.player = player;
		this.operType = operType;
	}

	@Override
	public void execute() {
		RoomPlayer roomPlayer = room.getRoomPlayer(player.getUserId());
		if (roomPlayer != null) {
			if (operType == 1) {
				roomPlayer.playerShieldHP = BuffInfoMgr.getInstance().getBuffInfo(1).shieldHP;
			} else if (operType == 2) {
				SyncPVPCommonMsg.Builder pvpCommonMsg = SyncPVPCommonMsg.newBuilder();
				pvpCommonMsg.setPvpId(roomPlayer.pvpId);
				pvpCommonMsg.setOperType(2);
				roomPlayer.playerShieldHP = 0;
				room.sendSyncMsg(0, Protocol.S_C_SYNC_PVP_SHIELDHP, MessageUtil.buildMessage(Protocol.S_C_SYNC_PVP_SHIELDHP, pvpCommonMsg));
			}
		}
	}
}