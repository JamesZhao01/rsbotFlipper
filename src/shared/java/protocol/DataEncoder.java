package protocol;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolEncoder;
import org.apache.mina.filter.codec.ProtocolEncoderOutput;

public class DataEncoder implements ProtocolEncoder {
    @Override
    public void encode(IoSession ioSession, Object o, ProtocolEncoderOutput protocolEncoderOutput) throws Exception {

        Data data = (Data)o;
        IoBuffer ioBuffer = IoBuffer.allocate(8 + data.getData().length, false);
        ioBuffer.setAutoExpand(true);
        ioBuffer.putInt(data.getMessageType().getId());
        ioBuffer.putInt(data.getData().length);
        ioBuffer.put(data.getData());
        ioBuffer.flip();
        protocolEncoderOutput.write(ioBuffer);
    }

    @Override
    public void dispose(IoSession ioSession) throws Exception {

    }
}
