package com.star.light.socket.cmd.pvp;
import com.star.light.action.room.AddPlayerHPAction;
import com.star.light.player.GamePlayer;
import com.star.light.protocol.Protocol;
import com.star.light.room.Room;
import com.star.light.socket.PBMessage;
import com.star.light.socket.cmd.NetCmd;

public class UseRecoverProp implements NetCmd {
	public void execute(GamePlayer player, PBMessage packet) throws Exception {
		Room room = player.getRoom();
		if (room != null) {
			room.enqueue(new AddPlayerHPAction(room, player));
			room.sendSyncMsg(0, Protocol.S_C_USE_RECOVERY_DRUG, packet);
		}
	}
}
