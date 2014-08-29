import java.io.File;
import java.io.IOException;

public class WriteData {
	public static void main(String[] args)  throws IOException {
		String fileName = args[0];
String filename = ...;
FileOutputStream fos = new FileOutputStream (filename);
DataOutputStream dos = new DataOutputStream (fos);
dos.writeInt (42);
dos.writeDouble (Math.PI);
dos.close();

	}
}
