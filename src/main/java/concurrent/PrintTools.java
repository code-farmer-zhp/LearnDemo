package concurrent;

import java.util.concurrent.Phaser;

/**
 * Created by peng.zhou1 on 2017/2/28.
 */
public class PrintTools {
    public static Phaser phaser;

    public static void methodA() {
        System.out.println(Thread.currentThread().getName() + " A1 begin=" + System.currentTimeMillis());

        phaser.arriveAndAwaitAdvance();

        System.out.println(Thread.currentThread().getName() + " A1 end=" + System.currentTimeMillis());


        System.out.println(Thread.currentThread().getName() + " A2 begin=" + System.currentTimeMillis());

        phaser.arriveAndAwaitAdvance();

        System.out.println(Thread.currentThread().getName() + " A2 end=" + System.currentTimeMillis());
    }

    public static void methodB() throws InterruptedException {
        System.out.println(Thread.currentThread().getName() + " A1 begin=" + System.currentTimeMillis());
        Thread.sleep(5000);
        System.out.println("A:" + phaser.getRegisteredParties());
        //phaser.arriveAndDeregister();
        phaser.arriveAndAwaitAdvance();
        System.out.println("B:" + phaser.getRegisteredParties() + ":" + phaser.getPhase());
        System.out.println(phaser.isTerminated());

        System.out.println(Thread.currentThread().getName() + " A1 end=" + System.currentTimeMillis());

    }

    static class ThreadA extends Thread {

        @Override
        public void run() {
            PrintTools.methodA();
        }
    }

    static class ThreadB extends Thread {

        @Override
        public void run() {
            PrintTools.methodA();
        }
    }

    static class ThreadC extends Thread {
        @Override
        public void run() {
            try {
                PrintTools.methodB();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        Phaser phaser = new Phaser(3) {
            @Override
            protected boolean onAdvance(int phase, int registeredParties) {
                System.out.println("通过屏障");
                //return true;//破坏屏障
                return false;
            }
        };
        System.out.println(phaser.getRegisteredParties());
        //phaser.register();//每次增加一个
        phaser.bulkRegister(10);//批量增加
        System.out.println(phaser.getRegisteredParties());
        phaser.register();
        System.out.println(phaser.getRegisteredParties());

        //phaser.awaitAdvanceInterruptibly(5)

       /* PrintTools.phaser = phaser;

        ThreadA threadA = new ThreadA();
        threadA.setName("A");
        threadA.start();

        ThreadB threadB = new ThreadB();
        threadB.setName("B");
        threadB.start();


        ThreadC threadC = new ThreadC();
        threadC.setName("C");
        threadC.start();
*/
    }
}
