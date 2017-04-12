package concurrent;


import java.util.concurrent.Exchanger;

public class ExchangeTest {
    static class Mythread extends Thread {
        private Exchanger<String> exchanger;

        public Mythread(Exchanger<String> exchanger) {
            this.exchanger = exchanger;
        }

        @Override
        public void run() {
            try {
                System.out.println("A" + exchanger.exchange("交换A"));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    static class Mythread2 extends Thread {
        private Exchanger<String> exchanger;

        public Mythread2(Exchanger<String> exchanger) {
            this.exchanger = exchanger;
        }

        @Override
        public void run() {
            try {
                System.out.println("B" + exchanger.exchange("交换B"));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        Exchanger<String> exchanger = new Exchanger<>();
        new Mythread(exchanger).start();
        new Mythread2(exchanger).start();
    }
}
