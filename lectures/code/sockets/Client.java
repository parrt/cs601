import java.net.*;
import java.io.*;

public class Client {
	public static void main(String[] args) throws IOException {
		Socket s = new Socket("localhost", 8080);
		OutputStream out = s.getOutputStream();
		PrintStream pout = new PrintStream(out);
		pout.println("hi from java client");
		pout.close();
		s.close();
	}
}

