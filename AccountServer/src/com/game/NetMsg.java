package com.game;

import io.netty.channel.Channel;

public interface NetMsg {
	void execute(Channel channel, PBMessage packet) throws Exception;
}
