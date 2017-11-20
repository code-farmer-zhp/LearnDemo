package netty.nettyinaction;


import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.group.ChannelGroup;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.util.SelfSignedCertificate;

public class SecureChatServer extends ChatServer {

    private final SslContext context;

    public SecureChatServer(SslContext context) {
        this.context = context;
    }

    @Override
    protected ChannelInitializer<Channel> createInit(ChannelGroup channelGroup) {
        return new SecureChatServerInitializer(channelGroup, context);
    }

    public static void main(String[] args) throws Exception {
        SelfSignedCertificate cert = new SelfSignedCertificate();
        SslContext context = SslContext.newServerContext(cert.certificate(), cert.privateKey());
        SecureChatServer endpoint = new SecureChatServer(context);
        ChannelFuture future = endpoint.start();
        Runtime.getRuntime().addShutdownHook(new Thread(() -> endpoint.destory()));
        future.channel().closeFuture().syncUninterruptibly();

    }
}
