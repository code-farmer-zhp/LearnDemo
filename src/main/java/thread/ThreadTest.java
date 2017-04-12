package thread;

class SetGet {
    private String value;

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}

class GetThread extends Thread {
    private SetGet lock;

    public GetThread(SetGet lock) {
        this.lock = lock;
    }

    @Override
    public void run() {
        synchronized (lock) {
            for (; ; ) {
                while (lock.getValue() == null) {
                    try {
                        lock.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                System.out.println("get:" + lock.getValue());
                lock.setValue(null);
                lock.notifyAll();
            }
        }
    }
}

class SetThread extends Thread {
    private SetGet lock;

    public SetThread(SetGet lock) {
        this.lock = lock;
    }

    @Override
    public void run() {
        synchronized (lock) {
            for (; ; ) {
                while (lock.getValue() != null) {
                    try {
                        lock.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                String value = System.nanoTime() + "";
                lock.setValue(value);
                lock.notifyAll();
                System.out.println("set:" + value);
            }
        }
    }
}

public class ThreadTest {

    public static void main(String[] args) {
        SetGet setGet = new SetGet();
        GetThread getThread = new GetThread(setGet);
        getThread.start();
        GetThread getThread2 = new GetThread(setGet);
        getThread2.start();


        SetThread setThread = new SetThread(setGet);
        setThread.start();
        SetThread setThread2 = new SetThread(setGet);
        setThread2.start();

        Thread[] threads = new Thread[Thread.currentThread().getThreadGroup().activeCount()];
        Thread.currentThread().getThreadGroup().enumerate(threads);
        for (Thread thread : threads) {
            System.out.println(thread.getName() + ":" + thread.getState());
        }
    }
}
