package com;

import java.util.List;

public class ServerCfgInfo
{
	public String designcfgPath;
	public String logPath;
	public boolean isGM;
	public int serverIndex;
	public ServerInfo accountServer;
	public ServerInfo gameServer;
	public ServerInfo gatewayServer;
	public DBInfo mainDb;
	public DBInfo logDb;
	public List<ServerInfo> crossserver;
}