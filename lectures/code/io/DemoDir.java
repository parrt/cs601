import java.io.File;
import java.io.IOException;
import java.util.Arrays;

public class DemoDir {
	public static void main(String[] args)  throws IOException {
		String fileName = args[0];
		File f = new File(fileName);
		if ( f.exists() && f.isDirectory() ) {
			String dirListing[] = f.list();
			System.out.println(Arrays.toString(dirListing));
		}
		else {
			System.err.println("Doesn't exist or isn't a dir");
		}
	}
}
