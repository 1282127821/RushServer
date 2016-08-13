package com.db;

import java.sql.Connection;

public interface IDBPool {
	Connection getConnection();
	void shutdown();
	String getState();
	int getCurConns();
}
