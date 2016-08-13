package com.star.light.game;

import java.sql.Connection;

public class MainDBDao extends BaseDao {
	protected Connection openConn() {
		return DBPoolMgr.getInstaqnce().getMainDBConn();
	}
}