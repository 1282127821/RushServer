package com.rush;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import org.apache.log4j.PropertyConfigurator;
import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.executor.ExecutorFilter;
import org.apache.mina.filter.executor.OrderedThreadPoolExecutor;
import org.apache.mina.transport.socket.DatagramSessionConfig;
import org.apache.mina.transport.socket.SocketAcceptor;
import org.apache.mina.transport.socket.SocketSessionConfig;
import org.apache.mina.transport.socket.nio.NioDatagramAcceptor;
import org.apache.mina.transport.socket.nio.NioSocketAcceptor;

import com.alibaba.fastjson.JSON;
import com.rush.mina.ProtobufCodecFactory;
import com.rush.util.GameLog;
import com.rush.util.IDWorker;

public abstract class BaseServer {
	protected static String configPath;
	private boolean terminate = true;
	private static SocketAcceptor acceptor;
	public static ServerCfgInfo serverCfgInfo = null;
	public static IDWorker IDWORK = new IDWorker(1);

	public boolean start() {
		if (!initComponent(initServerCfg(configPath), "加载服务器配置文件")) {
			return false;
		}

		PropertyConfigurator.configure(serverCfgInfo.gameServer.logpath);

		return true;
	}

	public static boolean initServerCfg(String filaPathName) {
		try {
			FileInputStream fs = new FileInputStream(filaPathName);
			InputStreamReader isr = new InputStreamReader(fs, "UTF-8");
			BufferedReader reader = new BufferedReader(isr);
			StringBuilder serverJson = new StringBuilder();
			String jsonStr = null;
			while ((jsonStr = reader.readLine()) != null) {
				serverJson.append(jsonStr);
			}

			serverCfgInfo = JSON.parseObject(serverJson.toString(), ServerCfgInfo.class);
			reader.close();
			fs.close();
			return true;
		} catch (Exception e) {
			GameLog.info("加载游戏服务器配置出错" + e.getMessage());
			return false;
		}
	}
	
	/**
	 * 根据玩家Id获得对应的网关Id
	 */
	public static int getGateWayId(long accountId) {
		return (int) (accountId % serverCfgInfo.gateway.size());
	}

	/***
	 * 得到网关的大小
	 */
	public static int getGateSize() {
		return serverCfgInfo.gateway.size();
	}

	public boolean isTerminate() {
		return terminate;
	}

	public void setTerminate(boolean terminate) {
		this.terminate = terminate;
	}

	public boolean stop() {
		acceptor.unbind();
		if (acceptor.isDisposed()) {
			acceptor.dispose();
		}
		System.exit(0);
		return true;
	}

	/**
	 * 初始化相关模块
	 */
	public boolean initComponent(boolean initResult, String componentName) {
		if (!initResult) {
			GameLog.error(componentName + "错误");
		} else {
			GameLog.info(componentName + "加载完成");
		}
		return initResult;
	}

	/**
	 * 初始化战斗服务器和网关服务器连接
	 */
	public boolean initMina(IoHandlerAdapter ioHandlerAdapter, ServerInfo serverInfo) {
		try {
			acceptor = new NioSocketAcceptor(Runtime.getRuntime().availableProcessors() + 1);
			acceptor.getFilterChain().addLast("codec", new ProtocolCodecFilter(new ProtobufCodecFactory()));
			int maximumPoolSize = Runtime.getRuntime().availableProcessors() * 4 + 1;
			OrderedThreadPoolExecutor threadPool = new OrderedThreadPoolExecutor(maximumPoolSize);
			acceptor.getFilterChain().addLast("executor", new ExecutorFilter(threadPool));
			SocketSessionConfig config = acceptor.getSessionConfig();
			config.setSendBufferSize(4096);
			config.setReadBufferSize(4096);
			config.setIdleTime(IdleStatus.BOTH_IDLE, 10);
			config.setReuseAddress(false);
			config.setSoLinger(5);// 5秒
			config.setTcpNoDelay(true);
			acceptor.setHandler(ioHandlerAdapter);
			acceptor.bind(new InetSocketAddress(serverInfo.port));
			GameLog.info("启动服务器监听成功, port: " + serverInfo.port + ", adminPort: " + serverInfo.adminPort);
			return true;
		} catch (Exception e) {
			GameLog.error("启动服务器监听失败, port: " + serverInfo.port + ", adminPort: " + serverInfo.adminPort, e);
		}
		return false;
	}

	public boolean initUDP(IoHandlerAdapter ioHandlerAdapter, int port) {
		try {
			NioDatagramAcceptor acceptor = new NioDatagramAcceptor();// 创建一个UDP的接收器
			acceptor.setHandler(ioHandlerAdapter);// 设置接收器的处理程序
			Executor threadPool = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors() * 4 + 1);
			acceptor.getFilterChain().addLast("exector", new ExecutorFilter(threadPool));
			DatagramSessionConfig dcfg = acceptor.getSessionConfig();// 建立连接的配置文件
			dcfg.setReadBufferSize(4096);// 设置接收最大字节默认2048
			dcfg.setReceiveBufferSize(1024);// 设置输入缓冲区的大小
			dcfg.setSendBufferSize(1024);// 设置输出缓冲区的大小
			dcfg.setReuseAddress(true);// 设置每一个非主监听连接的端口可以重用

			acceptor.bind(new InetSocketAddress(port));// 绑定端口
			GameLog.info("启动udp服务器监听成功, port: " + port);
			return true;
		} catch (Exception e) {
			GameLog.error("启动udp服务器监听失败, port: ", e);
		}
		return false;
	}
}
