package com.db;

import java.sql.Connection;

//TODO:LZGLZG 对于数据库的更新有一种实验性的想法，把需要更新的数据库脚本进行打包起来，一起批量执行，另外可以单独起一个线程来执行这个操作
//这样可以避免IO的操作影响到游戏的逻辑操作
public class MainDBDao extends DBDao {
	protected Connection openConn() {
		return DBPoolMgr.getInstaqnce().getMainDBConn();
	}
}