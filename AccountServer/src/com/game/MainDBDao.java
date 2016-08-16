package com.game;

import java.sql.Connection;

public class MainDBDao extends DBDao {
	protected Connection openConn() {
		return DBPoolMgr.getInstaqnce().getMainDBConn();
	}
}