package pattern.visitor;

/**
 * 规范的visitor模式
 */
public class VAluminum extends Aluminum implements Visitable {
    @Override
    public void accept(Visitor v) {
        v.visit(this);
    }
}
