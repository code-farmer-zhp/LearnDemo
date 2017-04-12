package pattern.mediator;


public abstract class Persion {
    private String name;
    private Mediator mediator;

    public Persion(String name, Mediator mediator) {
        this.name = name;
        this.mediator = mediator;
    }

    public abstract void done(String msg);

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Mediator getMediator() {
        return mediator;
    }

    public void setMediator(Mediator mediator) {
        this.mediator = mediator;
    }
}
