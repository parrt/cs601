import org.stringtemplate.v4.ST;
import org.stringtemplate.v4.STGroup;
import org.stringtemplate.v4.STGroupDir;

public abstract class Page {
	static STGroup templates = new STGroupDir("skin1", '$', '$');
	public void generate() {
		ST pageST = templates.getInstanceOf("page");
		ST bodyST = generateBody();
		pageST.add("body", bodyST);
		pageST.add("title", getTitle());

		pageST.inspect();

		System.out.println(pageST);
	}
	public abstract ST generateBody();
	public abstract String getTitle();
}
