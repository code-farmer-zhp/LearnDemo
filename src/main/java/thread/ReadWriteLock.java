package thread;

import java.util.concurrent.locks.ReentrantReadWriteLock;

public class ReadWriteLock {
    private ReentrantReadWriteLock lock = new ReentrantReadWriteLock();

    public void read() {
        ReentrantReadWriteLock.ReadLock readLock = lock.readLock();
        readLock.lock();
        try {
            System.out.println("读锁" + System.nanoTime());
            Thread.sleep(10000);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            readLock.unlock();
        }
    }

    public void write() {
        ReentrantReadWriteLock.WriteLock writeLock = lock.writeLock();
        writeLock.lock();
        try {
            System.out.println("写锁" + System.currentTimeMillis());
            Thread.sleep(1000);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            writeLock.unlock();
        }
    }

    public static void main(String[] args) {
        ReadWriteLock readWriteLock = new ReadWriteLock();
        for (int i = 0; i < 10; i++) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    readWriteLock.write();
                }
            }).start();
        }
    }
}
