package pattern.bridge;

/**
 * Created by peng.zhou1 on 2017/2/10.
 */
public class Implementation1 implements Implementation {

    private Library1 delegate = new Library1();


    @Override
    public void facility1() {
        System.out.println("Implementation1.facility1");
        delegate.method1();
    }

    @Override
    public void facility2() {
        System.out.println("Implementation1.facility2");
        delegate.method2();
    }

    @Override
    public void facility3() {
        System.out.println("Implementation1.facility3");
        delegate.method2();
        delegate.method1();

    }

    @Override
    public void facility4() {
        System.out.println("Implementation1.facility4");
        delegate.method1();
    }
}
