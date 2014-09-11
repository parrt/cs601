import java.net.*;
import java.io.*;

// http://java.sun.com/j2se/1.3/docs/api/java/net/Socket.html
// http://java.sun.com/j2se/1.3/docs/api/java/net/ServerSocket.html

public class ThreadedServer {
	public static final boolean singleThreaded = false;

	public static void main(String[] args) throws IOException {
		ServerSocket s = new ServerSocket(8080);
		while ( true ) {
			Socket channel = s.accept();
			Runnable handler = new Handler(channel);
			if ( singleThreaded ) {
				handler.run();
			}
			else {
				Thread t = new Thread(handler);
				t.start();
			}
		}
	}

	static class Handler implements Runnable {
		Socket channel;
		Handler(Socket channel) {
			this.channel = channel;
		}

		@Override
		public void run() {
			try {
				processRequest(channel);
			}
			catch (IOException ioe) {
				ioe.printStackTrace(System.err);
			}
		}

		void processRequest(Socket channel) throws IOException {
			System.out.println("[inside] coming live to you from radio USF on port: "+
				channel.getPort());
			OutputStream out = channel.getOutputStream();
			InputStream in = channel.getInputStream();
			DataInputStream din = new DataInputStream(in);
			String line = din.readLine();
			while (line != null) {
				System.out.printf("%d %s\n", channel.getPort(), line);
				line = din.readLine();
			}
			din.close();
			channel.close();
			System.out.println("[inside] closing port: "+
				channel.getPort());
		}
	}
}
