import java.net.ServerSocket;
import java.net.Socket;
import java.io.*;

public class HttpServer {
	public static final int DEFAULT_PORT = 8080;

	String rootDir = ".";
	int port;

	public HttpServer(String rootDir, int port) {
		this.rootDir = rootDir;
		this.port = port;
	}

	public void accept() {
		ServerSocket server = null;
		// create a server listening at port, max 15 pending connections
		try {
			server = new ServerSocket(port, 15);
		}
		catch (IOException e) {
			System.err.println("cannot listen at "+port+" ("+e.getMessage()+")");
			return;
		}
		while (true) {
			// For each connection, start a new client handler
			// in its own thread and keep on listening
			try {
				Socket socket = server.accept();
				//  Create a new Thread for the client
				Thread clientThread = null;
				clientThread = new Thread(new ClientHandler(socket,rootDir));
				clientThread.start();
			}
			catch (IOException e) {
				System.err.println("Error creating socket connection");
				System.exit(1);
			}
		}
	}

    public static void main(String[] args) {
        if (args.length != 1) {
            System.err.println("Usage: java HttpServer root-dir");
            System.exit(0);
        }
		HttpServer server = new HttpServer(args[0], DEFAULT_PORT);
        server.accept();
    }
}
