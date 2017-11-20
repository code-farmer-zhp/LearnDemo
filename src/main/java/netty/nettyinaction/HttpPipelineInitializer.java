package netty.nettyinaction;


import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.handler.codec.http.HttpRequestDecoder;
import io.netty.handler.codec.http.HttpRequestEncoder;
import io.netty.handler.codec.http.HttpResponseDecoder;
import io.netty.handler.codec.http.HttpResponseEncoder;

public class HttpPipelineInitializer extends ChannelInitializer<Channel> {

    private final boolean client;

    public HttpPipelineInitializer(boolean client) {
        this.client = client;
    }

    @Override
    protected void initChannel(Channel ch) throws Exception {
        if (client) {
            ch.pipeline().addLast("encoder", new HttpRequestEncoder());
            ch.pipeline().addLast("decoder", new HttpResponseDecoder());
        } else {
            ch.pipeline().addLast("encoder", new HttpResponseEncoder());
            ch.pipeline().addLast("decoder", new HttpRequestDecoder());
        }

    }
}
