package com.room;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

import com.execaction.Action;
import com.execaction.Executor;
import com.pbmessage.GamePBMsg.RoomInfoListMsg;
import com.pbmessage.GamePBMsg.RoomInfoMsg;
import com.player.GamePlayer;
import com.protocol.Protocol;
import com.util.GameLog;

public final class RoomMgr {
	private AtomicInteger roomID = new AtomicInteger(0);
	public static AtomicInteger pvpId = new AtomicInteger(0);
	private ConcurrentHashMap<Integer, Room> pvpRoomMap = new ConcurrentHashMap<Integer, Room>();

	public static Executor executor = new Executor(8, 16, 5, "RoomMgr");
	private static RoomMgr instance = new RoomMgr();

	public static RoomMgr getInstance() {
		return instance;
	}

	public void enDefaultQueue(Action action) {
		executor.enDefaultQueue(action);
	}

	public void stop() {
		GameLog.info("the RoomMgr is stop...");
		executor.stop();
	}

	public Room getRoom(int roomId) {
		return  pvpRoomMap.get(roomId);
	}

	public Room createPVPRoom() {
		int roomId = roomID.addAndGet(1);
		Room room = new Room(roomId);
		pvpRoomMap.put(roomId, room);
		return room;
	}

	public void removePVPRoom(Room room) {
		room.getTotalRoomPlayer().clear();
		pvpRoomMap.remove(room.getRoomId());
	}

	/**
	 * 打包可以被打劫的房间信息给客户端
	 */
	public void packTotalRoomList(GamePlayer player) {
		RoomInfoListMsg.Builder netMsg = RoomInfoListMsg.newBuilder();
		for (Room roomInfo : pvpRoomMap.values()) {
			if (roomInfo.roomState == RoomState.USEING) {
				RoomInfoMsg.Builder roomInfoMsg = RoomInfoMsg.newBuilder();
				roomInfoMsg.setRoomId(roomInfo.getRoomId());
				roomInfoMsg.setRoomName(roomInfo.getRoomName());
				roomInfoMsg.setRoomState(roomInfo.getRoomState());
				netMsg.addRoomInfoList(roomInfoMsg);
			}
		}
		player.sendPacket(Protocol.S_C_ROOM_INFO_LIST, netMsg);
	}

	public void resetRooms() {
		pvpRoomMap.clear();
	}
}
