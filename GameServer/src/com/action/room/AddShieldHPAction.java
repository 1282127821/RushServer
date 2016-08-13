package com.action.room;

import com.execaction.Action;
import com.netmsg.MessageUtil;
import com.pbmessage.GamePBMsg.SyncPVPCommonMsg;
import com.player.GamePlayer;
import com.protocol.Protocol;
import com.room.Room;
import com.room.RoomPlayer;
import com.table.BuffInfoMgr;

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