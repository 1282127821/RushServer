package com.star.light.mina;

import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFactory;
import org.apache.mina.filter.codec.ProtocolDecoder;
import org.apache.mina.filter.codec.ProtocolEncoder;

public class ProtobufStrictCodecFactory implements ProtocolCodecFactory {

	private final ProtobufStrictEncoder encoder;
	private final ProtobufStrictDecoder decoder;

	public ProtobufStrictCodecFactory() {
		encoder = new ProtobufStrictEncoder();
		decoder = new ProtobufStrictDecoder();
		encoder.setIsStrict(false);
		decoder.setIsStrict(false);
	}

	@Override
	public ProtocolEncoder getEncoder(IoSession session) throws Exception {
		return encoder;
	}

	@Override
	public ProtocolDecoder getDecoder(IoSession session) throws Exception {
		return decoder;
	}

	/**
	 * <pre>
	 * 获取秘钥key
	 * </pre>
	 * 
	 * @return
	 */
	public static int[] getKeys() {
		return new int[] { 0xae, 0xbf, 0x56, 0x78, 0xab, 0xcd, 0xef, 0xf1 };
	}
}
