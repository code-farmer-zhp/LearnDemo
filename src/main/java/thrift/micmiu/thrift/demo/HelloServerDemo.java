package thrift.micmiu.thrift.demo;

import org.apache.thrift.TProcessor;
import org.apache.thrift.protocol.TCompactProtocol;
import org.apache.thrift.server.TNonblockingServer;
import org.apache.thrift.server.TServer;
import org.apache.thrift.transport.TFramedTransport;
import org.apache.thrift.transport.TNonblockingServerSocket;

public class HelloServerDemo {

    public static void main(String[] args) throws Exception {

       /* //简单测试模型
        TProcessor processor = new HelloWorldService.Processor<>(new HelloWorldImpl());

        TServerSocket serverSocket = new TServerSocket(8089);
        TServer.Args targs = new TServer.Args(serverSocket);

        targs.processor(processor);
        targs.protocolFactory(new TBinaryProtocol.Factory());

        TServer server = new TSimpleServer(targs);
        System.out.println("服务启动");
        server.serve();*/





        /*//线程池服务模型，使用标准的阻塞式IO，预先创建一组线程处理请求
        TProcessor processor = new HelloWorldService.Processor<>(new HelloWorldImpl());

        TServerSocket serverSocket = new TServerSocket(8089);
        TThreadPoolServer.Args targs = new TThreadPoolServer.Args(serverSocket);

        targs.processor(processor);
        targs.protocolFactory(new TBinaryProtocol.Factory());

        TServer server = new TThreadPoolServer(targs);
        System.out.println("服务启动");
        server.serve();*/


        //使用非阻塞式IO，服务端和客户端需要指定 TFramedTransport 数据传输的方式。
        //指定非阻塞服务socket
        //绑定地址和端口 创建ServerSocket对象
        TNonblockingServerSocket serverSocket = new TNonblockingServerSocket(8089);

        //构建非阻塞服务参数
        TNonblockingServer.Args targs = new TNonblockingServer.Args(serverSocket);
        //服务实现
        TProcessor processor = new HelloWorldService.Processor<>(new HelloWorldImpl());
        targs.processor(processor);
        //传输层 http socket 等等
        targs.transportFactory(new TFramedTransport.Factory());
        //协议层如二进制协议，json协议。
        targs.protocolFactory(new TCompactProtocol.Factory());

        //服务启动
        TServer server = new TNonblockingServer(targs);
        server.serve();

        //半同步半异步的服务模型
       /* TProcessor processor = new HelloWorldService.Processor<>(new HelloWorldImpl());
        TNonblockingServerSocket serverSocket = new TNonblockingServerSocket(8089);
        THsHaServer.Args targs = new THsHaServer.Args(serverSocket);
        targs.processor(processor);
        targs.transportFactory(new TFramedTransport.Factory());
        targs.protocolFactory(new TBinaryProtocol.Factory());

        TServer server = new THsHaServer(targs);
        server.serve();*/

    }

}
