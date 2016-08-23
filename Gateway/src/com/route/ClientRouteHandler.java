package com.route;

import org.apache.mina.core.session.IoSession;

import com.GatewayServer;
import com.conn.SessionType;
import com.network.LinkedClient;
import com.user.UserMgr;
import com.util.GameLog;

/**
 * 客户端数据包转发（只处理客户端数据包）
 */
public class ClientRouteHandler extends RouteHandler {
	public ClientRouteHandler() {
		super("client conn");
	}

	@Override
	public void sessionOpened(IoSession session) throws Exception {
		if (GatewayServer.getInstance().isTerminate()) {
			session.closeNow();
			GameLog.error("isTerminate session.close");
			return;
		}
		session.setAttribute(LinkedClient.SESSION_TYPE, SessionType.GAME_CLIENT);
		super.sessionOpened(session);
	}
	
	@Override
	public void sessionClosed(IoSession session) throws Exception {
		// 从在线玩家列表中,将该user移除
		if (session.getAttribute(LinkedClient.KEY_ID) != null) {
			long clientId = (Long) session.getAttribute(LinkedClient.KEY_ID);
			UserMgr.removeOnline(clientId, session);
		}
				
		// 从临时玩家session列表中，将该session移除
		if (session.getAttribute(LinkedClient.TEMP_SESSION_USER_ID) != null) {
			long userId = (Long) session.getAttribute(LinkedClient.TEMP_SESSION_USER_ID);
			UserMgr.removeTempSession(userId, session);
		}
	}
}
