package thread;

public class DBThread {
    private volatile boolean pushA = false;

    public synchronized void saveA() {
        try {
            while (pushA) {
                wait();
            }
            System.out.println("备份A");
            notifyAll();
            pushA = true;
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public synchronized void saveB() {
        try {
            while (!pushA) {
                wait();
            }
            System.out.println("备份B");
            notifyAll();
            pushA = false;
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        final DBThread dbThread = new DBThread();
        for (int i = 0; i < 20; i++) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    dbThread.saveA();
                }
            }).start();

            new Thread(new Runnable() {
                @Override
                public void run() {
                    dbThread.saveB();
                }
            }).start();
        }
    }
}
