package thrift.micmiu.thrift.demo;

import org.apache.thrift.protocol.TCompactProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.transport.TFramedTransport;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TTransport;

public class HelloClientDemo {
    public static void main(String[] args) throws Exception {
       /* //传输层 tcp
        TTransport tTransport = new TSocket("localhost", 8089, 2000);
        //协议层
        TProtocol protocol = new TBinaryProtocol(tTransport);

        HelloWorldService.Client client = new HelloWorldService.Client(protocol);

        tTransport.open();

        String zhp = client.sayHello("zhp");
        System.out.println(zhp);*/

        //使用非阻塞式IO，服务端和客户端需要指定 TFramedTransport 数据传输的方式。
         TTransport tTransport = new TSocket("localhost", 8089, 2000);
        TFramedTransport tFramedTransport = new TFramedTransport(tTransport);

        TProtocol protocol = new TCompactProtocol(tFramedTransport);

        HelloWorldService.Client client = new HelloWorldService.Client(protocol);
        tFramedTransport.open();

        String zhp = client.sayHello("zhp");
        System.out.println(zhp);


        //半同步半异步的服务模型
       /* TTransport tTransport = new TSocket("localhost", 8089, 2000);
        TFramedTransport tFramedTransport = new TFramedTransport(tTransport);

        TProtocol protocol = new TBinaryProtocol(tFramedTransport);
        HelloWorldService.Client client = new HelloWorldService.Client(protocol);
        tFramedTransport.open();

        String zhp = client.sayHello("zhp");
        System.out.println(zhp);*/

       /* TAsyncClientManager clientManager = new TAsyncClientManager();

        TNonblockingSocket socket = new TNonblockingSocket("localhost", 8089, 200);


        TProtocolFactory tProtocolFactory = new TCompactProtocol.Factory();

        HelloWorldService.AsyncClient client = new HelloWorldService.AsyncClient(tProtocolFactory, clientManager, socket);
        client.sayHello("zhp", new AsyncMethodCallback<String>() {
            @Override
            public void onComplete(String s) {
                System.out.println(s);
            }

            @Override
            public void onError(Exception e) {
                e.printStackTrace();
            }
        });
        Thread.sleep(Integer.MAX_VALUE);*/


    }
}
