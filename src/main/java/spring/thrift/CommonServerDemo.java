package spring.thrift;


import thrift.micmiu.thrift.demo.HelloWorldImpl;
import thrift.micmiu.thrift.demo.HelloWorldService;
import org.apache.thrift.TMultiplexedProcessor;
import org.apache.thrift.TProcessor;
import org.apache.thrift.protocol.TCompactProtocol;
import org.apache.thrift.server.TServer;
import org.apache.thrift.server.TThreadedSelectorServer;
import org.apache.thrift.transport.TFramedTransport;
import org.apache.thrift.transport.TNonblockingServerSocket;
import org.apache.thrift.transport.TNonblockingServerTransport;

import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.Map;

/**
 * Thrift通用服务启动类 可以和spring集成
 */
public class CommonServerDemo {

    private int port;

    private Map<String, Object> exposedServerMap;

    public CommonServerDemo(int port, Map<String, Object> exposedServerMap) {
        this.port = port;
        this.exposedServerMap = exposedServerMap;
    }

    public void start() {
        try {
            TNonblockingServerTransport socket = new TNonblockingServerSocket(port);

            TMultiplexedProcessor mp = new TMultiplexedProcessor();

            //同时暴露多个服务
            for (Map.Entry<String, Object> entry : exposedServerMap.entrySet()) {
                String serviceInterface = entry.getKey();
                Class<?> processorClass = Class.forName(serviceInterface + "$Processor");
                Class<?> ifaceClass = Class.forName(serviceInterface + "$Iface");
                Constructor<?> constructor = processorClass.getConstructor(ifaceClass);
                TProcessor processor = (TProcessor) constructor.newInstance(entry.getValue());

                mp.registerProcessor(serviceInterface, processor);
            }

            TThreadedSelectorServer.Args args = new TThreadedSelectorServer.Args(socket);
            args.processor(mp);
            args.transportFactory(new TFramedTransport.Factory());
            args.protocolFactory(new TCompactProtocol.Factory());

            TServer server = new TThreadedSelectorServer(args);
            server.serve();

            //TODO 注册服务
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        Map<String, Object> exposedServerMap = new HashMap<>();
        exposedServerMap.put(HelloWorldService.class.getName(), new HelloWorldImpl());
        new CommonServerDemo(8089, exposedServerMap).start();
    }
}
