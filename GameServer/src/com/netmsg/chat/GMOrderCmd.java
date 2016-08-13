package com.netmsg.chat;

import com.netmsg.NetCmd;
import com.netmsg.PBMessage;
import com.pbmessage.GamePBMsg.GMOrderMsg;
import com.player.GamePlayer;
import com.player.ItemChangeType;
import com.prop.Prop;

public class GMOrderCmd implements NetCmd {
	public void execute(GamePlayer player, PBMessage packet) throws Exception {
		GMOrderMsg netMsg = GMOrderMsg.parseFrom(packet.getMsgBody());
		String gmOrderType = netMsg.getGmContent(0);
		if (gmOrderType.equalsIgnoreCase("addexp")) {
			player.addExp(Integer.parseInt(netMsg.getGmContent(1)));
		} else if (gmOrderType.equalsIgnoreCase("addgold")) {
			player.addGold(Integer.parseInt(netMsg.getGmContent(1)), ItemChangeType.GMGET);
		} else if (gmOrderType.equalsIgnoreCase("adddiamond")) {
			player.addGold(Integer.parseInt(netMsg.getGmContent(1)), ItemChangeType.GMGET);
		} else if (gmOrderType.equalsIgnoreCase("additem")) {
			int itemId = Integer.parseInt(netMsg.getGmContent(1));
			int itemCount = Integer.parseInt(netMsg.getGmContent(2));
			player.getPropMgr().addOnePropToPackage(itemId, itemCount, ItemChangeType.GMGET);
		} else if (gmOrderType.equalsIgnoreCase("delitem")) {
			int itemId = Integer.parseInt(netMsg.getGmContent(1));
			int itemCount = Integer.parseInt(netMsg.getGmContent(2));
			player.getPropMgr().removePropByPropId(itemId, itemCount, ItemChangeType.GMGET);
		} else if (gmOrderType.equalsIgnoreCase("clearbag")) {
			int bagType = Integer.parseInt(netMsg.getGmContent(1));
			Prop[] aryProp = player.getPropMgr().getAllProp(bagType);
			for (Prop prop : aryProp) {
				if (prop != null) {
					player.getPropMgr().destroyPropByProp(bagType, prop, prop.getPropInstance().getStackCount(),
							ItemChangeType.GMGET);
				}
			}
		}
	}
}