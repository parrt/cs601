import java.io.File;
import java.io.IOException;

public class ReadText {
	public static void main(String[] args)  throws IOException {
		String fileName = args[0];
InputStreamReader isr = new InputStreamReader(System.in);
BufferedReader reader = new BufferedReader(isr);
String line = reader.readLine();
while ( line!=null ) {
  // Process line
  line = reader.readLine();
}
}
