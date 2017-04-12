package pattern.memento;


public class Client {
    public static void main(String[] args) {
        GameRole gameRole = new GameRole();
        gameRole.init();
        gameRole.show();

        RoleStateMange roleStateMange = new RoleStateMange();
        roleStateMange.setMemento(gameRole.save());

        gameRole.fightBoss();
        gameRole.show();

        gameRole.recove(roleStateMange.getMemento());
        gameRole.show();

    }
}
