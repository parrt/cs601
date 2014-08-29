import java.io.File;
import java.io.IOException;

public class WriteText {
	public static void main(String[] args)  throws IOException {
String fileName = "/tmp/more-examples";
File f = new File(fileName);
FileWriter fw = new FileWriter (f);
PrintWriter pw = new PrintWriter (fw);
pw.println("Welcome home");
pw.close();

		String fileName = args[0];
		File f = new File(fileName);
		System.out.println("file " + fileName + " is " +
						   f.length() + " bytes long");
		System.out.println("Path: " + f.getCanonicalPath());
	}
}
