package com.skill;

import com.db.DBObject;
import com.db.DBOption;

public class FightSkillUnit extends DBObject {
	/**
	 * 技能数据库的主键Id
	 */
	private long skillDBId;
	
	/**
	 * 技能Id
	 */
	private int skillId;

	/**
	 * 技能等级
	 */
	private int skillLv;
	
	/**
	 * 技能的类型 true为主动技能 false为被动技能
	 */
	private boolean isActiveSkill;

	public long getSkillDBId() {
		return skillDBId;
	}
	
	public void setSkillDBId(long skillDBId) {
		this.skillDBId = skillDBId;
	}

	public int getSkillId() {
		return skillId;
	}

	public void setSkillId(int skillId) {
		this.skillId = skillId;
		setOp(DBOption.UPDATE);
	}

	public int getSkillLv() {
		return skillLv;
	}

	public void setSkillLv(int skillLv) {
		this.skillLv = skillLv;
		setOp(DBOption.UPDATE);
	}
	
	public boolean isActiveSkill() {
		return isActiveSkill;
	}

	public void setActiveSkill(boolean isActiveSkill) {
		this.isActiveSkill = isActiveSkill;
		setOp(DBOption.UPDATE);
	}

	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append("skillDBId:		").append(skillDBId).append("\n");
		sb.append("skillId:		").append(skillId).append("\n");
		sb.append("skillLv:		").append(skillLv).append("\n");
		sb.append("isActiveSkill:		").append(isActiveSkill).append("\n");
		return sb.toString();
	}
}