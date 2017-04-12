package netty;


import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import netty.msgpack.MsgPackDecoder;
import netty.msgpack.MsgPackEncoder;

import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.List;

public class TimeClient {

    public void connect(int port) {
        NioEventLoopGroup group = new NioEventLoopGroup();
        try {
            Bootstrap bootstrap = new Bootstrap();
            bootstrap.group(group)
                    .channel(NioSocketChannel.class)
                    .remoteAddress(new InetSocketAddress("localhost", port))
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ByteBuf byteBuf = Unpooled.copiedBuffer("$_".getBytes());
                            //ch.pipeline().addLast(new DelimiterBasedFrameDecoder(1024,byteBuf));
                            //ch.pipeline().addLast(new StringDecoder());
                            ch.pipeline().addLast("frameDecoder",new LengthFieldBasedFrameDecoder(65535,0,2,0,2));
                            ch.pipeline().addLast("msgpackDecoder", new MsgPackDecoder());
                            //增加两个字节头信息
                            ch.pipeline().addLast("frameEncode",new LengthFieldPrepender(2));
                            ch.pipeline().addLast("msgpackEncode", new MsgPackEncoder());
                            ch.pipeline().addLast(new TimClientHandler());
                        }
                    });
            ChannelFuture sync = bootstrap.connect().sync();
            sync.channel().closeFuture().sync();
        } catch (Exception e) {
            e.printStackTrace();
            group.shutdownGracefully();
        }

    }

    public static void main(String[] args) {
        new TimeClient().connect(8699);
    }
}

class TimClientHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        List<UserInfo> userInfos = userInfos();
        for (UserInfo userInfo : userInfos) {
            System.out.println(userInfo);
            ctx.write(userInfo);
        }
        ctx.flush();
    }


    private List<UserInfo> userInfos() {
        List<UserInfo> list = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            UserInfo userInfo = new UserInfo();
            userInfo.setAge(i);
            userInfo.setName("zhp" + i);
            list.add(userInfo);
        }
        return list;
    }


    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        System.out.println("client:" + msg);
        ctx.write(msg);

    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.flush();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}


