package spring.thrift;


import org.apache.thrift.transport.TTransport;

public class ConnectionManager {

    private static final ThreadLocal<TTransport> transportThreadLocal = new ThreadLocal<>();

    private ConnectionProvider connectionProvider;

    public TTransport getConnection() {
        TTransport connection = connectionProvider.getConnection();
        transportThreadLocal.set(connection);
        return connection;
    }

    public void returnConnection() {
        try {
            TTransport tTransport = transportThreadLocal.get();
            connectionProvider.returnConnection(tTransport);
        }finally {
            transportThreadLocal.remove();
        }
    }
    public void setConnectionProvider(ConnectionProvider connectionProvider) {
        this.connectionProvider = connectionProvider;
    }
}
