package concurrent;


import java.util.LinkedList;
import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class ListPool {
    private int poolMaxSize = 3;
    private int semaphorePermits = 5;
    private LinkedList<String> list = new LinkedList<>();

    private Semaphore semaphore = new Semaphore(semaphorePermits);
    private ReentrantLock lock = new ReentrantLock();
    private Condition condition = lock.newCondition();

    public ListPool() {
        for (int i = 0; i < poolMaxSize; i++) {
            list.add("zhp" + i);
        }
    }

    public String get() {
        String result = null;
        try {
            semaphore.acquire();
            lock.lock();
            try {
                while (list.size() == 0) {
                    condition.await();
                }
                result = list.removeFirst();
            } finally {
                lock.unlock();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public void put(String value) {
        lock.lock();
        try {
            list.addLast(value);
            condition.signalAll();
        } finally {
            lock.unlock();
        }
        semaphore.release();

    }

    public static void main(String[] args) {
        ListPool listPool = new ListPool();
        new Thread(() -> {
            for (int i = 0; i < 2000; i++) {
                String s = listPool.get();
                System.out.println("get:" + s);
                listPool.put(s);
            }
        }).start();
    }
}
