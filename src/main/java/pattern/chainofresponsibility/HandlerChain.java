package pattern.chainofresponsibility;


import java.util.ArrayList;
import java.util.List;

public class HandlerChain{

    private List<Handler> handlers;

    private int index = 0;

    public HandlerChain(List<Handler> handlers) {
        this.handlers = handlers;
    }

    public void handler(Request request) {
        if (index < handlers.size()) {
            next().handler(request, this);
        }else {
            System.out.println("done");
        }
    }

    private Handler next() {
        return handlers.get(index++);
    }

    public static void main(String[] args) {
        List<Handler> handlers = new ArrayList<>();
        Handler1 handler1 = new Handler1();
        Handler2 handler2 = new Handler2();
        handlers.add(handler1);
        handlers.add(handler2);
        new HandlerChain(handlers).handler(new Request());
    }
}
