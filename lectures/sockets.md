Sockets and network programming
====

[Terence Parr](http://parrt.cs.usfca.edu)

# Introduction

Programs running on different computers need to communicate, with the most obvious being a browser and a Web server. It also could be a database client and server. Lots of mechanisms have appeared over the years including remote procedure call (RPC) stuff like CORBA, COM, etc...  Even UNIX System V (AT&T) in the 70s had inter-process communication that would work across a network but the basic mechanism everybody uses these days is called Sockets, and came out of Berkeley BSD UNIX in the late 70s.

## From pipes to sockets

Separate processes (not threads) on the same computer may share data and synchronize via _pipes_.  For example,

```bash
$ ls | grep Aug
```

pipes the output of `ls` to the input of `grep` using the UNIX `pipe()` function that sets up a one-way data flow from one process to another.

But, what about connecting processes on separate computers? Java provides access to OS _sockets_ that allow two or more processes on the same or different computers to send/receive data. There are two kinds of sockets that we care about, one that acts like a phone connection and one that acts like a radio that spews data packets. The good news is that we can treat sockets just like files or any other stream of data from a programming perspective. Of course, we still have to keep in mind that it is a slow link across a network versus in memory on the same machine. (Unless we are connected to a socket on the same machine.)

# Background

## IP

First, we need to talk about the IP protocol, which is really the lowest level abstraction above the hardware (at leased from my point of view).  *Please distinguish IP protocol from ethernet or any other physical networking mechanism.* This is a data protocol that sits on top of some physical network.

IP is an addressing and fragmentation protocol.  It breaks all communications into _packets_, chunks of data up to 65536 bytes long.  Packets are individually _routed_ from source to destination.  IP is allowed to drop packets; i.e., it is an unreliable protocol.  There are no acknowledgements and no retransmissions.  There is no flow-control such as there is in a RS-232 serial interface.

One way to think of IP communication is by analogy to communications via a letter. You write the letter (this is the data you are sending); put the letter inside an envelope (the IP packet); address the envelope (using an IP address); put your return address on the envelope (your local IP address); and then you send the letter.  Like a real letter, you have no way of knowing whether an IP packet was received. If you send a second letter one day after the first, the second one may be received before the first. Or, the second one may never be received.

IP uses _IP addresses_ to define source/target.  IPs are 32 bit numbers represented as 4 8-bit numbers separated by periods.  When you try to visit `www.cnn.com` in your browser, the computer must first translate `www.cnn.com` to an IP address.  Then the browser can make a connection to the web server on the target machine identified by the IP address.  You can think of this as the "phone number" of a machine.  Special IPs:

* Behind firewalls, people often use 192.168.x.y and use NAT (_network address translation_) in their firewall to translate an outside address (a real IP) to the special IPs behind the wall. In this case there is an external or public IP address and a private IP address. My `maniac.cs.usfca.edu` machine has public IP 138.202.170.154 but internal IP 10.10.10.51 or something like that.
* 127.0.0.1 is "localhost"

A good security feature to hide your machines from outside.  For example, all machines from within IBM's firewall probably look like the exact same IP address to the outside world (for example, in web server log files).  That is one reason you cannot use an IP address to identify "sessions" for a web server application.

Here is a simple Java program that prints out the current machines IP address and the address of jguru:

```java
import java.net.*;

// http://java.sun.com/j2se/1.3/docs/api/java/net/InetAddress.html

public class IP {
    public static void main(String[] args) throws Exception {
        System.out.println(InetAddress.getLocalHost());
        System.out.println(InetAddress.getByName("www.jguru.com"));
    }
}
```

When you run it, you get:

```bash
$ javac IP.java
$ java IP
terence.local/192.168.1.8
maniac.cs.usfca.edu/138.202.170.154
$
```

This is similar to doing:

```bash
$ nslookup maniac.cs.usfca.edu
```

## UDP

UDP (_User Datagram Protocol_) is a _connectionless_ protocol sitting on top of IP that provides *unreliable* packet delivery.  It essentially provides user-level access to the low-level IP hardware.  But adds _port numbers_ and _checksumming_ for error handling (UDP can drop bad packets).

* UDP packets arrive out of order possibly or even not at all.
* The target/recipient does not acknowledge receipt
* There is no control (e.g., packets can arrive faster than the recipient can process them).

Useful for games (sending position), network time services, internet telephony, DNS, streaming video.

UDP is much faster than TCP.

## TCP

TCP (_Transmission Control Protocol_) is another protocol, a reliable but slower one, sitting on top of IP.  Believe it or not it comes from the 1970s. TCP provides reliable, stream-oriented connections; can treat the connection like a stream/file rather than packets.  Packets are ordered into the proper sequence at the target machine via use of _sequence numbers_.  TCP automatically deals with lost packets before delivering a complete "file" to a recipient.  Control-flow prevents buffer overflows etc...

TCP is like a phone connection versus the simple "fire and forget" letter stateless style of UDP.  TCP sockets are open for the duration of a communication (i.e., until you close the connection).

Unlike UDP, the destination host and port number is not sufficient to identify a recipient of a TCP connection. There are five distinct elements that make a TCP *connection* unique:

1. IP address of the server 
1. IP address of the client 
1. Port number of the server 
1. Port number of the client (data goes out a socket from source too)
1. Protocol (UDP, TCP/IP, etc...) 

where each requested client socket is assigned a unique port number leaving the client whereas the server port number is always the same. If any of these numbers is different, the socket is different.  A server can thus listen to one and only one port yet talk to multiple clients at the same time!

A TCP *header* contains a sequence number and error correcting information that tells TCP how to order and get a complete chunk of data to or from the socket.

## What is a socket?

If the IP address is like an office building main phone number, a socket is like the extension numbers for offices.  So the IP and socket, often called the port, uniquely identify an "office" (server process).  You will see unique identifiers like `192.168.2.100:80` where 80 is the port.  Just like in an office, it is possible no process is listening at a port.  That is, there is no server waiting for requests at that port.

Ports run from 1..65535.  1..1024 require root privileges to use and ports 1..255 are reserved for common, publicly-defined server ports like:

* 80: HTTP
* 110: POP
* 25: SMTP
* 22: SSH

Continuing the office analogy further, just because you can open a connection to a port doesn't mean you can speak the right language.  Processes at ports all speak a specific, predefined, agreed-upon protocol like HTTP. To effectively communicate you need to know both the address and the protocol.

You can use `telnet` to connect to ports to manually speak the protocol.  The most successful and long-lived protocols are simple and text based.

Here I connect to the POP server at jguru:

```bash
$ telnet pop.jguru.com 110
Trying 65.219.20.147...
Connected to pop.jguru.com.
Escape character is '^]'.
+OK POP3 lobby v6.50 server ready
QUIT
+OK Sayonara
Connection closed by foreign host.
```

Or, you can connect to the SSH port 22:

```bash
$ telnet www.cs.usfca.edu 22
Trying 138.202.170.2...
Connected to www.cs.usfca.edu.
Escape character is '^]'.
SSH-2.0-OpenSSH_5.3
```

# Sockets and Security

Because sockets are the means by which computers on a network communicate, they open your computer to attack.  The simplest possible attack is a _denial of service_ just like a telemarketer that calls you at home incessantly.  Another common attack is to exploit a vulnerability in a particular program listening at a port.  Sometimes it's possible to trick a listening server program into allowing unauthorized access to that program or even the whole computer.  The hacker either wants data on the server or wants to use the machine as a _mule_ (launch further attacks from that machine to (a) make it difficult to trace back to the hacker and (b) launch multiple simultaneous attacks).

The single most common vulnerability in server software is probably _buffer overflow_.  By overwriting a buffer, the software crashes, is convinced to allow access, or execute some code sent by the hacker, thus, providing access.  Here is a simple C program that illustrates how a single buffer overflow can crash a server.  The program, which will most likely never return from function `gone()`, depending on the operating system and compiler:

```C
#include <stdio.h>

void gone() {
  int a[2];
  a[3] = 'x'; // It writes past buffer, likely onto return address
}

int main() {
  gone();	    // likely never comes back
  printf("done\n");
}
```
The array `a` has two `int`s but you are overwriting it by 1 int.  That array is allocated on the stack instead of the heap (via `malloc()`) and so you are overwriting the stack activation record for `gone()`.  When `gone()` hits the return instruction, it will most likely not see a valid return address as `gone()` has stepped on it.  Here is what happens when I compile and run it on my 10.9.4 OS X box:

```bash
$ cc bufovf.c
bufovf.c:5:9: warning: array index 3 is past the end of the array (which
      contains 2 elements) [-Warray-bounds]
        a[3] = 'x'; // It writes past buffer, likely onto return address
        ^ ~
1 warning generated.
$ ./a.out
Abort trap: 6
$
```

Languages with automatic runtime array bounds checking such as Java make buffer overflow attacks impossible by their very nature, but languages like C are easy targets.  This should highlight an important fact about languages such as Java, C#, and Python.  When there is an error, you know it's an algorithmic problem.  You could not have corrupted the runtime system as you can in C/C++.

## Firewalls

A _firewall_ is a piece of hardware or software that blocks or restricts access to a port on a computer or set of computers.  For example, the SMTP port on our USF servers is not visible to machines outside the firewall.  Our firewall(s) filter out incoming `machine:port` requests that are dangerous.  You can even set a firewall to stop connections to the SMTP server when a virus attachment is suspected.  Another very common filter is for ssh connections.  For example, at jGuru, we only allowed ssh connections to our live servers from certain IP addresses (machines in our office).  The ssh port is open, but only to certain machines.  A random machine on the net could not get to the ssh port.

One can also use a firewall to filter outgoing requests.  For example, some companies stop all outgoing HTTP requests except from a single _proxy_ machine.  Browsers must pass all HTTP traffic through this proxy machine to get outside the wall.  In this manner, a company can track and/or stop its employees from accessing certain websites.

_How can peer-to-peer systems allow connections through firewalls?_ In other words, if Sriram works behind a firewall blocking instant messenger chat port _x_ at BEA and Terence works at a firewall at USF blocking port _x_, how can Sriram and Terence chat client-to-client instead of doing their jobs?  The only solution is for both of them to contact a central server outside the wall(s) and have traffic routed through that central server.  Even if Sriram's computer sends his IP address to the central server, there is no way Terence can open a connection to that IP address from outside Sriram's firewall.  Peer-to-peer systems such as this, that must operate in the presence of firewalls, are really client-server architectures.

# Java Sockets and Client/Server Programming

You can use Java to communicate with remote processes using a client/server model. A server listens for connection requests from clients across the network or even from the same machine. Clients know how to connect to the server via an IP address and port number. Upon connection, the server reads the request sent by the client and responds appropriately. In this way, applications can be broken down into specific tasks that are accomplished in separate locations.

The data that is sent back and forth over a socket can be anything you like in text or binary. Normally, the client sends a request for information or processing to the server, which performs a task or sends data back.

The IP and port number of the server are generally well-known and advertised so the client knows where to find the service. In contrast, the port number on the client side (the outgoing socket) is generally allocated automatically by the kernel.

Here is an example talking to the web server in CS department (port 80).  The protocol is `GET /` which directs the web server to get contents of the `index.html` file at the document root and send the text back to you.

```bash
$ telnet www.cs.usfca.edu 80
Trying 138.202.170.4...
Connected to nexus.cs.usfca.edu.
Escape character is '^]'.
GET /
<?xml version="1.0" encoding="utf-8"?>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" itemscope="" itemtype="http://schema.org/WebPage">
<head>
...
</html>
Connection closed by foreign host.
$ 
```

## Creating a server

Java makes socket programming extremely easy.  To create a server listening for requests, all you need to do is create a `ServerSocket` object attached to a port number and call method `accept()`.  For example, port 8080:

```java
ServerSocket sSocket = new ServerSocket(8080);
Socket channel = sSocket.accept();
```

Method `accept()` returns when a client has connected to your server.  The `channel` socket has a *different* port number than 8080.  The server socket is 8080 so to get more than one person talking to the server at once, the server needs to hand off socket connections to a different port.

You can get input/output streams from the `channel` socket to have a conversation with the client:

```java
OutputStream out = channel.getOutputStream();
PrintStream pout = new PrintStream(out);
InputStream in = channel.getInputStream();
DataInputStream din = new DataInputStream(in);
```

If you read from the input stream, you'll hear what the client has to say.  You can respond by sending data out the output stream.

```java
String line = din.readLine();
pout.println("You said: "+line);
```

Finally, don't forget to close your streams and socket:

```java
din.close();
pout.close();
channel.close();
```

## Creating a client

To talk to a server, open a socket to the machine and port:

```java
Socket s = new Socket("localhost", 8080); // port 8080
```

When this returns, you can get input/output streams:

```java
OutputStream out = s.getOutputStream();
PrintStream pout = new PrintStream(out);
pout.println("hi from java client");
pout.close();
s.close();
```

The client's input stream is pulling from the server's output stream and vice versa.

## An Example

You can think of client/server programming like a pizza-delivery place.  As an employee at the pizza place, you wait by the phone (you are the "server").  Upon receiving a call from a client, you send a "hello" message.  The client responds by sending you an order.  You acknowledge and write down the order (performing the server's task).  You or they hang up (connection closes).  Typically the server will spawn a thread to actually handle the request as it can be complicated, like making the pizza.  The server should go back to answering the phone rather than using a single-threaded model and making the pizza itself.  Note: the server blocks waiting on a request at the port rather than sitting in a spin loop, "picking up the phone" to see if anybody is there--it waits for a "telephone ring."

The following code embodies a simple, single-threaded version of the above scenario (it assumes ASCII text communication).

```java
import java.net.*;
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
      System.err.println("Can't open for business or problem serving!");
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
      finally { // ensure close happens
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
```

When we get to threads you will learn how to allow the pizza to be made while the phone is being answered.  It is like hiring more than one employee.
