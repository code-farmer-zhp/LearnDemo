package pattern.bridge;


public class Abstraction {
    private Implementation implementation;

    public Abstraction(Implementation implementation) {
        this.implementation = implementation;
    }

    public void service1() {
        implementation.facility1();
        implementation.facility2();
    }

    public void service2() {
        implementation.facility2();
        implementation.facility3();
    }


    public void service3() {
        implementation.facility1();
        implementation.facility2();
        implementation.facility4();
    }

    protected Implementation getImplementation() {
        return implementation;
    }

    public static void main(String[] args) throws InterruptedException {

        String url = "http://img06.fn-mart.com//pic/4593133c905324df942b/knq2nn52FTLMhMul32/59eaeajGlGnaKx/CsmRslh4fWmAAL7tAAOlfkA46FY959.jpg";
        String s = url.replaceAll("http://img[0-9]*.fn-mart.com", "//img10.fn-mart.com");
        System.out.println(s);

    }
}
