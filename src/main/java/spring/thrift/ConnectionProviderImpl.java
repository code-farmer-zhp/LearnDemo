package spring.thrift;

import org.apache.commons.pool.impl.GenericObjectPool;
import org.apache.thrift.transport.TTransport;


public class ConnectionProviderImpl implements ConnectionProvider {

    private String ip;

    private int port;

    private int readTimeout;

    private int maxActive = GenericObjectPool.DEFAULT_MAX_ACTIVE;

    private int maxIdel = GenericObjectPool.DEFAULT_MAX_IDLE;

    private int minIdel = GenericObjectPool.DEFAULT_MIN_IDLE;

    private long maxWait = GenericObjectPool.DEFAULT_MAX_WAIT;

    /**
     * 取对象时 是否测试可用
     */
    private boolean testOnBorrow = GenericObjectPool.DEFAULT_TEST_ON_BORROW;

    /**
     * 还对象时 是否测试可用
     */
    private boolean testOnReturn = GenericObjectPool.DEFAULT_TEST_ON_RETURN;

    /**
     * 空闲的时候 是否测试可用
     */
    private boolean testWhileIdel = GenericObjectPool.DEFAULT_TEST_WHILE_IDLE;

    private GenericObjectPool<TTransport> objectPool;

    public void init() {
        ThriftPoolableObjectFactory factory = new ThriftPoolableObjectFactory(ip, port, readTimeout);
        objectPool = new GenericObjectPool<>(factory);

        objectPool.setMaxActive(maxActive);
        objectPool.setMaxIdle(maxIdel);
        objectPool.setMinIdle(minIdel);
        objectPool.setMaxWait(maxWait);
        objectPool.setTestOnBorrow(testOnBorrow);
        objectPool.setTestOnReturn(testOnReturn);
        objectPool.setTestWhileIdle(testWhileIdel);
        //链接耗尽时应该怎么做
        objectPool.setWhenExhaustedAction(GenericObjectPool.DEFAULT_WHEN_EXHAUSTED_ACTION);
    }

    public void destory() {
        try {
            objectPool.close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public TTransport getConnection() {
        try {
            return objectPool.borrowObject();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void returnConnection(TTransport tSocket) {
        try {
            objectPool.returnObject(tSocket);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public int getReadTimeout() {
        return readTimeout;
    }

    public void setReadTimeout(int readTimeout) {
        this.readTimeout = readTimeout;
    }

    public int getMaxActive() {
        return maxActive;
    }

    public void setMaxActive(int maxActive) {
        this.maxActive = maxActive;
    }

    public int getMaxIdel() {
        return maxIdel;
    }

    public void setMaxIdel(int maxIdel) {
        this.maxIdel = maxIdel;
    }

    public int getMinIdel() {
        return minIdel;
    }

    public void setMinIdel(int minIdel) {
        this.minIdel = minIdel;
    }

    public long getMaxWait() {
        return maxWait;
    }

    public void setMaxWait(long maxWait) {
        this.maxWait = maxWait;
    }

    public boolean isTestOnBorrow() {
        return testOnBorrow;
    }

    public void setTestOnBorrow(boolean testOnBorrow) {
        this.testOnBorrow = testOnBorrow;
    }

    public boolean isTestOnReturn() {
        return testOnReturn;
    }

    public void setTestOnReturn(boolean testOnReturn) {
        this.testOnReturn = testOnReturn;
    }

    public boolean isTestWhileIdel() {
        return testWhileIdel;
    }

    public void setTestWhileIdel(boolean testWhileIdel) {
        this.testWhileIdel = testWhileIdel;
    }
}
