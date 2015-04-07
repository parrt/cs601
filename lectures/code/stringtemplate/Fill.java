import org.stringtemplate.v4.ST;
import org.stringtemplate.v4.STGroup;
import org.stringtemplate.v4.STGroupDir;

public class Fill {
	public static class User {
		public String name;
		public String phone;
		public User(String name, String phone) {
			this.name=name;
			this.phone=phone;
		}
		public String toString() { return name+":"+phone; }
	}

	public static void main(String[] args) {
		String[] names = {"Terence", "Jim", "Ketaki", "Siddharth"};
		String[] phones = {"x1", "x9", "x2", "x5"};
		User[] users = {new User("Tom", "x99"), new User("Jane","x1")};

		STGroup group = new STGroupDir(".");
		ST st = group.getInstanceOf("fill");
		st.add("names", names);
		st.add("phones", phones);
		st.add("users", users);
		System.out.println(st.toString());
	}
}
