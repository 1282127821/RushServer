package com.mina;

import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.CumulativeProtocolDecoder;
import org.apache.mina.filter.codec.ProtocolCodecFactory;
import org.apache.mina.filter.codec.ProtocolEncoderAdapter;

/**
 * ProtocolCodecFactory
 */
public class ProtobufCodecFactory implements ProtocolCodecFactory {
	private static ProtocolEncoderAdapter ENCODER_INSTANCE = new ProtobufEncoder();
	private static CumulativeProtocolDecoder DECODER_INSTANCE = new ProtobufDecoder();

	public CumulativeProtocolDecoder getDecoder(IoSession session) throws Exception {
		return DECODER_INSTANCE;
	}

	public ProtocolEncoderAdapter getEncoder(IoSession session) throws Exception {
		return ENCODER_INSTANCE;
	}
}
