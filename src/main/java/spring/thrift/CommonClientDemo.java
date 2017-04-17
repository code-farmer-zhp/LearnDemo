package spring.thrift;

import thrift.micmiu.thrift.demo.HelloWorldService;
import org.apache.thrift.TException;
import org.apache.thrift.protocol.TCompactProtocol;
import org.apache.thrift.protocol.TMultiplexedProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.transport.TFramedTransport;
import org.apache.thrift.transport.TTransport;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.cglib.proxy.InvocationHandler;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.springframework.cglib.proxy.Proxy.newProxyInstance;

public class CommonClientDemo<T> implements FactoryBean<T> {

    private ConnectionManager connectionManager;

    private Class interfaces;

    private Constructor<?> constructor;

    private String serviceName;

    public CommonClientDemo(String hostName, int port, int timeOut, Class serviceClazz) {
        try {
            ConnectionProviderImpl connectionProvider = new ConnectionProviderImpl();
            connectionProvider.setIp(hostName);
            connectionProvider.setPort(port);
            connectionProvider.setReadTimeout(timeOut);
            connectionProvider.init();

            connectionManager = new ConnectionManager();
            connectionManager.setConnectionProvider(connectionProvider);
            serviceName = serviceClazz.getName();
            //获得service里面的Iface 接口
            interfaces = Class.forName(serviceName + "$Iface");
            //获得service里面的Client类
            Class<?> clientClass = Class.forName(serviceName + "$Client");
            constructor = clientClass.getConstructor(TProtocol.class);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public T getObject() {
        try {
            //创建代理
            ClassLoader classLoader = this.getClass().getClassLoader();
            return (T) newProxyInstance(classLoader, new Class[]{interfaces}, new InvocationHandler() {
                @Override
                public Object invoke(Object o, Method method, Object[] params) throws Throwable {
                    try {
                        TTransport tTransport = connectionManager.getConnection();
                        TFramedTransport tFramedTransport = new TFramedTransport(tTransport);
                        TProtocol protocol = new TCompactProtocol(tFramedTransport);

                        TMultiplexedProtocol map = new TMultiplexedProtocol(protocol, serviceName);

                        //创建client实例
                        Object target = constructor.newInstance(map);

                        return method.invoke(target, params);
                    } finally {
                        connectionManager.returnConnection();
                    }
                }
            });
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Class<?> getObjectType() {
        return interfaces;
    }

    @Override
    public boolean isSingleton() {
        return true;
    }

    public static void main(String[] args) throws TException {
        CommonClientDemo<HelloWorldService.Iface> commonClientDemo = new CommonClientDemo<>("localhost", 8089, 1000, HelloWorldService.class);
        HelloWorldService.Iface client = commonClientDemo.getObject();
        System.out.println(commonClientDemo.getObjectType());
        ExecutorService executorService = Executors.newCachedThreadPool();
        for (int i = 0; i < 10000; i++) {
            final int index = i;
            executorService.execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        String zhp = client.sayHello("zhp" + index);
                        System.out.println(zhp);
                    } catch (TException e) {
                        e.printStackTrace();
                    }

                }
            });

        }
        executorService.shutdown();
    }

}
