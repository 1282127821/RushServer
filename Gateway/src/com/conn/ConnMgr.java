package com.conn;

import java.io.IOException;
import java.net.InetSocketAddress;

import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.executor.ExecutorFilter;
import org.apache.mina.filter.executor.OrderedThreadPoolExecutor;
import org.apache.mina.transport.socket.SocketSessionConfig;
import org.apache.mina.transport.socket.nio.NioSocketAcceptor;

import com.BaseServer;
import com.ServerInfo;
import com.mina.ProtobufStrictCodecFactory;
import com.mina.ProtobufStrictEncoder;
import com.route.ClientRouteHandler;
import com.util.GameLog;

/**
 * 客户端连接管理
 **/
public class ConnMgr {
	private NioSocketAcceptor clientAcceptor;
	private ProtobufStrictCodecFactory codecFactory;

	public boolean init() {
		return reload();
	}

	public boolean reload() {
		// 连接到服务器端
		if (!ClientSet.init()) {
			return false;
		}

		return loadClientConns();
	}

	/**
	 * 初始化客户端连接监听
	 */
	private boolean loadClientConns() {
		try {
			ServerInfo serverInfo = BaseServer.serverCfgInfo.gateway.get(0);
			codecFactory = new ProtobufStrictCodecFactory();
			clientAcceptor = new NioSocketAcceptor(Runtime.getRuntime().availableProcessors() + 1);
			SocketSessionConfig config = clientAcceptor.getSessionConfig();
			config.setReceiveBufferSize(1024 * 64);
			config.setSendBufferSize(1024 * 4);
			config.setSoLinger(0);
			config.setReuseAddress(false);
			config.setTcpNoDelay(true);
			config.setIdleTime(IdleStatus.BOTH_IDLE, 60 * 2);
			clientAcceptor.getFilterChain().addLast("codec", new ProtocolCodecFilter(codecFactory));
			int maximumPoolSize = Runtime.getRuntime().availableProcessors() * 4 + 1;
			OrderedThreadPoolExecutor threadPool = new OrderedThreadPoolExecutor(maximumPoolSize);
			clientAcceptor.getFilterChain().addLast("execute", new ExecutorFilter(threadPool));
			clientAcceptor.setHandler(new ClientRouteHandler());
			clientAcceptor.bind(new InetSocketAddress(serverInfo.port));
		} catch (IOException e) {
			GameLog.error("ConnMgr loadClientConns : ", e);
			return false;
		}
		return true;
	}

	/**
	 * 停服清理网络连接
	 */
	public void stop() {
		try {
			clientAcceptor.unbind();
			clientAcceptor.dispose();
		} catch (Exception e) {
			GameLog.error("ConnMgr stop has exception", e);
		}
	}

	public void setIsRecord(boolean isRecord) {
		try {
			ProtobufStrictEncoder encoder = (ProtobufStrictEncoder) codecFactory.getEncoder(null);
			encoder.setIsRecord(isRecord);
		} catch (Exception e) {
			GameLog.error("加密日志记录设置错误", e);
		}
	}
}