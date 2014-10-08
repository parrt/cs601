import org.stringtemplate.v4.ST;
import org.stringtemplate.v4.STGroupDir;

public class TestStringTemplate {
	public static void main(String[] args) {
		ST query = new ST("SELECT <column> FROM <table>;");
		query.add("column", "subject");
		query.add("table", "emails");
		// now add multiple tables and columns; add separator
		System.out.println("QUERY: "+query.render());
	}
}

