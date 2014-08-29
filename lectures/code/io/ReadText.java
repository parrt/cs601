import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class ReadText {
	public static void main(String[] args) throws IOException {
		String fileName = args[0];
		InputStreamReader isr = new InputStreamReader(System.in);
		BufferedReader reader = new BufferedReader(isr);
		String line = reader.readLine();
		while (line != null) {
			// Process line
			line = reader.readLine();
		}
	}
}
