package nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.util.Date;
import java.util.Iterator;
import java.util.Set;

public class NioService {

    private Selector selector;

    public void init() throws IOException {
        selector = Selector.open();
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        serverSocketChannel.configureBlocking(false);
        serverSocketChannel.bind(new InetSocketAddress(9099));
        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);

        while (true) {
            selector.select();
            Set<SelectionKey> selectionKeys = selector.selectedKeys();
            Iterator<SelectionKey> iterator = selectionKeys.iterator();
            while (iterator.hasNext()) {
                SelectionKey next = iterator.next();
                iterator.remove();
                try {
                    handlerInput(next);
                } catch (Exception e) {
                    e.printStackTrace();
                    if (next != null) {
                        next.cancel();
                        if (next.channel() != null) {
                            next.channel().close();
                        }
                    }

                }
            }
        }

    }

    public static void main(String[] args) throws IOException {
        new NioService().init();
    }

    private void handlerInput(SelectionKey key) throws IOException {
        if (key.isValid()) {
            if (key.isAcceptable()) {
                System.out.println("connect in");
                ServerSocketChannel channel = (ServerSocketChannel) key.channel();
                SocketChannel sc = channel.accept();
                sc.configureBlocking(false);
                sc.register(selector, SelectionKey.OP_READ);
                System.out.println("connect out");
            }
            if (key.isReadable()) {
                System.out.println("read");
                SocketChannel channel = (SocketChannel) key.channel();
                ByteBuffer allocate = ByteBuffer.allocate(1024);
                //写ByteBuffer 内部调用写ByteBuffer.put(读的数据)
                int read = channel.read(allocate);
                if (read > 0) {
                    allocate.flip();
                    byte[] bytes = new byte[allocate.remaining()];
                    allocate.get(bytes);

                    String body = new String(bytes, "UTF-8");
                    System.out.println("receive:" + body);
                    dowrite(channel);
                } else if (read == -1) {
                    key.cancel();
                    channel.close();
                }
            }
        }
    }

    private void dowrite(SocketChannel channel) throws IOException {
        String s = new Date().toLocaleString();
        byte[] bytes = s.getBytes(Charset.forName("UTF-8"));
        ByteBuffer bytebuffer = ByteBuffer.wrap(bytes);
        channel.write(bytebuffer);
        System.out.println("response:" + s);
    }


}
