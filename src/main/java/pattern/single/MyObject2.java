package pattern.single;


public final class MyObject2 {

    private static volatile MyObject2 myObject2;

    private MyObject2() {

    }

    public MyObject2 getInstance() {
        if (myObject2 == null) {
            synchronized (MyObject2.class) {
                if (myObject2 == null) {
                    myObject2 = new MyObject2();
                }
            }
        }
        return myObject2;
    }
}
