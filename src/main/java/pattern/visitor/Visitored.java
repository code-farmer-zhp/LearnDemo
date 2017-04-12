package pattern.visitor;


public class Visitored implements Visitor {
    @Override
    public void visit(Aluminum a) {
        System.out.println("AluminumVisitor visit(Aluminum)");
    }

    @Override
    public void visit(Cat cat) {
        System.out.println("visit cat");
    }
}
