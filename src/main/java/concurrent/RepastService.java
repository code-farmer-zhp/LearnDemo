package concurrent;

import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 多生产者 多消费者模式
 */
public class RepastService {

    private Semaphore setSemaphore = new Semaphore(5);//生产者
    private Semaphore getSetSemaphore = new Semaphore(10);//消费者
    private ReentrantLock lock = new ReentrantLock();
    private Condition setCondition = lock.newCondition();
    private Condition getCondition = lock.newCondition();

    private Object[] queue = new Object[4];

    private boolean isFull() {
        for (int i = 0; i < queue.length; i++) {
            if (queue[i] == null) {
                return false;
            }
        }
        return true;
    }

    private boolean isEmpty() {
        for (int i = 0; i < queue.length; i++) {
            if (queue[i] != null) {
                return false;
            }
        }
        return true;
    }

    public void set() {
        try {
            setSemaphore.acquire();
            try {
                lock.lock();
                try {
                    while (isFull()) {
                        setCondition.await();
                    }
                    for (int i = 0; i < queue.length; i++) {
                        if (queue[i] == null) {
                            queue[i] = "数据";
                            break;
                        }
                    }
                    getCondition.signal();
                } finally {
                    lock.unlock();
                }
            } finally {
                setSemaphore.release();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void get() {
        try {
            getSetSemaphore.acquire();
            try {
                lock.lock();
                try {
                    while (isEmpty()) {
                        getCondition.await();
                    }
                    for (int i = 0; i < queue.length; i++) {
                        if (queue[i] != null) {
                            System.out.println("获得数据" + queue[i]);
                            queue[i] = null;
                            break;
                        }
                    }
                    setCondition.signal();
                } finally {
                    lock.unlock();
                }

            } finally {
                getSetSemaphore.release();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
