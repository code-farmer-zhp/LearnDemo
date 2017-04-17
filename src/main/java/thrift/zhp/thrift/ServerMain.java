package thrift.zhp.thrift;

import org.apache.thrift.protocol.TCompactProtocol;
import org.apache.thrift.server.TNonblockingServer;
import org.apache.thrift.transport.TFramedTransport;
import org.apache.thrift.transport.TNonblockingServerSocket;
import org.apache.thrift.transport.TNonblockingServerTransport;
import org.apache.thrift.transport.TTransportException;


public class ServerMain {
    public static void main(String[] args) throws TTransportException {
        TNonblockingServerTransport socket = new TNonblockingServerSocket(8089, 1000);

        TNonblockingServer.Args targs = new TNonblockingServer.Args(socket);
        targs.processor(new MySecondService.Processor<>(new MySecondServiceHandler()));
        targs.transportFactory(new TFramedTransport.Factory());
        targs.protocolFactory(new TCompactProtocol.Factory());

        TNonblockingServer server = new TNonblockingServer(targs);
        server.serve();
    }
}
