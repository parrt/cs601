import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.IOException;

public class ReadData {
	public static void main(String[] args)  throws IOException {
		FileInputStream fis = new FileInputStream("/tmp/junk.data");
		DataInputStream dis = new DataInputStream(fis);
		int fortyTwo = dis.readInt();
		double pi = dis.readDouble();
		dis.close();
		System.out.printf("read %d and %1.5f\n", fortyTwo, pi);
	}
}
