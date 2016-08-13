package com.team;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.BaseServer;
import com.execaction.Action;
import com.pbmessage.GamePBMsg.TeamMemberMsg;
import com.pbmessage.GamePBMsg.TeamMsg;
import com.pbmessage.GamePBMsg.TeamViewListMsg;
import com.pbmessage.GamePBMsg.TeamViewMsg;
import com.player.GamePlayer;
import com.player.WorldMgr;
import com.protocol.Protocol;
import com.room.RoomMgr;
import com.scene.SceneMgr;

public final class TeamMgr {
	private Map<Long, Team> teamMap = new ConcurrentHashMap<Long, Team>();
	private static TeamMgr instance = new TeamMgr();

	public static TeamMgr getInstance() {
		return instance;
	}

	public void enDefaultQueue(Action action) {
		RoomMgr.executor.enDefaultQueue(action);
	}

	/**
	 * 创建队伍
	 */
	public void createTeam(GamePlayer player) {
		Team team = new Team();
		long teamId = BaseServer.IDWORK.nextId();
		team.createTeam(teamId, player);
		teamMap.put(teamId, team);
		player.sendPacket(Protocol.S_C_TEAM_CREATE_SUCCESS, team.packOneTeamInfo());
	}

	/**
	 * 根据队伍Id获得对应的队伍
	 */
	public Team getTeamById(long teamId) {
		if (teamMap.containsKey(teamId)) {
			return teamMap.get(teamId);
		}

		return null;
	}

	/**
	 * 加入某个队伍
	 */
	public void joinTeam(long teamId, GamePlayer player) {
		Team team = getTeamById(teamId);
		if (team == null || player.getTeam() != null) {
			return;
		}

		team.joinTeam(player);
	}

	/**
	 * 离开队伍
	 */
	public void leaveTeam(GamePlayer player) {
		Team team = player.getTeam();
		if (team == null) {
			return;
		}

		SceneMgr.getInstance().playerLeave(player);
		team.leaveTeam(player);
		// 如果队伍不存在玩家了 ，就删除
		if (team.getMemberCount() == 0) {
			teamMap.remove(team.getTeamId());
		}
	}

	/**
	 * 踢出玩家
	 */
	public void kickOutTeam(GamePlayer player, long userId) {
		Team team = player.getTeam();
		if (team == null || !team.isLeader(player.getUserId()) || !team.isMember(userId)) {
			return;
		}

		SceneMgr.getInstance().playerLeave(player);
		GamePlayer teamPlayer = WorldMgr.getOnlinePlayer(userId);
		if (teamPlayer != null) {
			team.kickOutTeam(teamPlayer);
		}
	}

	/**
	 * 改变队长
	 */
	public void changeLeaderTeam(GamePlayer player, long userId) {
		Team team = player.getTeam();
		if (team == null || !team.isLeader(player.getUserId()) || !team.isMember(userId)) {
			return;
		}

		GamePlayer teamPlayer = WorldMgr.getOnlinePlayer(userId);
		if (teamPlayer != null) {
			team.changeLeaderTeam(teamPlayer);
		}
	}

	/**
	 * 解散队伍
	 */
	public void disbanTeam(GamePlayer player) {
		Team team = player.getTeam();
		// 判断是否有队伍和否是队长
		if (team == null || !team.isLeader(player.getUserId())) {
			return;
		}

		// 解散队伍
		team.disbanTeam();
		// 从队伍管理器里面删除
		teamMap.remove(team.getTeamId());
	}

	public void requestTeamInfo(GamePlayer player) {
		TeamMsg.Builder netMsg =TeamMsg.newBuilder();
		Team team = player.getTeam();
		if (team != null) {
			netMsg.setTeamInfo(team.packOneTeamInfo());
		} else {
			netMsg.setTotalTeamList(packTotalTeamInfo());
		}
		player.sendPacket(Protocol.S_C_REQUEST_TEAM_INFO, netMsg);
	}

	private TeamViewListMsg.Builder packTotalTeamInfo() {
		TeamViewListMsg.Builder netMsg = TeamViewListMsg.newBuilder();
		for (Team value : teamMap.values()) {
			/* 过滤非等待状态的队伍 */
			if (value.getTeamState() != Team.TeamState.ts_wait) {
				continue;
			}

			long leaderId = value.getLeaderId();
			int nCount = value.getMemberCount();
			TeamViewMsg.Builder teamMsg = TeamViewMsg.newBuilder();
			teamMsg.setTeamId(value.getTeamId());
			teamMsg.setTeamMemberNum(nCount);
			TeamMemberMsg.Builder leaderInfo = TeamMemberMsg.newBuilder();
			GamePlayer tempplayer = WorldMgr.getOnlinePlayer(leaderId);
			leaderInfo.setUserId(tempplayer.getUserId());
			leaderInfo.setLevel(tempplayer.getPlayerLv());
			leaderInfo.setNickName(tempplayer.getUserName());
			leaderInfo.setTemplateId(tempplayer.getJobId());
			teamMsg.setLeaderInfo(leaderInfo);
			netMsg.addTeamViewList(teamMsg);
		}

		return netMsg;
	}

	/**
	 * 同步队伍列表
	 */
	public void synTeamList(GamePlayer player) {
		player.sendPacket(Protocol.S_C_TEAM_LIST, packTotalTeamInfo());
	}

	public void synTeamMessage(GamePlayer player, int taskId) {
		Team team = player.getTeam();
		// 判断是否有队伍
		if (team == null) {
			return;
		}

		// 是否是队长
		if (!team.isLeader(player.getUserId())) {
			return;
		}

		team.synTeamMessage(player, taskId);
	}
	
	public void combineTeam(GamePlayer player, long userId) {
		GamePlayer otherPlayer = WorldMgr.getOnlinePlayer(userId);
		if (otherPlayer == null) {
			return;
		}
		
		Team team = player.getTeam();
		if (team != null) {
			Team otherTeam = otherPlayer.getTeam();
			if (otherTeam != null) {
				player.sendTips(1013);
				return;
			}
			
			if (team.isTeamFull()) {
				player.sendTips(1016);
				return;
			}
			
			joinTeam(team.getTeamId(), otherPlayer);
		} else {
			Team otherTeam = otherPlayer.getTeam();
			if (otherTeam != null) {
				if (otherTeam.isTeamFull()) {
					player.sendTips(1015);
					return;
				}
				
				joinTeam(otherTeam.getTeamId(), player);
			} else {
				createTeam(player);
				joinTeam(player.getTeamId(), otherPlayer);
			}
		}
	}
}
