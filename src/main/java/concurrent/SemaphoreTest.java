package concurrent;

import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 信号量
 */

class Service {
    private Semaphore semaphore = new Semaphore(3);
    private ReentrantLock lock = new ReentrantLock();

    public void say() {
        try {
            semaphore.acquire();
            try {
                System.out.println(Thread.currentThread().getName() + "准备");

                lock.lock();
                try {
                    System.out.println("doSomething");
                    Thread.sleep(1000);
                } finally {
                    lock.unlock();
                }
            } finally {
                semaphore.release();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {

    }
}

public class SemaphoreTest {
    public static void main(String[] args) throws InterruptedException {
        Semaphore semaphore = new Semaphore(1);

        semaphore.acquire();//获取许可   可被中断
        //semaphore.acquireUninterruptibly();//不可中断
        semaphore.release();//释放许可 调用一次增加一个许可
        semaphore.availablePermits();//获取许可量
        semaphore.drainPermits();//耗尽许可量并返回之前许可量

        semaphore.getQueueLength();//
        semaphore.hasQueuedThreads();//
    }
}
