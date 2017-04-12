package zhp.rpc.consumer;


import java.util.concurrent.Semaphore;

public class InvokeFuture {
    private Semaphore semaphore = new Semaphore(0);
    private Throwable cause;
    private Object result;
    private volatile boolean haveResult = false;

    public Throwable getCause() {
        return cause;
    }

    public void setCause(Throwable cause) {
        this.cause = cause;
        if (!haveResult) {
            semaphore.release();
        }
        haveResult = true;
    }

    public Object getResult() {
        if (haveResult) {
            if (cause != null) {
                throw new RuntimeException(cause);
            }
            return result;
        }
        try {
            semaphore.acquire();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public void setResult(Object result) {
        if (!haveResult) {
            this.result = result;
            haveResult = true;
            semaphore.release();
        }
    }


}
