package com.webservice;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.xml.ws.Endpoint;

import com.BaseServer;
import com.ServerInfo;
import com.sun.net.httpserver.HttpContext;
import com.sun.net.httpserver.HttpServer;

public final class YiShiWSServer {
	private static YiShiWSServer instance = new YiShiWSServer();
	public static YiShiWSServer getInstance() {
		return instance;
	}

	public void start() {
		try {
			ServerInfo serverInfo = BaseServer.serverCfgInfo.accountServer;
			HttpServer server = HttpServer.create(new InetSocketAddress(serverInfo.port), 0);
			ExecutorService threads = Executors.newFixedThreadPool(10);
			server.setExecutor(threads);
			server.start();
			Endpoint castleEndpoint = Endpoint.create(new CastleWS());
			HttpContext castleContext = server.createContext("/castlews");
			castleEndpoint.publish(castleContext);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
