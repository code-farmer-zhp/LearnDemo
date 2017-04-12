package zhp.rpc.provider;

import com.alibaba.fastjson.JSONObject;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.ReferenceCountUtil;
import zhp.rpc.consumer.RpcRequest;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

//标识这类的实例之间可以在 channel 里面共享
@ChannelHandler.Sharable
public class NettyServerHandler extends ChannelInboundHandlerAdapter {

    private Map<String, Object> impls = new ConcurrentHashMap<>();

    public void addImpl(String key, Object value) {
        impls.put(key, value);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        System.out.println("received");
        RpcResponse rpcResponse = new RpcResponse();
        try {
            String in = (String) msg;
            //System.out.println("Server received: " + in);
            //将所接收的消息返回给发送者。注意，这还没有冲刷数据 异步写
            RpcRequest rpcRequest = JSONObject.parseObject(in, RpcRequest.class);
            Class clazz = rpcRequest.getClazz();
            Object o = impls.get(clazz.getName());
            Method method = o.getClass().getMethod(rpcRequest.getMethodName(), rpcRequest.getParamTypes());
            Object invoke = method.invoke(o, rpcRequest.getArgs());

            rpcResponse.setRequestId(rpcRequest.getRequestId());
            rpcResponse.setResult(JSONObject.toJSONString(invoke));
            rpcResponse.setResultType(invoke.getClass());
        } catch (Exception e) {
            e.printStackTrace();
            rpcResponse.setException(e);
        }finally {
            ReferenceCountUtil.release(msg);
        }
        String responseStr = JSONObject.toJSONString(rpcResponse) + System.lineSeparator();
        ctx.writeAndFlush(Unpooled.copiedBuffer(responseStr.getBytes()));
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        //冲刷所有待审消息到远程节点。关闭通道后，操作完成
        ctx.flush();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
