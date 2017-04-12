package pattern.chainofresponsibility;

public class Handler1 implements Handler {

    @Override
    public void handler(Request request, HandlerChain chain) {
        System.out.println("Handler1 start");
        chain.handler(request);
        System.out.println("Handler1 end");
    }
}
