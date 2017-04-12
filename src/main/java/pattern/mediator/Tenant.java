package pattern.mediator;


public class Tenant extends Persion {

    public Tenant(String name, Mediator mediator) {
        super(name, mediator);
    }

    @Override
    public void done(String msg) {
        getMediator().zhaofang(msg, this);
    }
}
