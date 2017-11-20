package netty.nettyinaction;


import io.netty.channel.CombinedChannelDuplexHandler;

public class CombinedByteIntegerCodec extends CombinedChannelDuplexHandler<ToIntegerDecoder, ToByteEncoder> {
    public CombinedByteIntegerCodec() {
        super(new ToIntegerDecoder(), new ToByteEncoder());
    }
}
