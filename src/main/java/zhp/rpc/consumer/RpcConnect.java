package zhp.rpc.consumer;

public interface RpcConnect {
    Object send(RpcRequest request, boolean async);

    InvokeFuture getInvoker(String requestId);

    void removeInvoker(String reqeustId);
}
