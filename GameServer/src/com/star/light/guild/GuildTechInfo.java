package com.star.light.guild;

import com.star.light.db.DBObject;
import com.star.light.db.DBOption;

public class GuildTechInfo extends DBObject {
	/**
	 * 科技Id
	 */
	private int techId;
	
	/**
	 * 科技等级
	 */
	private int techLv;
	
	/**
	 * 单个科技的总贡献度
	 */
	private int contribution;

	public int getTechId() {
		return techId;
	}

	public void setTechId(int techId) {
		this.techId = techId;
	}

	public int getTechLv() {
		return techLv;
	}

	public void setTechLv(int techLv) {
		if (this.techLv != techLv) {
			this.techLv = techLv;
			setOp(DBOption.UPDATE);
		}
	}
	
	public int getContribution() {
		return contribution;
	}

	public void setContribution(int contribution) {
		if (this.contribution != contribution) {
			this.contribution = contribution;
			setOp(DBOption.UPDATE);
		}
	}
	
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append("techId:		").append(techId).append("\n");
		sb.append("techLv:		").append(techLv).append("\n");
		sb.append("contribution:		").append(contribution).append("\n");
		return sb.toString();
	}
}
