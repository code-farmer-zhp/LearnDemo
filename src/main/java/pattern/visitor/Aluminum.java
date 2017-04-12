package pattern.visitor;


public class Aluminum extends Trash  implements Visitable{

    public static void main(String[] args) {
        Aluminum aluminum = new Aluminum();
        Cat cat = new Cat();

        Visitored visitored = new Visitored();

        aluminum.accept(visitored);
        cat.accept(visitored);

    }

    @Override
    public void accept(Visitor v) {
        v.visit(this);
    }
}
