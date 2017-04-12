package thread;

class A extends InheritableThreadLocal<Long> {
    @Override
    protected Long initialValue() {
        return 0L;
    }

    @Override
    protected Long childValue(Long parentValue) {
        System.out.println("change");
        return 1 + parentValue;
    }
}

public class ThreadLocalTest {
    public static A a = new A();

    public static void main(String[] args) {
        try {
            for (int i = 0; i < 10; i++) {
                System.out.println("main:" + a.get());
                Thread.sleep(100);
            }
            Thread.sleep(5000);

            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        for (int i = 0; i < 10; i++) {
                            System.out.println(Thread.currentThread().getName() + ":" + a.get());
                            Thread.sleep(100);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            });
            thread.start();
            a.set(100L);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
