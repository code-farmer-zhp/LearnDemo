package zhp.rpc;


import java.util.Random;

public class HelloImpl implements Hello {
    public String say(String name) {
        MutilObj mutilObj = new MutilObj();
        mutilObj.setName(name);
        mutilObj.setAge(new Random().nextInt(100));
        return mutilObj.toString();
    }
}
