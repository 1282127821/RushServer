package com.db;

import java.sql.Connection;

public class LogDBDao extends DBDao {
	protected Connection openConn() {
		return DBPoolMgr.getInstaqnce().getLogDBConn();
	}
}
