import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;

public class FileEncodingRead {
	public static void main(String[] args) throws IOException {
		String encoding = args[0];
		String fileName = args[1];
		FileInputStream isr = new FileInputStream(fileName);
		Reader r = new InputStreamReader(isr, encoding);
		BufferedReader br = new BufferedReader(r);
		String data = readFully(br);
		System.out.println(data);
	}

	public static String readFully(BufferedReader r) throws IOException {
		StringBuilder buf = new StringBuilder();
		String line = r.readLine();
		while (line != null) {
			buf.append(line);
			line = r.readLine();
		}
		return buf.toString();
	}
}
