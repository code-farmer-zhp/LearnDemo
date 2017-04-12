package pattern.visitor;


public interface Visitable {
    void accept(Visitor v);
}
