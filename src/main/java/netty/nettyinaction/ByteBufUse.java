package netty.nettyinaction;


import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.buffer.CompositeByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.util.ByteProcessor;

import java.nio.charset.Charset;
import java.util.Arrays;

public class ByteBufUse {
    public static void main(String[] args) {
        ByteBuf heapBuf = Unpooled.copiedBuffer("abc", Charset.defaultCharset());
        //是否有一个支撑数组
        if (heapBuf.hasArray()) {
            byte[] array = heapBuf.array();
            int offset = heapBuf.arrayOffset() + heapBuf.readerIndex();
            int length = heapBuf.readableBytes();
            System.out.println("array:" + Arrays.toString(array) + " offset:" + offset + " length:" + length);
        }

        ByteBuf directBuf = Unpooled.directBuffer();
        byte[] bytes = "def".getBytes();
        for (int i = 0; i < bytes.length; i++) {
            directBuf.writeByte(bytes[i]);
        }
        if (!directBuf.hasArray()) {
            int length = directBuf.readableBytes();
            byte[] array = new byte[length];
            directBuf.getBytes(directBuf.readerIndex(), array);
            System.out.println("array:" + Arrays.toString(array));
        }

        CompositeByteBuf compositeBuffer = Unpooled.compositeBuffer();
        compositeBuffer.addComponents(heapBuf, directBuf);


        int length = compositeBuffer.capacity();
        System.out.println(length);
        byte[] array = new byte[length];
        compositeBuffer.getBytes(compositeBuffer.readerIndex(), array);
        System.out.println(Arrays.toString(array));


        compositeBuffer.forEachByte(ByteProcessor.FIND_CR);

        Charset utf8 = Charset.forName("UTF-8");
        ByteBuf byteBuf = Unpooled.copiedBuffer("Netty in action rocks!", utf8);
        ByteBuf slice = byteBuf.slice(0, 15);
        System.out.println(slice.toString(utf8));
        byteBuf.setByte(0, 'J');
        System.out.println(byteBuf.getByte(0));
        System.out.println(slice.getByte(1));
        assert byteBuf.getByte(0) == slice.getByte(0);
        System.out.println("no here");

        Channel channel = null;
        ByteBufAllocator alloc = channel.alloc();
        ByteBuf buffer = alloc.buffer();
        buffer.refCnt();


    }
}
