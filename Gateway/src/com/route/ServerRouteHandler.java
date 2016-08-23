package com.route;

import org.apache.mina.core.session.IoSession;

import com.conn.ClientTryer;
import com.conn.SessionType;
import com.network.LinkedClient;
import com.util.GameLog;

/**
 * 处理数据包的转发
 */
public class ServerRouteHandler extends RouteHandler {
	public ServerRouteHandler(String handName) {
		super(handName);
	}

	@Override
	public void sessionOpened(IoSession session) throws Exception {
		GameLog.info("ServerRouteHandler RemoteAddress " + session.getRemoteAddress().toString());
		session.setAttribute(LinkedClient.SESSION_TYPE, SessionType.GAME_SERVER);
	}
	
	@Override
	public void sessionClosed(IoSession session) throws Exception {
		GameLog.error(handName + " conn force closed.");
		Object obj = session.getAttribute(LinkedClient.KEY_NO_RETRY);
		if (obj != null) {
			boolean noRetry = (Boolean) obj;
			if (noRetry) {
				return;
			}
		}
		handleDispose(session);
	}

	@Override
	public void exceptionCaught(IoSession session, Throwable cause) throws Exception {
		super.exceptionCaught(session, cause);
		GameLog.error(handName + " conn has exception:", cause);
	}

	private void handleDispose(IoSession session) {
		LinkedClient client = (LinkedClient) session.getAttribute(LinkedClient.KEY_CLIENT);
		if(client != null) {			
			ClientTryer.getInstance().ctry(client, 10, -1);
		}
	}
}
