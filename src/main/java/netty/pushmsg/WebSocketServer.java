package netty.pushmsg;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.util.concurrent.ImmediateEventExecutor;

public final class WebSocketServer {

    private final ChannelGroup channelGroup = new DefaultChannelGroup(ImmediateEventExecutor.INSTANCE);

    private final NioEventLoopGroup group = new NioEventLoopGroup();

    private Channel channel;

    public ChannelFuture start() {
        ServerBootstrap bootstrap = new ServerBootstrap();
        bootstrap.group(group)
                .channel(NioServerSocketChannel.class)
                .localAddress(8060)
                .childHandler(new WebSocketServerInitializer(channelGroup));
        ChannelFuture future = bootstrap.bind();
        future.syncUninterruptibly();
        channel = future.channel();
        return future;
    }

    public void destory() {
        if (channel != null) {
            channel.close();
        }
        channelGroup.close();
        group.shutdownGracefully();
    }


    public static void main(String[] args) {
        WebSocketServer endpoint = new WebSocketServer();
        ChannelFuture future = endpoint.start();
        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                endpoint.destory();
            }
        });
        future.channel().closeFuture().syncUninterruptibly();

    }

    public static class WebSocketServerInitializer extends ChannelInitializer<Channel> {

        private final ChannelGroup channelGroup;

        public WebSocketServerInitializer(ChannelGroup channelGroup) {
            this.channelGroup = channelGroup;
        }

        @Override
        protected void initChannel(Channel ch) throws Exception {
            ch.pipeline().addLast(
                    new HttpServerCodec(),
                    new HttpObjectAggregator(64 * 1024),
                    new WebSocketServerProtocolHandler("/app/websocket"),
                    new TextWebSocketFrameHandler(channelGroup));

        }
    }
}
