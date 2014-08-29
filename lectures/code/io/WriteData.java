import java.io.DataOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class WriteData {
	public static void main(String[] args)  throws IOException {
		FileOutputStream fos = new FileOutputStream("/tmp/junk.data");
		DataOutputStream dos = new DataOutputStream(fos);
		dos.writeInt (42);
		dos.writeDouble (Math.PI);
		dos.close();
	}
}
