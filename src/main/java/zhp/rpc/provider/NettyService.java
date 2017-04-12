package zhp.rpc.provider;


import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.LineBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;

import java.net.InetSocketAddress;

public class NettyService {
    private int port;
    private String name;
    private Object impl;

    public NettyService(int port, String name, Object impl) {
        this.port = port;
        this.name = name;
        this.impl = impl;
    }

    public void start() throws Exception {
        NioEventLoopGroup group = new NioEventLoopGroup();//处理事件的处理，如接受新的连接和读/写数据。
        //可以共享
        final NettyServerHandler nettyServerHandler = new NettyServerHandler();
        nettyServerHandler.addImpl(name, impl);
        try {
            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.group(group)
                    .channel(NioServerSocketChannel.class)//指定使用 NIO 的传输 Channel
                    .localAddress(new InetSocketAddress(port))//设置 socket 地址使用所选的端口
                    .childHandler(new ChannelInitializer<SocketChannel>() {//添加 EchoServerHandler 到 Channel 的 ChannelPipeline
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            socketChannel.pipeline().addLast(new LineBasedFrameDecoder(Integer.MAX_VALUE));
                            socketChannel.pipeline().addLast(new StringDecoder());
                            socketChannel.pipeline().addLast(nettyServerHandler);
                        }
                    });
            ChannelFuture f = bootstrap.bind().sync();//同步等待绑定的服务器
            System.out.println(NettyService.class.getName() + " started and listen on" + f.channel().localAddress());
            f.channel().closeFuture().sync();//等待关闭
        } finally {
            group.shutdownGracefully().sync();
        }
    }
}
