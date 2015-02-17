import java.net.*;
import java.io.*;

public class Server {
	public static void main(String[] args) throws IOException {
		int port = Integer.valueOf(args[0]);
		ServerSocket s = new ServerSocket(port);
		while ( true ) {
			System.out.println("waiting for connection...");
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
