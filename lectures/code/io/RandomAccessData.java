import java.io.File;
import java.io.IOException;

public class RandomAccessData {
	public static void main(String[] args)  throws IOException {
RandomAccessFile f = new RandomAccessFile("junk", "rw");
f.writeInt(34);
f.writeDouble(3.14159);
f.seek(0);
int i = f.readInt();
double d = f.readDouble();
f.close();

		String fileName = args[0];
	}
}
