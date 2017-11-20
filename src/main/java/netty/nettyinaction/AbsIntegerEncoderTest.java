package netty.nettyinaction;


import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.embedded.EmbeddedChannel;
import org.junit.Assert;
import org.junit.Test;

public class AbsIntegerEncoderTest {

    @Test
    public void testEncoded() {
        ByteBuf buffer = Unpooled.buffer();
        for (int i = 1; i < 10; i++) {
            buffer.writeInt(i * -1);
        }

        EmbeddedChannel channel = new EmbeddedChannel(new AbsIntegerEncoder());

        Assert.assertTrue(channel.writeOutbound(buffer));
        Assert.assertTrue(channel.finish());


        for (int i = 1; i < 10; i++) {
            int t = channel.readOutbound();
            Assert.assertEquals(i, t);
        }

        Assert.assertNull(channel.readOutbound());
    }
}
