package zhp.rpc.consumer;

import com.alibaba.fastjson.JSONObject;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.CharsetUtil;
import io.netty.util.ReferenceCountUtil;
import zhp.rpc.provider.RpcResponse;


public class RpcClientHandler extends ChannelInboundHandlerAdapter {
    private RpcConnect rpcConnect;
    private Throwable cause;

    public RpcClientHandler(RpcConnect rpcConnect) {
        this.rpcConnect = rpcConnect;
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf in = (ByteBuf) msg;
        System.out.println("from service received: " + in.toString(CharsetUtil.UTF_8));
        RpcResponse rpcResponse = JSONObject.parseObject(in.toString(CharsetUtil.UTF_8), RpcResponse.class);
        String reqeustId = rpcResponse.getRequestId();
        InvokeFuture invoke = rpcConnect.getInvoker(reqeustId);
        try {
            if (cause != null) {
                invoke.setCause(cause);
            } else {
                Throwable exception = rpcResponse.getException();
                if (exception == null) {
                    String result = rpcResponse.getResult();
                    Object o = JSONObject.parseObject(result, rpcResponse.getResultType());
                    invoke.setResult(o);
                } else {
                    invoke.setCause(exception);
                }
            }
        } finally {
            rpcConnect.removeInvoker(reqeustId);
            ReferenceCountUtil.release(msg);
        }

    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        //冲刷所有待审消息到远程节点。关闭通道后，操作完成
        ctx.writeAndFlush(Unpooled.EMPTY_BUFFER);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        this.cause = cause;
        cause.printStackTrace();
    }
}
