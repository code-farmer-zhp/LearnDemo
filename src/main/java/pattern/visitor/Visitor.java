package pattern.visitor;

/**
 * 可以访问各种对象 这些对象不一定要有关系
 */
public interface Visitor {

    void visit(Aluminum a);

    void visit(Cat cat);

}
