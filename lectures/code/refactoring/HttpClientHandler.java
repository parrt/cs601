import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class HttpClientHandler implements Runnable {
	Socket socket;
	String docRoot;

	static public String errorPage(String msg){
		String htmlPage = "<html><head><title>Error!!!</title>" +
						  "<body text=#33FFFF bgcolor=#333333>"+
						  "<h1>Error!!!</h1>" +  "<p>" + msg + "</p>"+
						  "</body></html>";
		return htmlPage;
	}

	static public String htmlPage(String msg){
		String htmlPage = "<html><head><title>Display</title></head>" +
						  "<body text=#33FFFF bgcolor=#333333>" +
						  "<p>" + msg + "</p>" +
						  "</body></html>";
		return htmlPage;
	}

	public HttpClientHandler(Socket socket, String docRoot) {
		this.socket = socket;
		this.docRoot = docRoot;
	}

	public void run() {
		InputStream in = null;
		OutputStream out = null;
		DataInputStream din = null;
		try {
			// Get In and Out streams sorted
			out = socket.getOutputStream();
			in = socket.getInputStream();
			din = new DataInputStream(new BufferedInputStream(in));

			// Split the header string: requestPart[0] = GET, requestPart[1]= /filepath
			String headerLine = din.readLine();
			String[] requestPart = headerLine.split(" ");
			String command = requestPart[0];
			String filePath = requestPart[1];

			if (!command.startsWith("GET")) {
				out.write(errorPage("Server only accepts GET command.").getBytes());
			}
			else {
				processGet(out, filePath);
			}
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		finally {
			try {
				if ( in!=null ) in.close();
				if ( din!=null ) din.close();
				if ( out!=null ) out.close();
				socket.close();
			}
			catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public void processGet(OutputStream out, String filePath) throws IOException { }

	protected String getContentTypeHeader(String fileLocation) {
		String contentType;

		if ((fileLocation.endsWith(".html")) | (fileLocation.endsWith(".htm"))) {
			contentType = "Content-Type: text/html\n\n";
		}
		else if (fileLocation.endsWith(".gif")) {
			contentType = "Content-Type: image/gif\n\n";
		}
		else if (fileLocation.endsWith(".jpg")) {
			contentType = "Content-Type: image/jpg\n\n";
		}
		else {
			contentType = "Content-Type: \n\n";
		}
		return contentType;
	}
}
