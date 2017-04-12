package pattern.mediator;


public class TestMain {
    public static void main(String[] args) {
        MediatorStructure mediator = new MediatorStructure();
        Tenant tenant = new Tenant("zhp", mediator);
        HouseOwner houseOwner = new HouseOwner("fangdong1", mediator);

        mediator.setTenant(tenant);
        mediator.setHouseOwner(houseOwner);

        tenant.done("我要看房子了");

        houseOwner.done("好啊。来看房子吧");

        HouseOwner houseOwner2 = new HouseOwner("fangdong2", mediator);
        mediator.setHouseOwner(houseOwner2);

        tenant.done("我要看房子了");

        houseOwner2.done("好啊。来看房子吧");
    }
}
