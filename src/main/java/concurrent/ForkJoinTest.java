package concurrent;

import java.util.concurrent.*;

class MyRecursiveAction extends RecursiveAction {

    private int start;
    private int end;

    public MyRecursiveAction(int start, int end) {
        this.start = start;
        this.end = end;
    }

    @Override
    protected void compute() {
        System.out.println(Thread.currentThread().getName());
        if (end - start > 2) {
            int mid = (start + end) / 2;
            MyRecursiveAction left = new MyRecursiveAction(start, mid);
            MyRecursiveAction right = new MyRecursiveAction(mid + 1, end);
            invokeAll(left, right);

        } else {
            System.out.println(start + ":" + end);
        }

    }
}

class MyRecursiveTask extends RecursiveTask<Integer> {
    private int start;
    private int end;

    public MyRecursiveTask(int start, int end) {
        this.start = start;
        this.end = end;
    }

    @Override
    protected Integer compute() {
        if (end - start > 2) {
            int mid = (start + end) / 2;
            MyRecursiveTask left = new MyRecursiveTask(start, mid);
            MyRecursiveTask right = new MyRecursiveTask(mid + 1, end);
            invokeAll(left, right);
            return left.join() + right.join();
        } else {
            return start + end;
        }
    }
}

public class ForkJoinTest {

    public static void main(String[] args) {
        ForkJoinPool forkJoinPool = new ForkJoinPool();
        //forkJoinPool.submit(new MyRecursiveAction(1, 20));
        //Thread.sleep(5000);
        ForkJoinTask<Integer> submit = forkJoinPool.submit(new MyRecursiveTask(1,20));
        System.out.println(submit.join());

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("end");

    }

}
