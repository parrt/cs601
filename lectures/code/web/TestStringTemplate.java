import org.stringtemplate.v4.ST;
import org.stringtemplate.v4.STGroupDir;

import java.awt.*;

public class TestStringTemplate {
	public static void main(String[] args) {
		ST query = new ST("SELECT <column; separator={<sep>}> FROM <table>;");
		query.add("column", "subject");
		query.add("column", "body");
		query.add("sep", new ST(", "));
		query.add("table", new String[] {"email","users"});
		// now add multiple tables and columns; add separator
		System.out.println("QUERY: "+query.render());
		int[] data = {1,2};
		for (int x : data) {

		}
	}
}

