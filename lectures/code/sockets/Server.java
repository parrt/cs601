import java.net.*;
import java.io.*;

// http://java.sun.com/j2se/1.3/docs/api/java/net/Socket.html
// http://java.sun.com/j2se/1.3/docs/api/java/net/ServerSocket.html

public class Server {
	public static void main(String[] args) throws IOException {
		int port = Integer.valueOf(args[0]);
		ServerSocket s = new ServerSocket(port);
		while ( true ) {
			Socket channel = s.accept();
			OutputStream out = channel.getOutputStream();
			InputStream in = channel.getInputStream();
			DataInputStream din = new DataInputStream(in);
			String line = din.readLine();
			while ( line!=null ) {
				System.out.println(line);
				line = din.readLine();
			}
			din.close();
			channel.close();
		}
	}
}
