import org.stringtemplate.v4.ST;
import org.stringtemplate.v4.STGroupDir;

import java.awt.*;

public class TestStringTemplate {
	public static void main(String[] args) {
		ST query = new ST("SELECT <column; separator=\",\"> FROM <table; separator=\",\">;");
		query.add("column", "subject");
		query.add("column", "body");
//		query.add("sep", new ST(", "));
		query.add("table", new String[] {"email","users"});
//		query.add("table", "email");
		// now add multiple tables and columns; add separator
		System.out.println("QUERY: "+query.render());
	}
}

