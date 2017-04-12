package pattern.bridge;

/**
 * Created by peng.zhou1 on 2017/2/10.
 */
public class Implementation2 implements Implementation {

    private Library2 delegate = new Library2();

    @Override
    public void facility1() {
        System.out.println("Implementation2.facility1");
        delegate.operation1();
    }

    @Override
    public void facility2() {
        System.out.println("Implementation2.facility2");
        delegate.operation2();
    }

    @Override
    public void facility3() {
        System.out.println("Implementation2.facility3");
        delegate.operation3();
    }

    @Override
    public void facility4() {
        System.out.println("Implementation2.facility4");
        delegate.operation1();
    }
}
