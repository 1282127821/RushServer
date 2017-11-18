package com.action.room;

import java.util.List;

import com.executor.AbstractAction;
import com.netmsg.MessageUtil;
import com.pbmessage.GamePBMsg.OnceHitDataMsg;
import com.pbmessage.GamePBMsg.SyncHitDataMsg;
import com.pbmessage.GamePBMsg.SyncHurtMsg;
import com.pbmessage.GamePBMsg.SyncPVPCommonMsg;
import com.protocol.Protocol;
import com.room.Room;
import com.room.RoomBossInfo;
import com.room.RoomPlayer;
import com.util.Log;

public class SyncHurtAction extends AbstractAction
{
	private Room room;
	private SyncHurtMsg hurtMsg;

	public SyncHurtAction(Room room, SyncHurtMsg hurtMsg)
	{
		this.room = room;
		this.hurtMsg = hurtMsg;
	}

	@Override
	public void execute()
	{
		RoomBossInfo roomBossInfo = room.roomBossInfo;
		int bossPVPId = roomBossInfo.pvpId;
		SyncHurtMsg.Builder netMsg = SyncHurtMsg.newBuilder();
		netMsg.setPvpId(hurtMsg.getPvpId());
		netMsg.setHitGroupIdx(hurtMsg.getHitGroupIdx());
		netMsg.setDirectionX(hurtMsg.getDirectionX());
		netMsg.setIsFly(hurtMsg.getIsFly());
		netMsg.setModelName(hurtMsg.getModelName());
		netMsg.setActionLabel(hurtMsg.getActionLabel());
		netMsg.setFlySkillId(hurtMsg.getFlySkillId());
		List<SyncHitDataMsg> hitDataListMsg = hurtMsg.getBearHurtListList();
		List<RoomPlayer> roomPlayerList = room.getTotalRoomPlayer();
		boolean isDie = false;
		for (SyncHitDataMsg hitMsg : hitDataListMsg)
		{
			int bearerId = hitMsg.getBearerId();
			SyncHitDataMsg.Builder hitNetMsg = SyncHitDataMsg.newBuilder();
			hitNetMsg.setBearerId(bearerId);
			hitNetMsg.setCurrentSuperArmorLevel(hitMsg.getCurrentSuperArmorLevel());
			hitNetMsg.setHitPointX(hitMsg.getHitPointX());
			hitNetMsg.setHitPointY(hitMsg.getHitPointY());
			hitNetMsg.setHitPointZ(hitMsg.getHitPointZ());
			hitNetMsg.setBearerPointX(hitMsg.getBearerPointX());
			hitNetMsg.setBearerPointY(hitMsg.getBearerPointY());
			hitNetMsg.setBearerPointZ(hitMsg.getBearerPointZ());
			hitNetMsg.setSynIsBuffer(hitMsg.getSynIsBuffer());
			List<OnceHitDataMsg> onceHitListMsg = hitMsg.getHitDataListList();
			if (bossPVPId == bearerId)
			{
				hitNetMsg.setCurrHP(roomBossInfo.bossHP);
				for (OnceHitDataMsg onceMsg : onceHitListMsg)
				{
					roomBossInfo.bossHP -= onceMsg.getSyncBearHp();
					if (roomBossInfo.bossHP <= 0 && !roomBossInfo.isDead)
					{
						roomBossInfo.isDead = true;
						isDie = true;
						Log.info("LZGLZG  SyncHurtAction roomBossInfo Die");
					}
					OnceHitDataMsg.Builder onceHitNetMsg = OnceHitDataMsg.newBuilder();
					onceHitNetMsg.setSyncBearHp(onceMsg.getSyncBearHp());
					onceHitNetMsg.setSyncBearArmor(onceMsg.getSyncBearArmor());
					onceHitNetMsg.setSyncIsCrit(onceMsg.getSyncIsCrit());
					hitNetMsg.addHitDataList(onceHitNetMsg);
					// GameLog.info("LZGLZG RoomPlayer roomBossInfo: " +
					// roomBossInfo.bossHP);
				}

			}
			else
			{
				for (RoomPlayer roomPlayer : roomPlayerList)
				{
					if (roomPlayer.pvpId == bearerId)
					{
						hitNetMsg.setCurrHP(roomPlayer.playerHP);
						for (OnceHitDataMsg onceMsg : onceHitListMsg)
						{
							OnceHitDataMsg.Builder onceHitNetMsg = OnceHitDataMsg.newBuilder();
							onceHitNetMsg.setSyncBearHp(onceMsg.getSyncBearHp());
							if (roomPlayer.playerShieldHP > 0)
							{
								roomPlayer.playerShieldHP -= onceMsg.getSyncBearHp();
								roomPlayer.playerShieldHP = roomPlayer.playerShieldHP < 0 ? 0 : roomPlayer.playerShieldHP;
								if (roomPlayer.playerShieldHP == 0)
								{
									SyncPVPCommonMsg.Builder pvpCommonMsg = SyncPVPCommonMsg.newBuilder();
									pvpCommonMsg.setPvpId(roomPlayer.pvpId);
									pvpCommonMsg.setOperType(2);
									room.sendSyncMsg(0, Protocol.S_C_SYNC_PVP_SHIELDHP, MessageUtil.buildMessage(Protocol.S_C_SYNC_PVP_SHIELDHP, pvpCommonMsg));
								}
								onceHitNetMsg.setSyncBearHp(0);
							}
							else
							{
								roomPlayer.playerHP -= onceMsg.getSyncBearHp();
								if (roomPlayer.playerHP <= 0 && !roomPlayer.isDead)
								{
									roomPlayer.isDead = true;
									isDie = true;
									Log.info("LZGLZG SyncHurtAction RoomPlayer Die");
								}
							}

							onceHitNetMsg.setSyncBearArmor(onceMsg.getSyncBearArmor());
							onceHitNetMsg.setSyncIsCrit(onceMsg.getSyncIsCrit());
							hitNetMsg.addHitDataList(onceHitNetMsg);
							// GameLog.info("LZGLZG RoomPlayer playerHP: " +
							// roomPlayer.playerHP);
						}
						break;
					}
				}
			}
			netMsg.addBearHurtList(hitNetMsg);
		}

		room.sendSyncMsg(0, Protocol.S_C_SYNC_HURT, MessageUtil.buildMessage(Protocol.S_C_SYNC_HURT, netMsg));
		if (isDie)
		{
			room.isPVPEnd();
		}
	}
}
