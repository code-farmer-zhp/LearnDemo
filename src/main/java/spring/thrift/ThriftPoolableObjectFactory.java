package spring.thrift;

import org.apache.commons.pool.PoolableObjectFactory;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TTransport;

/**
 * 连接池
 */
public class ThriftPoolableObjectFactory implements PoolableObjectFactory<TTransport> {

    private String ip;
    private int port;
    private int readTimeOut;

    public ThriftPoolableObjectFactory(String ip, int port, int readTimeOut) {
        this.ip = ip;
        this.port = port;
        this.readTimeOut = readTimeOut;
    }

    @Override
    public TTransport makeObject() throws Exception {
        TTransport tTransport = new TSocket(ip, port, readTimeOut);
        tTransport.open();
        return tTransport;
    }

    @Override
    public void destroyObject(TTransport tTransport) throws Exception {
        if (tTransport.isOpen()) {
            tTransport.close();
        }

    }

    @Override
    public boolean validateObject(TTransport tTransport) {
        try {
            if (tTransport.isOpen()) {
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return false;
    }

    /**
     * 激活对象
     */
    @Override
    public void activateObject(TTransport tTransport) throws Exception {

    }

    /**
     * 钝化对象
     */
    @Override
    public void passivateObject(TTransport tTransport) throws Exception {

    }
}
