import org.stringtemplate.v4.ST;
import org.stringtemplate.v4.STGroup;
import org.stringtemplate.v4.STGroupDir;

class Test {
	public static void main(String[] args) {
		// all attributes are single-valued:
		ST query =
				new ST("SELECT <column> FROM <table>;");
		query.add("column", "name");
		query.add("table", "User");
		System.out.println("QUERY: "+query.render());

		// if column can be multi-valued, specify a separator
		query = new ST("SELECT <distinct> <column; separator=\", \"> FROM <table>;");
		query.add("column", "name");
		query.add("column", "email");
		query.add("table", "User");
		// uncomment next line to make "DISTINCT" appear in output
		query.add("distinct", "DISTINCT");
		System.out.println("QUERY: "+query.render());

		// specify a template to apply to an attribute
		// Use a template group so we can specify the start/stop chars
		ST list = new ST("<ul>$items$</ul>", '$', '$');
		// demonstrate setting arg to anonymous subtemplate
		// could use $item[i]$ in template, but want to show args here.
		ST item = new ST("$item:{x | <li>x</li>}$", '$', '$');
		item.add("item", "Terence");
		item.add("item", "Jim");
		item.add("item", "John");
		list.add("items", item); // nested template
		System.out.println("HTML: "+list.render());

		// Look for templates in CLASSPATH as resources
		STGroup mgroup = new STGroupDir(".");
		ST m = mgroup.getInstanceOf("method");
		// "method.st" references body() so "body.st" will be loaded too
		m.add("visibility", "public");
		m.add("name", "foobar");
        m.add("returnType", "void");
        System.out.println("Java: " +m.render());

        ST t = new ST("List:\n"+
                      "$names:{n | <br>$i$. $n$\n}$\n", '$', '$');
        t.add("names", "Terence");
        t.add("names", "Jim");
        t.add("names", "Sriram");
		System.out.println("HTML: " +t.render());
	}
}
