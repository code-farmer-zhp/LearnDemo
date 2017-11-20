package netty.nettyinaction;


import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.embedded.EmbeddedChannel;
import io.netty.handler.codec.TooLongFrameException;
import org.junit.Assert;
import org.junit.Test;

public class FrameChunkDecoderTest {

    @Test
    public void testFramesDecoded() {
        ByteBuf buffer = Unpooled.buffer();
        for (int i = 0; i < 9; i++) {
            buffer.writeByte(i);
        }

        ByteBuf input = buffer.duplicate();

        EmbeddedChannel channel = new EmbeddedChannel(new FrameChunkDecoder(3));

        Assert.assertTrue(channel.writeInbound(input.readBytes(2)));

        try {
            channel.writeInbound(input.readBytes(4));
            Assert.fail();
        } catch (TooLongFrameException e) {
            e.printStackTrace();
        }

        Assert.assertTrue(channel.writeInbound(input.readBytes(3)));
        Assert.assertTrue(channel.finish());

        ByteBuf read = channel.readInbound();
        Assert.assertEquals(buffer.readSlice(2), read);
        read.release();

        read = channel.readInbound();
        Assert.assertEquals(buffer.skipBytes(4).readSlice(3), read);
        read.release();
        buffer.release();

    }
}
