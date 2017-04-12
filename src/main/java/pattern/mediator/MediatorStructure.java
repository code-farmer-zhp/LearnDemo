package pattern.mediator;

public class MediatorStructure implements Mediator {
    private Persion houseOwner;
    private Persion tenant;

    public Persion getHouseOwner() {
        return houseOwner;
    }

    public void setHouseOwner(Persion houseOwner) {
        this.houseOwner = houseOwner;
    }

    public Persion getTenant() {
        return tenant;
    }

    public void setTenant(Persion tenant) {
        this.tenant = tenant;
    }

    @Override
    public void chuzufang(String msg, Persion persion) {
        String send = persion.getName() + "说：" + msg;
        //通知找房的人
        System.out.println(tenant.getName() + "知道了：" + send);

    }

    @Override
    public void zhaofang(String msg, Persion persion) {
        String send = persion.getName() + "说：" + msg;
        //通知出租的人
        System.out.println(houseOwner.getName() + "知道了：" + send);
    }
}
