import java.util.Locale;

public class DefaultFileEncoding {
	public static void main(String[] args) {
		Locale locale = Locale.getDefault();
		String encoding = System.getProperty("file.encoding");
		System.out.printf("Default encoding for %s is %s\n", locale, encoding);
	}
}
