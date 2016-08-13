package com.rush.route;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IoSession;

import com.rush.conn.ClientSet;
import com.rush.socket.PBMessage;
import com.rush.user.NetCmd;
import com.rush.user.NetMsgMgr;
import com.rush.user.User;
import com.rush.user.UserMgr;
import com.rush.util.GameLog;

/**
 * 数据包转发处理器
 */
public abstract class RouteHandler extends IoHandlerAdapter {
	protected String handName;
	private static final NetMsgMgr netInstance = NetMsgMgr.getInstance();
	private static LinkedBlockingQueue<Runnable> workQueue = new LinkedBlockingQueue<Runnable>();
	private static RejectedExecutionHandler handler = new ThreadPoolExecutor.DiscardPolicy();
	private static ThreadPoolExecutor pool = new ThreadPoolExecutor(4, 8, 5, TimeUnit.MINUTES, workQueue, handler);

	public RouteHandler(String handName) {
		this.handName = handName;
	}

	//TODO:LZGLZG网络消息这块考虑把网关跟客户端  网关和服务器的进行拆分，GC两个之间不需要有UserIdd带着，只需要通过连接来关联就可以了，
	//但是GS之间的消息则需要带上UserId，因为要对消息做队列的拆分
	@Override
	public void messageReceived(IoSession session, Object message) throws Exception {
		// TODO:LZGLZG这里通过SessionType来做区分是从服务器发送给客户端  还是从客户端发送给服务器
//		int sessionType = (Integer)session.getAttribute(LinkedClient.SESSION_TYPE);
//		if (sessionType == SessionType.GAME_CLIENT) {
//			
//		} else if (sessionType == SessionType.GAME_SERVER) {
//			
//		}
//		GameLog.error("Session Type : " + session.getAttribute(LinkedClient.SESSION_TYPE));
		PBMessage packet = (PBMessage) message;
		long userId = packet.getUserId();
		short code = packet.getCodeId();
		if (code > 0 && code < 5000) {
			routeClient(userId, packet);
		} else if (code > 5001 && code < 10000) {
			ClientSet.routeServer(packet);
		} else if (code >= 20001 && code < 25000) {
			handle(session, code, packet);
		} else {
			GameLog.error("Illegal Packet. packet header : " + packet.headerToStr());
		}
	}

	/**
	 * 网关上需要解包协议处理
	 */
	private void handle(IoSession session, short codeId, PBMessage packet) {
		NetCmd netCmd = netInstance.getNetCmd(codeId);
		if (netCmd == null) {
			GameLog.error("not found cmd , code: 0x" + Integer.toHexString(codeId) + " , userId : " + packet.getUserId());
			return;
		}
		
		try {
			pool.execute(new CmdTask(netCmd, session, packet));
		} catch (Exception e) {
			e.printStackTrace();
			GameLog.error("code = " + codeId + " has exception:", e);
		}
	}

	/**
	 * 转发数据包到客户端
	 */
	private void routeClient(long userId, PBMessage packet) {
		User user = UserMgr.getOnlineUser(userId);
		if (user == null) {
			GameLog.error("找不到对应的用户, userId: " + userId + ", code: " + packet.getCodeId());
			return;
		}
		
		user.sendToClient(packet);
	}
	
	@Override
	public void exceptionCaught(IoSession session, Throwable cause) throws Exception {
		session.closeNow();
		GameLog.error("exceptionCaught session   " + cause.getMessage());
	}
}
