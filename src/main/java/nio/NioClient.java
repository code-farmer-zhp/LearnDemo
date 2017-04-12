package nio;


import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Date;
import java.util.Iterator;
import java.util.Set;

public class NioClient implements Runnable {

    SocketChannel socketChannel;

    Selector selector;

    public void init() throws IOException {
        selector = Selector.open();
        socketChannel = SocketChannel.open();
        socketChannel.configureBlocking(false);
    }

    public static void main(String[] args) throws IOException {
        NioClient nioClient = new NioClient();
        nioClient.init();
        new Thread(nioClient).start();
    }
    @Override
    public void run() {
        try {
            boolean connect = socketChannel.connect(new InetSocketAddress(9099));
            if (connect) {
                socketChannel.register(selector, SelectionKey.OP_READ);
                doWrite();

            } else {
                socketChannel.register(selector, SelectionKey.OP_CONNECT);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        while (true) {
            try {
                selector.select();
                Set<SelectionKey> selectionKeys = selector.selectedKeys();
                Iterator<SelectionKey> iterator = selectionKeys.iterator();
                while (iterator.hasNext()) {
                    SelectionKey key = iterator.next();
                    iterator.remove();
                    handlerInput(key);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void doWrite() throws IOException {
        String s = "clientRequest:" + new Date().toLocaleString();
        ByteBuffer write = ByteBuffer.wrap(s.getBytes());
        socketChannel.write(write);
        if (!write.hasRemaining()) {
            System.out.println("send success!");
        }
    }

    private void handlerInput(SelectionKey key) throws Exception {
        if (key.isValid()) {
            if (key.isConnectable()) {
                SocketChannel channel = (SocketChannel) key.channel();
                if (channel.finishConnect()) {
                    channel.register(selector, SelectionKey.OP_READ);
                    doWrite();
                } else {
                    System.out.println("connect fail");
                }
            }
            if (key.isReadable()) {
                SocketChannel channel = (SocketChannel) key.channel();
                ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
                int read = channel.read(byteBuffer);
                if (read > 0) {
                    byteBuffer.flip();
                    byte[] bytes = new byte[byteBuffer.remaining()];
                    byteBuffer.get(bytes);
                    String body = new String(bytes, "UTF-8");
                    System.out.println("Now is " + body);
                } else if (read < 0) {
                    key.cancel();
                    channel.close();
                }
            }

        }


    }
}
