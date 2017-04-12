package zhp.rpc.consumer;


import zhp.rpc.Hello;

import java.lang.reflect.Proxy;

public class Consumer<T> {
    private Class<T> interfaces;

    public Consumer(Class<T> interfaces) {
        this.interfaces = interfaces;
    }

    public Consumer<T> setInterfaces(Class<T> interfaces) {
        this.interfaces = interfaces;
        return this;
    }

    public T build() {
        return (T) Proxy.newProxyInstance(Consumer.class.getClassLoader(), new Class[]{interfaces}, new ProxyClient(interfaces));
    }

    public static void main(String[] args) {
        Hello build = new Consumer<>(Hello.class).build();
        String zhoupeng = build.say("zhoupeng");
        System.out.println(zhoupeng);
    }
}
