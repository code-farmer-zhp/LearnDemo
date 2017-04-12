package thread;

/**
 * Created by peng.zhou1 on 2017/2/24.
 */
public class ExceptionTests {
    static class ExceptionGroup extends ThreadGroup {

        public ExceptionGroup(String name) {
            super(name);
        }

        @Override
        public void uncaughtException(Thread t, Throwable e) {
            //super.uncaughtException(t, e);
            System.out.println("线程组捕获异常");
        }
    }

    static class ExceptionStaticHandler implements Thread.UncaughtExceptionHandler {

        @Override
        public void uncaughtException(Thread t, Throwable e) {
            System.out.println("Thread 类捕获异常");
        }
    }

    static class ExceptionHandler implements Thread.UncaughtExceptionHandler {
        @Override
        public void uncaughtException(Thread t, Throwable e) {
            System.out.println("thread 对象捕获异常");
        }
    }

    public static void main(String[] args) {
        //ExceptionGroup group = new ExceptionGroup("group");

        Thread thread = new Thread( new Runnable() {
            @Override
            public void run() {
                throw new RuntimeException("error");
            }
        });

        //thread.setUncaughtExceptionHandler(new ExceptionHandler());
        Thread.setDefaultUncaughtExceptionHandler(new ExceptionStaticHandler());

        thread.start();
    }
}
