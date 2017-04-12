package pattern.visitor;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * 结合装饰器模式访问
 */
public class VisitableDecorator extends Trash implements Visitable {

    private Trash delegate;

    private Method dispatch;

    public VisitableDecorator(Trash trash) {
        this.delegate = trash;
        try {
            dispatch = Visitor.class.getMethod("visit", trash.getClass());
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void accept(Visitor v) {
        try {
            //v.visit(trash)
            dispatch.invoke(v, delegate);
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        new VisitableDecorator(new Aluminum()).accept(new Visitored());
    }
}
