package pattern.memento;


public class GameRole {
    private int vit;
    private int atk;

    public void init() {
        vit = 100;
        atk = 100;
    }

    public void show() {
        System.out.println("体力:" + vit);
        System.out.println("攻击力:" + atk);
    }

    public void fightBoss() {
        this.vit = 0;
        this.atk = 0;
    }

    public RoleStateMemento save() {
        System.out.println("保存信息");
        return new RoleStateMemento(vit, atk);
    }

    public void recove(RoleStateMemento roleStateMemento) {
        System.out.println("恢复信息");
        this.vit = roleStateMemento.getVit();
        this.atk = roleStateMemento.getAtk();
    }
}
