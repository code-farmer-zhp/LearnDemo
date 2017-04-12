package pattern.mediator;


public class HouseOwner extends Persion {


    public HouseOwner(String name, Mediator mediator) {
        super(name, mediator);
    }

    @Override
    public void done(String msg) {
        getMediator().chuzufang(msg, this);
    }
}
