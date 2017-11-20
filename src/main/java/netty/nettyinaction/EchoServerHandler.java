package netty.nettyinaction;


import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.CharsetUtil;

import java.net.InetSocketAddress;
import java.util.concurrent.TimeUnit;

@ChannelHandler.Sharable
public class EchoServerHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf in = (ByteBuf) msg;
        System.out.println("Server received:" + in.toString(CharsetUtil.UTF_8));

        //ByteBufAllocator alloc = ctx.channel().alloc();
        //ByteBufAllocator alloc1 = ctx.alloc();

        EventLoop eventExecutors = ctx.channel().eventLoop();
        eventExecutors.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                System.out.println("good");
            }
        }, 60, 60, TimeUnit.SECONDS);
        ctx.pipeline();
        ctx.write(in);

        //连接第三方服务
        Bootstrap bootstrap = new Bootstrap();
        bootstrap.channel(NioSocketChannel.class)
                .handler(new SimpleChannelInboundHandler<ByteBuf>() {
                    @Override
                    protected void channelRead0(ChannelHandlerContext ctx, ByteBuf msg) throws Exception {

                    }
                });
        //TODO 复用eventLoop
        bootstrap.group(ctx.channel().eventLoop());

        bootstrap.connect(new InetSocketAddress("localhost", 80));

    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.writeAndFlush(Unpooled.EMPTY_BUFFER).addListener(ChannelFutureListener.CLOSE);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
