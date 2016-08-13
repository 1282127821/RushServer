package com.user;

import org.apache.mina.core.session.IoSession;

import com.netmsg.PBMessage;

public interface NetCmd  {
	void execute(IoSession session, PBMessage packet) throws Exception;
}
