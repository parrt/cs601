class JabberTest {
	public static void main(String[] args) {
		Jabber j = new Jabber("University of San Francisco");
		Jabber k = new Jabber("Computer Science 680");
		Thread t = new Thread(j);
		Thread u = new Thread(k);
		t.start();
		u.start();
	}
}
