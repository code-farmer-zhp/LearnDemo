package pattern.bridge;

/**
 * Created by peng.zhou1 on 2017/2/10.
 */
public class ClientService1 extends Abstraction {

    public ClientService1(Implementation implementation) {
        super(implementation);
    }

    public void serviceA() {
        service1();
        service2();
    }

    public void serviceB() {
        service3();
    }
}
