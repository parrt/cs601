import java.net.*;

// http://java.sun.com/j2se/1.3/docs/api/java/net/InetAddress.html

public class IP {
    public static void main(String[] args) throws Exception {
        System.out.println(InetAddress.getLocalHost());
        System.out.println(InetAddress.getByName("maniac.cs.usfca.edu"));
    }
}
