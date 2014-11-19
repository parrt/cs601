import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;

public class FileAndJavaHttpHandler extends HttpClientHandler {
	public FileAndJavaHttpHandler(Socket socket, String docRoot) {
		super(socket, docRoot);
	}

	public void processGet(OutputStream out, String filePath) throws IOException {
		String fileLocation = docRoot + filePath;

		if (filePath.startsWith("/java")) {
			execJava(filePath, out);
		}
		else {
			if (filePath.equals("/")) {
				filePath = indexFileHandler();
				fileLocation = docRoot + filePath;
			}
			downloadFile(fileLocation, out);
		}
	}

	private void downloadFile(String fileLocation, OutputStream out) throws IOException {
		File f = new File(fileLocation);

		if (f.exists() || f.isDirectory()) {
			FileInputStream inFile = new FileInputStream(f);

			// Sort out header
			String httpHeader = "HTTP/1.0 200 OK\n";

			//Determine Content-Type
			String contentType = getContentTypeHeader(fileLocation);
			try {
				// Write headers
				out.write(httpHeader.getBytes());
				out.write(contentType.getBytes());

				// Handle binary file
				byte[] buffer = new byte[1024];
				int readCount = 0;

				while ((readCount = inFile.read(buffer)) > 0) {
					out.write(buffer, 0, readCount);
				}
			}
			finally {
				inFile.close();
			}
		}
		else {
			out.write(errorPage("File doesn't exist.").getBytes());
		}
	}

	private void execJava(String path, OutputStream out) throws IOException {
		String[] pathPart = path.split("/");
		// check for no path
		if (pathPart.length < 3) {
			out.write(errorPage("Enter fully-qualified-classname after /java.").getBytes());
			out.flush();
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
				out.write(errorPage("Java class " + className + " not ServerSideJava.").getBytes());
				out.flush();
			}
		}
		catch (Exception e) {
			e.printStackTrace(System.err);
			out.write(errorPage("<p>Cannot execute!!! </p>" + e.getMessage()).getBytes());
		}
	}

	private String indexFileHandler() {
		File indexFile = new File(docRoot + "/index.htm");
		if (indexFile.exists()) {
			return "/index.htm";
		}
		else {
			return "/index.html";
		}
	}

}
