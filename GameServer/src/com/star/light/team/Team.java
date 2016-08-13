package com.star.light.team;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.star.light.execaction.AbstractActionQueue;
import com.star.light.player.GameConst;
import com.star.light.player.GamePlayer;
import com.star.light.protocol.Protocol;
import com.star.light.protocol.S2CProtocol;
import com.star.light.room.RoomMgr;
import com.star.light.scene.SceneMgr;
import com.star.light.scene.SceneType;
import com.star.light.socket.PBMessage;

import tbgame.pbmessage.GamePBMsg.TeamCommonMsg;
import tbgame.pbmessage.GamePBMsg.TeamInfoMsg;
import tbgame.pbmessage.GamePBMsg.TeamMemberMsg;
import tbgame.pbmessage.GamePBMsg.TeamSynMessageMsg_CSC;

public class Team extends AbstractActionQueue {
	public enum TeamState {
		ts_wait, 
		ts_fight
	}

	// 队伍ID
	private long teamId;

	// 队长ID
	private long leaderId;

	private List<GamePlayer> teamMemberList;

	/* 队伍状态 */
	private TeamState teamState = TeamState.ts_wait;

	public TeamState getTeamState() {
		return teamState;
	}

	public void setTeamState(TeamState teamState) {
		this.teamState = teamState;
	}

	public Team() {
		super(RoomMgr.executor);
		teamMemberList = new ArrayList<GamePlayer>();
	}

	public long getTeamId() {
		return teamId;
	}

	/**
	 * 获取队伍成员列表
	 */
	public List<GamePlayer> getTeamMemberList() {
		return teamMemberList;
	}

	public long getLeaderId() {
		return leaderId;
	}

	public void setLeaderId(long leaderId) {
		this.leaderId = leaderId;
	}

	/**
	 * 创建队伍
	 */
	public void createTeam(long teamId, GamePlayer player) {
		this.teamId = teamId;
		leaderId = player.getUserId();
		teamMemberList.add(player);
		player.setTeam(this);
		if (player.sceneType == SceneType.LOUNGE) {
			SceneMgr.getInstance().enterTeamScene(player, player.playerInfo.getPosX(), player.playerInfo.getPosY(),
					player.playerInfo.getPosZ(), player.playerInfo.getDirect());
		}
	}

	/**
	 * 解散队伍
	 */
	public void disbanTeam() {
		Iterator<GamePlayer> iter = teamMemberList.iterator();
		while (iter.hasNext()) {
			GamePlayer gamePlayer = iter.next();
			SceneMgr.getInstance().playerLeave(gamePlayer);
			gamePlayer.setTeam(null);
			gamePlayer.sendPacket(S2CProtocol.S_C_TEAM_DISBAN_SUCCESS, null);
		}
	}

	/**
	 * 退出队伍
	 */
	public void leaveTeam(GamePlayer player) {
		player.setTeam(null);
		if (leaderId == player.getUserId()) {
			leaderId = 0;
		}
		
		Iterator<GamePlayer> iter = teamMemberList.iterator();
		while (iter.hasNext()) {
			GamePlayer teamPlayer = iter.next();
			if (teamPlayer.getUserId() == player.getUserId()) {
				iter.remove();
			}
		}

		TeamInfoMsg.Builder netMsg = TeamInfoMsg.newBuilder();
		netMsg.setLeaderId(leaderId);
		for (GamePlayer teamPlayer : teamMemberList) {
			if (leaderId == 0) {
				leaderId = teamPlayer.getUserId(); // 是否改变过，没有改变的话
				netMsg.setLeaderId(leaderId);
			}

			netMsg.addTeamMemberList(packOneTeamMember(teamPlayer));
		}

		for (GamePlayer teamPlayer : teamMemberList) {
			teamPlayer.sendPacket(S2CProtocol.S_C_TEAM_INIT, netMsg);
		}
	}

	/*
	 * 踢出队伍
	 */
	public void kickOutTeam(GamePlayer player) {
		player.setTeam(null);
		TeamCommonMsg.Builder netMsg = TeamCommonMsg.newBuilder();
		netMsg.setUserId(player.getUserId());
		Iterator<GamePlayer> iter = teamMemberList.iterator();
		while (iter.hasNext()) {
			GamePlayer teamPlayer = iter.next();
			teamPlayer.sendPacket(S2CProtocol.S_C_TEAM_KICKOUT, netMsg);
			if (player.getUserId() == teamPlayer.getUserId()) {
				iter.remove();
			}
		}
	}

	/*
	 * 改变队长
	 */
	public void changeLeaderTeam(GamePlayer player) {
		leaderId = player.getUserId();
		Iterator<GamePlayer> iter = teamMemberList.iterator();
		while (iter.hasNext()) {
			GamePlayer tempPlayer = iter.next();
			TeamCommonMsg.Builder teamLeaderMsg = TeamCommonMsg.newBuilder();
			teamLeaderMsg.setUserId(leaderId);
			tempPlayer.sendPacket(S2CProtocol.S_C_TEAM_CHANGELEADER, teamLeaderMsg);
		}
	}

	private TeamMemberMsg.Builder packOneTeamMember(GamePlayer player) {
		TeamMemberMsg.Builder netMsg = TeamMemberMsg.newBuilder();
		netMsg.setUserId(player.getUserId());
		netMsg.setLevel(player.getPlayerLv());
		netMsg.setNickName(player.getUserName());
		netMsg.setTemplateId(player.getJobId());
		return netMsg;
	}

	public TeamInfoMsg.Builder packOneTeamInfo() {
		TeamInfoMsg.Builder netMsg = TeamInfoMsg.newBuilder();
		netMsg.setLeaderId(leaderId);
		for (GamePlayer gamePlayer : teamMemberList) {
			netMsg.addTeamMemberList(packOneTeamMember(gamePlayer));
		}

		return netMsg;
	}

	public void joinTeam(GamePlayer player) {
		teamMemberList.add(player);
		player.setTeam(this);
		if (player.sceneType == SceneType.LOUNGE) {
			SceneMgr.getInstance().enterTeamScene(player, player.playerInfo.getPosX(), player.playerInfo.getPosY(),
					player.playerInfo.getPosZ(), player.playerInfo.getDirect());
		}

		TeamInfoMsg.Builder netMsg = TeamInfoMsg.newBuilder();
		netMsg.setLeaderId(leaderId);
		// 同步自己的消息给队友
		for (GamePlayer teamPlayer : teamMemberList) {
			netMsg.addTeamMemberList(packOneTeamMember(teamPlayer));
		}

		for (GamePlayer teamPlayer : teamMemberList) {
			teamPlayer.sendPacket(S2CProtocol.S_C_TEAM_INIT, netMsg);
		}
	}

	/**
	 * 同步某些协议
	 */
	public void synTeamMessage(GamePlayer player, int taskId) {
		TeamSynMessageMsg_CSC.Builder netMsg = TeamSynMessageMsg_CSC.newBuilder();
		netMsg.setTaskId(taskId);
		for (GamePlayer teamPlayer : teamMemberList) {
			if (teamPlayer.getUserId() == player.getUserId()) {
				continue;
			}

			teamPlayer.curTaskId = taskId;
			teamPlayer.sendPacket(Protocol.S_C_ACCEPT_TASK, netMsg);
		}
	}

	/**
	 * 发送组队各种需要同步的消息，根据userId来决定是否不给该玩家发送同步消息。
	 */
	public void sendSyncMsg(long userId, short code, PBMessage netMsg) {
		for (GamePlayer gamePlayer : teamMemberList) {
			if (gamePlayer != null) {
				if (gamePlayer.getUserId() != userId) {
					netMsg.setCodeId(code);
					gamePlayer.sendPacket(netMsg);
				}
			}
		}
	}

	/**
	 * 获取队伍人数
	 */
	public int getMemberCount() {
		return teamMemberList.size();
	}
	
	/**
	 * 判断队伍是否已经满员
	 */
	public boolean isTeamFull() {
		return teamMemberList.size() >= GameConst.MAX_TEAM_MEMBER_COUNT;
	}

	/**
	 * 是否是队长
	 */
	public boolean isLeader(long userId) {
		return leaderId == userId;
	}

	/**
	 * 是否是成员
	 */
	public boolean isMember(long userId) {
		for (GamePlayer gamePlayer : teamMemberList) {
			if (gamePlayer.getUserId() == userId) {
				return true;
			}
		}
		return false;
	}
}
