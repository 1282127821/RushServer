package com.game;

import io.netty.channel.ChannelHandlerContext;

public interface NetMsg {
	void execute(ChannelHandlerContext ctx, PBMessage packet) throws Exception;
}
