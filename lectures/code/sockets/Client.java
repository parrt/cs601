import java.net.*;
import java.io.*;

// http://java.sun.com/j2se/1.3/docs/api/java/net/Socket.html

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

