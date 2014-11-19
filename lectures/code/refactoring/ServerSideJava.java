import java.io.IOException;
import java.io.OutputStream;

/** Any subclass of this will be executable by your http server */
public abstract class ServerSideJava {
	/** Invoked by your http server to print something meaningful
	 *  to 'out'.
	 *  The URL is the entire URL seen by the server; that is,
	 *  the entire "file name" after the HTTPD GET command your
	 *  server sees.  For example,
	 *  GET /index.html => URL=/index.html
	 *  GET /java/http.TheDate => URL=/java/http.TheDate
	 *  GET /java/http.Directory/a => URL=/java/http.Directory/a
	 *
	 *  Your client handle must send the URL and the OutputStream
	 *  to this method.
	 */
	public abstract void service(String URL,
								 OutputStream out) throws IOException;
}
