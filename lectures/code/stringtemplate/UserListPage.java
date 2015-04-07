import org.stringtemplate.v4.ST;

public class UserListPage extends Page {
	public static class User {
		String name;
		int age;
		public User(String name, int age) {
			this.name = name;
			this.age = age;
		}
		public String getName() { return name; }
		public int getAge() { return age; }
		public String toString() { return name+":"+age; }
	}

	static User[] users = new User[] {
		new User("Boris", 39),
		new User("Natasha", 31),
		new User("Jorge", 25),
		new User("Vladimir", 28)
	};

	/** This "controller" pulls from "model" and pushes to "view" */
	public ST generateBody() {
		ST bodyST = templates.getInstanceOf("userlist");
		User[] list = users; // normally pull from database
		// filter list if you want here (not in template)
		bodyST.add("users", list);
		return bodyST;
	}

	public String getTitle() { return "User List"; }

	/** Testing */
	public static void main(String[] args) {
		new UserListPage().generate();
	}
}

