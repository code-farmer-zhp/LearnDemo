package pattern.chainofresponsibility;

/**
 * Created by peng.zhou1 on 2017/2/20.
 */
public class Handler2 implements Handler {
    @Override
    public void handler(Request request, HandlerChain chain) {
        System.out.println("Handler2 start");
        chain.handler(request);
        System.out.println("Handler2 end");
    }
}
