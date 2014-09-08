/**
 * SslReverseEchoer.java
 * Copyright (c) 2005 by Dr. Herong Yang
 */
import java.io.*;
import java.net.*;
import java.security.*;
import javax.net.ssl.*;
public class SslReverseEchoer {
   public static void main(String[] args) {
      String ksName = "herong.jks";
      char ksPass[] = "HerongJKS".toCharArray();
      char ctPass[] = "My1stKey".toCharArray();
      try {
         KeyStore ks = KeyStore.getInstance("JKS");
         ks.load(new FileInputStream(ksName), ksPass);
         KeyManagerFactory kmf = 
         KeyManagerFactory.getInstance("SunX509");
         kmf.init(ks, ctPass);
         SSLContext sc = SSLContext.getInstance("TLS");
         sc.init(kmf.getKeyManagers(), null, null);
         SSLServerSocketFactory ssf = sc.getServerSocketFactory();
         SSLServerSocket s 
            = (SSLServerSocket) ssf.createServerSocket(8888);
         printServerSocketInfo(s);
         SSLSocket c = (SSLSocket) s.accept();
         printSocketInfo(c);
         BufferedWriter w = new BufferedWriter(new OutputStreamWriter(
            c.getOutputStream()));
         BufferedReader r = new BufferedReader(new InputStreamReader(
            c.getInputStream()));
         String m = "Welcome to SSL Reverse Echo Server."+
            " Please type in some words.";
         w.write(m,0,m.length());
         w.newLine();
         w.flush();
         while ((m=r.readLine())!= null) {
            if (m.equals(".")) break;
            char[] a = m.toCharArray();
            int n = a.length;
            for (int i=0; i<n/2; i++) {
               char t = a[i];
               a[i] = a[n-1-i];
               a[n-i-1] = t;
            }
            w.write(a,0,n);
            w.newLine();
            w.flush();
         }
         w.close();
         r.close();
         c.close();
         s.close();
      } catch (Exception e) {
         System.err.println(e.toString());
      }
   }
   private static void printSocketInfo(SSLSocket s) {
      System.out.println("Socket class: "+s.getClass());
      System.out.println("   Remote address = "
         +s.getInetAddress().toString());
      System.out.println("   Remote port = "+s.getPort());
      System.out.println("   Local socket address = "
         +s.getLocalSocketAddress().toString());
      System.out.println("   Local address = "
         +s.getLocalAddress().toString());
      System.out.println("   Local port = "+s.getLocalPort());
      System.out.println("   Need client authentication = "
         +s.getNeedClientAuth());
      SSLSession ss = s.getSession();
      System.out.println("   Cipher suite = "+ss.getCipherSuite());
      System.out.println("   Protocol = "+ss.getProtocol());
   }
   private static void printServerSocketInfo(SSLServerSocket s) {
      System.out.println("Server socket class: "+s.getClass());
      System.out.println("   Socker address = "
         +s.getInetAddress().toString());
      System.out.println("   Socker port = "
         +s.getLocalPort());
      System.out.println("   Need client authentication = "
         +s.getNeedClientAuth());
      System.out.println("   Want client authentication = "
         +s.getWantClientAuth());
      System.out.println("   Use client mode = "
         +s.getUseClientMode());
   } 
}
