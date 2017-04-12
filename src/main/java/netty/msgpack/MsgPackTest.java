package netty.msgpack;

import org.msgpack.MessagePack;
import org.msgpack.template.Templates;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class MsgPackTest {
    public static void main(String[] args) throws IOException {
        List<String> src = new ArrayList<>();
        src.add("a");
        src.add("b");
        src.add("c");
        MessagePack messagePack = new MessagePack();
        byte[] write = messagePack.write(src);

        System.out.println(Arrays.toString(write));
        List<String> read = messagePack.read(write, Templates.tList(Templates.TString));
        System.out.println(read);

    }
}
