import java.net.Socket;
import java.net.ServerSocket;
import java.io.*;

public class PizzaHut {
	public static final int PIZZA_HUT_PHONE_NUMBER = 8080;
	boolean openForBusiness = true;

	public static void main(String[] args) {
		try {
			PizzaHut restaurant = new PizzaHut();
			restaurant.startAnsweringPhone();
		}
		catch (IOException ioe) {
			System.err.println("Can't open for business!");
			ioe.printStackTrace(System.err);
		}
	}

	public void startAnsweringPhone() throws IOException {
		ServerSocket phone = new ServerSocket(PIZZA_HUT_PHONE_NUMBER);
		while (openForBusiness) {
			DataInputStream din = null;
			PrintStream pout = null;
			Socket phoneCall = null;
			try {
				// wait for a call; sleep while you are waiting
				phoneCall = phone.accept();
				// get an input stream (the headset speaker)
				InputStream in = phoneCall.getInputStream();
				din = new DataInputStream(in);
				// get an output stream (the microphone)
				OutputStream out = phoneCall.getOutputStream();
				pout = new PrintStream(out);

				// say hello
				pout.println("hello, Pizza Hut, how may I help you?");
				// take the order
				String order = din.readLine();
				// read it back to customer
				pout.println("your order: "+order); 

				createPizza(order);
			}
			finally {
				din.close();
				pout.close();
				phoneCall.close();
			}
		}
	}

	protected void createPizza(String order) {
		// parse order and perform work
	}
}

