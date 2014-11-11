import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;
import java.util.StringTokenizer;

public class ClientHandler implements Runnable {
	protected DataInputStream  in;
	protected PrintStream      out;
	protected Socket socket;
	protected String rootDir;

	public ClientHandler(Socket socket, String rootDir)
		throws IOException
	{
		this.socket = socket;
		in = new DataInputStream(socket.getInputStream());
		out = new PrintStream(socket.getOutputStream());
		this.rootDir = rootDir;
	}

	public void run() {
		try {
			String line = in.readLine();
			if ( line!=null ) {
				process(out, line);
			}
			socket.shutdownInput();
			socket.shutdownOutput();
			out.close();
			in.close();
			socket.close();
		}
		catch (IOException e) {
			e.printStackTrace(System.err);
			// do nothing; IO error means end of file
		}
	}

	protected void process(PrintStream out, String line) {
		if ( !line.startsWith("GET") ) {
			return;
		}
		StringTokenizer st = new StringTokenizer(line);
		st.nextToken(); 					// skip GET
		String fileName = st.nextToken();	// get file
		dumpFile(out, rootDir+fileName);
	}

	protected void dumpFile(PrintStream out, String fileName) {
		File f = new File(fileName);
		if ( !f.exists() ) {
			// doesn't exist, send error message
			out.println("<html><body>Missing file "+fileName+"</body></html>");
			return;
		}
		FileInputStream inFile = null;
		try {
			inFile = new FileInputStream(fileName);
			// System.out.println("reading "+fileName);
			byte[] buffer = new byte[1024];
			int readCount;

			while( (readCount = inFile.read(buffer)) > 0) {
				out.write(buffer, 0, readCount);
			}
			out.flush();
		}
		// If something went wrong, report it!
		catch(IOException e) {
			System.err.println("Couldn't send "+fileName);
			e.printStackTrace(System.err);
		}
		finally {
			try {
				if ( inFile!=null ) {
					inFile.close();
				}
			}
			catch(IOException e) {
				System.err.println("Couldn't close "+fileName);
			}
		}
	}
	void foo() {
		Airplane a = null;
		Bird b = null;
		FlyingSquirrel s = new FlyingSquirrel();

		CanFly f;

		f = a; // 1
		f = b; // 2
		f = s; // 3
		f = (CanFly)s; // 4
		a = b; // 5
	}
}

interface CanFly {
  public void fly();
}

class Airplane implements CanFly {
  public void fly() {}
}

class Bird implements CanFly {
  public void fly() {}
}

class FlyingSquirrel {
  public void fly() {}
}
