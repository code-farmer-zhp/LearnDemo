package pattern.state;

/**
 * Created by peng.zhou1 on 2017/1/20.
 */
public class Creature {

    private interface State {
        String response();
    }

    private class Frog implements State {
        public String response() {
            return "Ribbet!";
        }
    }

    private class Prince implements State {
        public String response() {
            return "Darling!";
        }
    }

    //初始状态
    private State state = new Frog();

    public void greet() {
        System.out.println(state.response());
    }

    //状态改变了
    public void kiss() {
        state = new Prince();
    }

    public static void main(String[] args) {
        Creature creature = new Creature();
        creature.greet();

        creature.kiss();

        creature.greet();
    }
}
