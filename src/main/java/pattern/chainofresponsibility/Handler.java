package pattern.chainofresponsibility;


public interface Handler {

    void handler(Request request, HandlerChain chain);
}
