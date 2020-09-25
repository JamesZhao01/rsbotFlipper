package com.jameszhao.rsbotFlipper.protocol;

import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFactory;
import org.apache.mina.filter.codec.ProtocolDecoder;
import org.apache.mina.filter.codec.ProtocolEncoder;

public class DataCodecFactory implements ProtocolCodecFactory {

    private DataDecoder decoder;
    private DataEncoder encoder;
    public DataCodecFactory() {
        decoder = new DataDecoder();
        encoder = new DataEncoder();
    }
    @Override
    public ProtocolEncoder getEncoder(IoSession ioSession) throws Exception {
        return encoder;
    }

    @Override
    public ProtocolDecoder getDecoder(IoSession ioSession) throws Exception {
        return decoder;
    }
}
