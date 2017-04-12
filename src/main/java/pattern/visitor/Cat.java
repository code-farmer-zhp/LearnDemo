package pattern.visitor;


import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

public class Cat implements Visitable {
    @Override
    public void accept(Visitor v) {
        v.visit(this);
    }

    public static void main(String[] args) throws Exception {
        RandomAccessFile randomAccessFile = new RandomAccessFile("","r");
        FileChannel channel = randomAccessFile.getChannel();
        MappedByteBuffer byteBuffer = channel.map(FileChannel.MapMode.READ_ONLY, 0, randomAccessFile.length());



    }
}
