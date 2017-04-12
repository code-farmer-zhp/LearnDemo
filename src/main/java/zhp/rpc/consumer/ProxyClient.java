package zhp.rpc.consumer;


import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.UUID;

public class ProxyClient implements InvocationHandler {
    private NettyRpcConnect rpcConnect = new NettyRpcConnect("localhost", 8099);
    private Class clazz;

    public ProxyClient(Class clazz) {
        this.clazz = clazz;
    }

    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        RpcRequest rpcRequest = new RpcRequest();
        rpcRequest.setRequestId(UUID.randomUUID().toString());
        rpcRequest.setClazz(clazz);
        rpcRequest.setMethodName(method.getName());
        rpcRequest.setParamTypes(method.getParameterTypes());
        rpcRequest.setArgs(args);
        return rpcConnect.send(rpcRequest, true);
    }
}
