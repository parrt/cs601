import java.net.*;
import java.io.*;
import java.util.*;

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
	Socket socket;
	String docRoot;
	String fileLocation;
	OutputStream out = null;
	PrintStream p = null;
	InputStream in = null;
	BufferedReader buf;

	public ClientHandler(Socket socket, String docRoot) {
		this.socket = socket;
		this.docRoot = docRoot;
	}

	public void run() {
		try {
			// Get In and Out streams sorted
			out = socket.getOutputStream();
			p = new PrintStream(out);
			in = socket.getInputStream();
			//Using bufferedReader in instead because readline is not deprecated
			buf = new BufferedReader(new InputStreamReader(in));

			// Split the header string: requestPart[0] = GET, requestPart[1]= /filepath
			String headerLine = buf.readLine();
			String[] requestPart;
			requestPart = headerLine.split(" ");
			String get = requestPart[0];
			String filePath = requestPart[1];

			if (!get.startsWith("GET")) {
				p.print(ErrorManager("Server only accepts GET command."));
				closeAll();
			}
			else {
				fileLocation = docRoot + filePath;

				if (filePath.startsWith("/java")) {
					execJava(filePath);
					closeAll();
				}
				else {
					if (filePath.equals("/")) {
						filePath = indexFileHandler();
						fileLocation = docRoot + filePath;
					}
					downloadFile(fileLocation);
				}
			}
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}
	private void downloadFile(String fileLocation) throws IOException {
		File f = new File(fileLocation);

		if (f.exists() || f.isDirectory()) {
			FileInputStream inFile = new FileInputStream(f);

			// Sort out header
			String httpHeader = "HTTP/1.0 200 OK\n";

			//Determine Content-Type
			String contentType;

			if ((fileLocation.endsWith(".html") == true) | (fileLocation.endsWith(".htm") == true)) {
				contentType = "Content-Type: text/html\n\n";
			}
			else if (fileLocation.endsWith(".gif") == true) {
				contentType = "Content-Type: image/gif\n\n";
			}
			else if (fileLocation.endsWith(".jpg") == true) {
				contentType = "Content-Type: image/jpg\n\n";
			}
			else {
				contentType = "Content-Type: \n\n";
			}
			// Write headers
			out.write(httpHeader.getBytes());
			out.write(contentType.getBytes());

			// Handle binary file
			byte[] buffer = new byte[1024];
			int readCount = 0;

			while ((readCount = inFile.read(buffer)) >0) {
				out.write(buffer, 0, readCount);
			}
		}
		else {
			p.print(ErrorManager("File doesn't exist."));
		}
		closeAll();
	}

	private Object execJava(String path) throws IOException {
		String[] pathPart = path.split("/");
		// check for no path
		if (pathPart.length < 3) {
			p.println(ErrorManager("Enter fully-qualified-classname after /java."));
			p.flush();
			closeAll();
			return null;
		}
		String className = pathPart[2];
		if (className.equals("http.Directory")) {
			path = docRoot + "/" + path;
		}

		try {
			Class c = Class.forName(className);
			Object o = c.newInstance();
			if (o instanceof ServerSideJava) {
				((ServerSideJava) o).service(path, out);
			}
			else {
				p.println(ErrorManager("Java class " + className + " not ServerSideJava."));
				p.flush();
			}
		}
		catch (ClassNotFoundException c) {
			c.printStackTrace(System.err);
			p.println(ErrorManager("<p>Cannot execute!!! </p>" + c.toString()));
			closeAll();
			return null;
		}
		catch (InstantiationException i) {
			i.printStackTrace(System.err);
			p.println(ErrorManager("<p>Cannot execute!!! </p>" + i.toString()));
			closeAll();
			return null;
		}
		catch (IllegalAccessException il) {
			il.printStackTrace(System.err);
			p.println(ErrorManager("<p>Cannot execute!!! </p>" + il.toString()));
			closeAll();
			return null;
		}
		closeAll();
		return null;
	}

	private String indexFileHandler() {
		File indexFile = new File(docRoot + "/index.htm");
		if (indexFile.exists() == true) {
			return "/index.htm";
		}
		else {
			return "/index.html";
		}
	}
	private void closeAll() throws IOException {
		out.close();
		p.close();
		in.close();
		buf.close();
		socket.close();
	}
	static public String ErrorManager(String contentIn){
		String htmlPage = "<html><head><title>Error!!!</title>" +
						  "<body text=#33FFFF bgcolor=#333333>"+
						  "<h1>Error!!!</h1>" +  "<p>" + contentIn + "</p>"+
						  "</body></html>";
		return htmlPage;
	}
	static public String htmlPage(String contentIn){
		String htmlPage = "<html><head><title>Display</title></head>" +
						  "<body text=#33FFFF bgcolor=#333333>" +
						  "<p>" + contentIn + "</p>" +
						  "</body></html>";
		return htmlPage;
	}
}
