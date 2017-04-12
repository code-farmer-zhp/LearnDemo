package pattern.single;


public final class MyObject3 {

    private MyObject3() {

    }

    private static class MyObjectHandler {
        private static MyObject3 myObject3 = new MyObject3();
    }

    public static MyObject3 getInstance() {
        return MyObjectHandler.myObject3;
    }
}
