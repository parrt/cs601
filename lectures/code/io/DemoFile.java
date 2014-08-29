import java.io.File;
import java.io.IOException;

public class DemoFile {
	public static void main(String[] args)  throws IOException {
		String fileName = args[0];
		File f = new File(fileName);
		System.out.println("file " + fileName + " is " +
						   f.length() + " bytes long");
		System.out.println("Path: " + f.getCanonicalPath());
	}
}
