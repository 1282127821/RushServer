package com;

import java.util.List;

public class ServerCfgInfo {
	public String designcfg;
	public String logpath;
	public int isGM;
	public int serverIndex;
	public ServerInfo accountServer;
	public ServerInfo gameServer;
	public List<ServerInfo> gateway;
	public DBInfo mainDb;
	public DBInfo logDb;
	public List<ServerInfo> crossserver;
}