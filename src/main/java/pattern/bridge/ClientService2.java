package pattern.bridge;


public class ClientService2 extends Abstraction {

    public ClientService2(Implementation implementation) {
        super(implementation);
    }

    public void serviceC() {
        service2();
        service3();
    }

    public void serviceD() {
        service1();
        service3();
    }

    public void serviceE() {
        getImplementation().facility3();
    }
}
