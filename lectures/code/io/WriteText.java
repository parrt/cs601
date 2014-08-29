import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class WriteText {
	public static void main(String[] args)  throws IOException {
		File f = new File("/tmp/junk.txt");
		FileWriter fw = new FileWriter(f);
		PrintWriter pw = new PrintWriter(fw);
		pw.println("Welcome home");
		pw.close();
	}
}
