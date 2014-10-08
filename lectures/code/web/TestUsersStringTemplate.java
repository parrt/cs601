import org.stringtemplate.v4.ST;
import org.stringtemplate.v4.STGroupDir;

public class TestUsersStringTemplate {
	public static void main(String[] args) {
		STGroupDir templates = new STGroupDir(".");
		ST st = templates.getInstanceOf("page");

		st.add("users", new MyUser(143, "parrt"));
		st.add("users", new MyUser(3, "tombu"));
		st.add("users", new MyUser(99, "kg9s"));
		System.out.println(st.render());
	}
}

