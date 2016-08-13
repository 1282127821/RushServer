package com.star.light.user;

import org.apache.mina.core.session.IoSession;

import com.star.light.socket.PBMessage;

public interface NetCmd  {
	void execute(IoSession session, PBMessage packet) throws Exception;
}
