package netty.nettyinaction;


import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.embedded.EmbeddedChannel;
import org.junit.Assert;
import org.junit.Test;

public class FixedLengthFrameDecoderTest {

    @Test
    public void testFramesDecoded() {

        ByteBuf buffer = Unpooled.buffer();
        for (int i = 60; i < 69; i++) {
            buffer.writeByte(i);
        }

        ByteBuf input = buffer.duplicate();
        EmbeddedChannel channel = new EmbeddedChannel(new FixedLengthFrameDecoder(3));

        Assert.assertTrue(channel.writeInbound(input.retain()));
        Assert.assertTrue(channel.finish());

        ByteBuf read = channel.readInbound();
        Assert.assertEquals(buffer.readSlice(3), read);
        read.release();

        read = channel.readInbound();
        Assert.assertEquals(buffer.readSlice(3), read);
        read.release();

        read = channel.readInbound();
        Assert.assertEquals(buffer.readSlice(3), read);
        read.release();

        Assert.assertNull(channel.readInbound());

        buffer.release();


    }

    @Test
    public void testFramesDecode2() {
        ByteBuf buffer = Unpooled.buffer();
        for (int i = 60; i < 69; i++) {
            buffer.writeByte(i);
        }

        ByteBuf input = buffer.duplicate();

        EmbeddedChannel channel = new EmbeddedChannel(new FixedLengthFrameDecoder(3));
        Assert.assertFalse(channel.writeInbound(input.readBytes(2)));
        Assert.assertTrue(channel.writeInbound(input.readBytes(7)));

        Assert.assertTrue(channel.finish());

        ByteBuf read = channel.readInbound();
        Assert.assertEquals(buffer.readSlice(3), read);
        read.release();

        read = channel.readInbound();
        Assert.assertEquals(buffer.readSlice(3), read);
        read.release();

        read = channel.readInbound();
        Assert.assertEquals(buffer.readSlice(3), read);
        read.release();

        Assert.assertNull(channel.readInbound());
        buffer.release();

    }
}
