package netty.msgpack;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;
import org.msgpack.MessagePack;

import java.util.List;

/**
 * Created by peng.zhou1 on 2016/12/8.
 */
public class MsgPackDecoder extends MessageToMessageDecoder<ByteBuf> {

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        int len = in.readableBytes();
        byte[] array = new byte[len];
        in.getBytes(in.readerIndex(), array, 0, len);
        MessagePack messagePack = new MessagePack();
        out.add(messagePack.read(array));
    }

}
