package spring.thrift;


import org.apache.thrift.transport.TTransport;

public interface ConnectionProvider {

    TTransport getConnection();

    void returnConnection(TTransport tSocket);

}
