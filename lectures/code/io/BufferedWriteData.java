import java.io.File;
import java.io.IOException;

public class BufferedWriteData.java {
	public static void main(String[] args)  throws IOException {
		String fileName = args[0];
FileOutputStream f = new FileOutputStream("junk");
BufferedOutputStream bf = new BufferedOutputStream(f);
DataOutputStream dos = new DataOutputStream (bf);
dos.writeInt(34);
dos.close();

	}
}
