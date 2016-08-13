package com.skill;

import com.db.DBObject;
import com.db.DBOption;
import com.player.GameConst;
import com.util.SplitUtil;

/***
 * 玩家技能链相关
 */
public class SkillChainInfo extends DBObject { 
	/**
	 * 角色技能链
	 */
	private int[] arySkillChain = new int[GameConst.PERSKILLCHAINCOUNT];

	public int[] getArySkillChain() {
		return arySkillChain;
	}

	public void setArySkillChain(int[] arySkillChain) {
		this.arySkillChain = arySkillChain;
		setOp(DBOption.UPDATE);
	}

	public void setAllSkillChain(String strSkillChain) {
		arySkillChain = SplitUtil.splitToInt(strSkillChain);
	}
	
	public String getAllSkillChain() {
		StringBuilder strSkillChain = new StringBuilder();
		for (int i = 0, len = arySkillChain.length; i < len; i++) {
			strSkillChain.append(arySkillChain[i]).append(",");
		}
		return strSkillChain.toString();
	}
	
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append("arySkillChain:		").append(getAllSkillChain()).append("\n");
		return sb.toString();
	}
}
