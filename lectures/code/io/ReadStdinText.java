import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class ReadStdinText {
	public static void main(String[] args) throws IOException {
		InputStreamReader isr = new InputStreamReader(System.in);
		BufferedReader reader = new BufferedReader(isr);
		String line = reader.readLine();
		while (line != null) {
			// Process line
			line = reader.readLine();
		}
	}
}
