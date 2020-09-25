package protocol;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.CumulativeProtocolDecoder;
import org.apache.mina.filter.codec.ProtocolDecoderOutput;

import java.util.Arrays;

public class DataDecoder extends CumulativeProtocolDecoder {

    @Override
    protected boolean doDecode(IoSession ioSession, IoBuffer ioBuffer, ProtocolDecoderOutput protocolDecoderOutput) throws Exception {
        if (ioBuffer.prefixedDataAvailable(4)) {
            Data.MessageType messageType = Data.MessageType.getById(ioBuffer.getInt());
            int i = ioBuffer.getInt();
            byte[] bytes = new byte[i];
            ioBuffer.get(bytes);
            System.out.println(Arrays.toString(bytes));
            Data data = new Data(bytes, messageType);
            protocolDecoderOutput.write(data);
            return true;
        } else {
            return false;
        }

    }
}
