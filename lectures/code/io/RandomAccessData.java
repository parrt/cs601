import java.io.IOException;
import java.io.RandomAccessFile;

public class RandomAccessData {
	public static void main(String[] args)  throws IOException {
		RandomAccessFile f = new RandomAccessFile("/tmp/junk.data", "rw");
		f.writeInt(34);
		f.writeDouble(3.14159);
		f.seek(0);  // rewind and read again
		int i = f.readInt();
		double d = f.readDouble();
		f.close();
		System.out.printf("read %d and %1.5f\n", i, d);
	}
}
