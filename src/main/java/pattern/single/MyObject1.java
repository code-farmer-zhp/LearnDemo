package pattern.single;


public final class MyObject1 {
    private static MyObject1 myObject1 = new MyObject1();

    private MyObject1() {

    }

    public MyObject1 getInstance() {
        return myObject1;
    }
}
