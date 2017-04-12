package concurrent;

import java.util.concurrent.*;

/**
 * Created by peng.zhou1 on 2017/3/1.
 */
public class ThreadPoolExecutors {
    public static void main(String[] args) throws Exception {
        ExecutorService executorService = Executors.newCachedThreadPool();

        //executorService.isShutdown();
        //executorService.isTerminated();


        CompletionService<String> service = new ExecutorCompletionService<>(executorService);

        service.submit(() -> "someThing");

        service.take().get();
    }
}
