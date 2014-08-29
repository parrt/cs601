import java.io.File;
import java.io.IOException;

public class ReadData {
	public static void main(String[] args)  throws IOException {
		String fileName = args[0];
FileInputStream fis = new FileInputStream(filename);
DataInputStream dis = new DataInputStream(fis);
int fortyTwo = dis.readInt();
double pi = dis.readDouble();
dis.close();
	}
}
