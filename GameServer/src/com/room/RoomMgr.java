package com.room;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

import com.executor.AbstractAction;
import com.executor.CmdTaskQueue;
import com.executor.ExecutorPool;
import com.pbmessage.GamePBMsg.RoomInfoListMsg;
import com.pbmessage.GamePBMsg.RoomInfoMsg;
import com.player.GamePlayer;
import com.protocol.Protocol;
import com.util.Log;

public final class RoomMgr
{
	private AtomicInteger roomID = new AtomicInteger(0);
	public static AtomicInteger pvpId = new AtomicInteger(0);
	private ConcurrentHashMap<Integer, Room> pvpRoomMap = new ConcurrentHashMap<Integer, Room>();
	/** 玩家命令处理线程池 */
	public static ExecutorPool userCmdpool = new ExecutorPool("RoomMgr", Runtime.getRuntime().availableProcessors());
	public static CmdTaskQueue actionQueue = new CmdTaskQueue(userCmdpool);
	private static RoomMgr instance = new RoomMgr();

	public static RoomMgr getInstance()
	{
		return instance;
	}

	public void addAction(AbstractAction action)
	{
		action.setActionQueue(actionQueue);
		actionQueue.add(action);
	}

	public void stop()
	{
		Log.info("the RoomMgr is stop...");
		userCmdpool.shutdown();
		actionQueue.clear();
	}

	public Room getRoom(int roomId)
	{
		return pvpRoomMap.get(roomId);
	}

	public Room createPVPRoom()
	{
		int roomId = roomID.addAndGet(1);
		Room room = new Room(roomId);
		pvpRoomMap.put(roomId, room);
		return room;
	}

	public void removePVPRoom(Room room)
	{
		room.getTotalRoomPlayer().clear();
		pvpRoomMap.remove(room.getRoomId());
	}

	/**
	 * 打包可以被打劫的房间信息给客户端
	 */
	public void packTotalRoomList(GamePlayer player)
	{
		RoomInfoListMsg.Builder netMsg = RoomInfoListMsg.newBuilder();
		for (Room roomInfo : pvpRoomMap.values())
		{
			if (roomInfo.roomState == RoomState.USEING)
			{
				RoomInfoMsg.Builder roomInfoMsg = RoomInfoMsg.newBuilder();
				roomInfoMsg.setRoomId(roomInfo.getRoomId());
				roomInfoMsg.setRoomName(roomInfo.getRoomName());
				roomInfoMsg.setRoomState(roomInfo.getRoomState());
				netMsg.addRoomInfoList(roomInfoMsg);
			}
		}
		player.sendPacket(Protocol.S_C_ROOM_INFO_LIST, netMsg);
	}

	public void resetRooms()
	{
		pvpRoomMap.clear();
	}
}
