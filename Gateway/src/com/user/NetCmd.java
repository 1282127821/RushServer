package com.user;

import com.netmsg.PBMessage;

import io.netty.channel.Channel;

public interface NetCmd  {
	void execute(Channel channel, PBMessage packet) throws Exception;
}
