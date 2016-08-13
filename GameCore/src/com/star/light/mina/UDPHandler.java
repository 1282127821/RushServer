//package com.star.light.mina;
//
//import java.nio.ByteOrder;
//
//import org.apache.mina.core.buffer.IoBuffer;
//import org.apache.mina.core.service.IoHandlerAdapter;
//import org.apache.mina.core.session.IdleStatus;
//import org.apache.mina.core.session.IoSession;
//
//import com.star.light.socket.Command;
//import com.star.light.socket.PBMessage;
//import com.star.light.util.Log;
//
//public class UDPHandler extends IoHandlerAdapter {
//
//	private String handlerName;
//
//	public UDPHandler(String handlerName) {
//		this.handlerName = handlerName;
//	}
//
//	// messageSent是Server响应给Clinet成功后触发的事件
//	@Override
//	public void messageSent(IoSession session, Object message) throws Exception {
//
//	}
//
//	// 抛出异常触发的事件
//
//	@Override
//	public void exceptionCaught(IoSession session, Throwable cause) throws Exception {
//		Log.error("exceptionCaught, " + cause.getMessage());
//		session.closeNow();
//	}
//
//	// Server接收到UDP请求触发的事件
//
//	@Override
//	public void messageReceived(IoSession session, Object message) throws Exception {
//		if (message instanceof IoBuffer) {
//			IoBuffer in = (IoBuffer) message;
//			// 声明这里message必须为IoBuffer类型
//			PBMessage packet = PBMessage.buildPBMessage();
//			in.order(ByteOrder.LITTLE_ENDIAN);
//			packet.readHeader(in);
//
//			// BODY
//			int bodyLen = packet.getLen() - PBMessage.HDR_SIZE;
//			if (bodyLen > 0) {
//				byte[] setMsgBody = new byte[bodyLen];
//				in.get(setMsgBody, 0, bodyLen);
//				packet.setMsgBody(setMsgBody);
//			}
//			messageExce(session, packet);
//		}
//	}
//
//	public void messageExce(IoSession session, Object message) throws Exception {
//		try {
//			PBMessage packet = (PBMessage) message;
//			short code = packet.getCodeId();
//			Command cmd = CommandSet.getCommand(code);
//			if (cmd == null) {
//				Log.warn(toMessage(" Can not found code = " + code + ",drop this packet."));
//				return;
//			}
//			cmd.execute(session, packet);
//		} catch (Exception e) {
//			Log.error(toMessage("Handler execute has exception:"), e);
//		}
//	}
//
//	// 连接关闭触发的事件
//
//	@Override
//	public void sessionClosed(IoSession session) throws Exception {
//	}
//
//	// 建立连接触发的事件
//
//	@Override
//	public void sessionCreated(IoSession session) throws Exception {
//	}
//
//	// 会话空闲
//	@Override
//	public void sessionIdle(IoSession session, IdleStatus status) throws Exception {
//	}
//
//	// 打开连接触发的事件，它与sessionCreated的区别在于，一个连接地址（A）第一次请求Server会建立一个Session默认超时时间为1分钟，此时若未达到超时时间这个连接地址（A）再一次向Server发送请求即是sessionOpened（连接地址（A）第一次向Server发送请求或者连接超时后向Server发送请求时会同时触发sessionCreated和sessionOpened两个事件）
//
//	@Override
//	public void sessionOpened(IoSession session) throws Exception {
//	}
//
//	protected String toMessage(String msg) {
//		return "[" + handlerName + "] " + msg;
//	}
//
//}
