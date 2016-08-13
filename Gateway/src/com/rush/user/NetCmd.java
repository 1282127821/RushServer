package com.rush.user;

import org.apache.mina.core.session.IoSession;

import com.rush.socket.PBMessage;

public interface NetCmd  {
	void execute(IoSession session, PBMessage packet) throws Exception;
}
