package zhp.rpc.consumer;


import com.alibaba.fastjson.JSONObject;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LineBasedFrameDecoder;

import java.net.InetSocketAddress;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class NettyRpcConnect implements RpcConnect {

    private static Map<String, InvokeFuture> futrues = new ConcurrentHashMap<>();

    private Channel channel;

    public NettyRpcConnect(String ip, int port) {
        InetSocketAddress inetSocketAddress = new InetSocketAddress(ip, port);
        EventLoopGroup group = new NioEventLoopGroup();
        final RpcClientHandler rpcClientHandler = new RpcClientHandler(this);
        try {
            Bootstrap bootstrap = new Bootstrap();
            bootstrap.group(group)
                    .channel(NioSocketChannel.class)
                    .remoteAddress(inetSocketAddress)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        public void initChannel(SocketChannel socketChannel) throws Exception {
                            socketChannel.pipeline().addLast(new LineBasedFrameDecoder(Integer.MAX_VALUE));
                            socketChannel.pipeline().addLast(rpcClientHandler);
                        }
                    }).option(ChannelOption.SO_KEEPALIVE, true);
            ChannelFuture future = bootstrap.connect().sync();
            channel = future.channel();
        } catch (Exception e) {
            //创建连接失败
            e.printStackTrace();
            try {
                group.shutdownGracefully().sync();
            } catch (Exception ex) {
                //释放资源失败
                ex.printStackTrace();
            }
            throw new RuntimeException(e);
        }

    }

    public Object send(RpcRequest request, boolean async) {
        final InvokeFuture invokeFuture = new InvokeFuture();
        futrues.put(request.getRequestId(), invokeFuture);
        //System.out.println("send requet" + JSONObject.toJSONString(request));
        String requestStr = JSONObject.toJSONString(request) + System.lineSeparator();
        ChannelFuture channelFuture = channel.writeAndFlush(Unpooled.copiedBuffer(requestStr.getBytes()));
        channelFuture.addListener((ChannelFutureListener) future -> {
            if (!future.isSuccess()) {
                future.cause().printStackTrace();
                invokeFuture.setCause(future.cause());
            }
        });
        return invokeFuture.getResult();
    }

    public InvokeFuture getInvoker(String requestId) {
        return futrues.get(requestId);
    }

    public void removeInvoker(String reqeustId) {
        futrues.remove(reqeustId);
    }
}
