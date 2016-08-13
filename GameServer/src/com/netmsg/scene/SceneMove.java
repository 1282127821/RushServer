package com.netmsg.scene;

import com.netmsg.NetCmd;
import com.netmsg.PBMessage;
import com.pbmessage.GamePBMsg.MainCityMoveMsg;
import com.player.GamePlayer;
import com.scene.SceneMgr;

public class SceneMove implements NetCmd {
	public void execute(GamePlayer player, PBMessage packet) throws Exception {
		byte[] msgBytes = packet.getMsgBody();
		if (msgBytes != null)
		{
			if (!player.isSyncScene() && player.getTeam() == null)
				return;
			
			MainCityMoveMsg netMsg = MainCityMoveMsg.parseFrom(msgBytes);
			MainCityMoveMsg.Builder moveMsg = MainCityMoveMsg.newBuilder();
			moveMsg.setUserId(player.getUserId());
			float posX = netMsg.getPosX();
			float posY = netMsg.getPosY();
			float posZ = netMsg.getPosZ();
			moveMsg.setPosX(posX);
			moveMsg.setPosY(posY);
			moveMsg.setPosZ(posZ);
			moveMsg.setDirect(netMsg.getDirect());
			SceneMgr.getInstance().playerMove(player, moveMsg);
		}
	}
}
