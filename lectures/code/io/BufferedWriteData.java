import java.io.BufferedOutputStream;
import java.io.DataOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class BufferedWriteData {
	public static void main(String[] args)  throws IOException {
		FileOutputStream f = new FileOutputStream("/tmp/junk.data");
		BufferedOutputStream bf = new BufferedOutputStream(f);
		DataOutputStream dos = new DataOutputStream(bf);
		dos.writeInt(34);
		dos.close();
	}
}
