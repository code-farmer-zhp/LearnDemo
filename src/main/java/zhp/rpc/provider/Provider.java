package zhp.rpc.provider;


import zhp.rpc.Hello;
import zhp.rpc.HelloImpl;

public class Provider<T> {
    private int port;

    private Class<T> interfaces;

    private T impl;

    public Provider<T> setPort(int port) {
        this.port = port;
        return this;
    }

    public Provider<T> setInterfaces(Class<T> interfaces) {
        this.interfaces = interfaces;
        return this;
    }

    public Provider<T> setImpl(T impl) {
        this.impl = impl;
        return this;
    }

    public void build() throws Exception {
        if (port == 0) {
            throw new RuntimeException("port 不能为空");
        }
        if (interfaces == null) {
            throw new RuntimeException("interfaces 不能为空");
        }
        if (impl == null) {
            throw new RuntimeException("impl 不能为空");
        }
        new NettyService(port, interfaces.getName(), impl).start();
    }

    public static void main(String[] args) throws Exception {
        new Provider<Hello>().setPort(8099).setInterfaces(Hello.class).setImpl(new HelloImpl()).build();
    }

}
