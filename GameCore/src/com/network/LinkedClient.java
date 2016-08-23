package com.network;

import com.netmsg.PBMessage;
import com.util.GameLog;

import io.netty.channel.Channel;

/**
 * 服务器和网关服务器之间的连接
 */
public class LinkedClient {
	public static final String TEMP_SESSION_USER_ID = "TEMP_SESSION_USER_ID";
	public static final String TEMP_SESSION_KEY = "TEMP_SESSION_KEY";
	public static final String KEY_ID = "KEY_CLIENT_ID";
	public static final String KEY_CLIENT = "KEY_CLIENT";
	public static final String KEY_NO_RETRY = "NO_RETRY";
	public static final String SESSION_TYPE = "SESSION_TYPE";

	private int type; // 连接类型
	private String address;
	private int port;
	private boolean isTry;
	private int connTimes;
	private Channel channel = null; // 连接会话

	public LinkedClient(int type, String address, int port) {
		this.type = type;
		this.address = address;
		this.port = port;
		this.isTry = true;
		this.connTimes = 0;
	}

	public String getAddress() {
		return address;
	}

	public int getPort() {
		return port;
	}

	public void setChannel(Channel channel) {
		this.channel = channel;
	}

	public Channel getChannel() {
		return channel;
	}

	public int getType() {
		return type;
	}

	public boolean isTry() {
		return isTry;
	}

	public void setTry(boolean isTry) {
		this.isTry = isTry;
	}

	public int getConnTimes() {
		return connTimes;
	}

	public void resetConnTimes() {
		connTimes = 0;
	}

	public boolean isLinkedClient(String address, int port) {
		return this.address.equals(address) && this.port == port;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(", address : ").append(address);
		sb.append(", port : ").append(port);
		sb.append(", connTimes : ").append(connTimes);
		sb.append(", : ").append(channel.remoteAddress().toString());
		return sb.toString();
	}

	/**
	 * 是否连接
	 */
	public synchronized boolean isConnected() {
		return channel != null && channel.isActive();
	}

	/**
	 * 连接
	 */
	public synchronized boolean connect() {
		try {
//			connector = new NioSocketConnector(Runtime.getRuntime().availableProcessors() + 1);
//			connector.setConnectTimeoutMillis(1000 * 8);
//			connector.setHandler(handler);
//			connector.getFilterChain().addLast("codec", new ProtocolCodecFilter(new ProtobufCodecFactory()));
//			connector.getFilterChain().addLast("executor", new ExecutorFilter(Executors.newSingleThreadExecutor(new ThreadFactory() {
//				public Thread newThread(Runnable r) {
//					Thread thread = new Thread(r, address + "-" + port + "-thread");
//					return thread;
//				}
//			})));
//
//			ConnectFuture future = connector.connect(new InetSocketAddress(address, port));
//			future.awaitUninterruptibly(1000 * 10);
//			session = future.getSession();
//			session.setAttribute(KEY_CLIENT, this);
//			connTimes = 0;
			return true;
		} catch (Exception e) {
//			connector.dispose();
//			connector = null;
			GameLog.error("connect to address " + address + ":" + port + " fail.", e);
			connTimes++;
			return false;
		}
	}

	public synchronized void disConnect() {
		if (isConnected()) {
			channel.close();
			channel = null;
		}

//		if (connector != null) {
//			connector.dispose();
//			connector = null;
//		}
	}

	/**
	 * 发送数据包
	 */
	public void send(PBMessage packet) {
		try {
			if (channel != null && channel.isActive()) {
				channel.writeAndFlush(packet);
			}
		} catch (Exception e) {
			GameLog.warn("session write error . packet : " + packet.toString(), e);
		}
	}
}
