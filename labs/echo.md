# Echo lab

*Pair up with a fellow student. One of you create the server program and one of you create the client program. Then switch places so that both students have created both a socket and a server.*

## Goal

Your goal in this lab is to create a simple echo client and server in Java using sockets without threads.  It will teach you the basics of sockets but also about protocols.

You will launch a server that listens for connections from clients and, upon connection, reads line by line from the client. Each line will be echoed back to the client. The client and server must interleave read and write of lines so that things are synchronized. The server speaks first, printing out through the socket to the client saying: `hello\n`. The client reads from the socket to get that line and prints it out. The client then reads a line from standard input from the user and sends that single line down to the server. Similarly, the server must read a line after it emits a line.  It will then echo the line back to the client. Sequence goes like this:

| Client | Server |
|--------|--------|
|        | open server socket, await connection       |
|  open connection       |        |
|        | send `hello\n` to client then wait on a read line from socket      |
| read line from server, print it (`hello\n`)  |        |
| read line from stdin, send to server        |   |
|  |read line, send back to client, print to stdout, wait on a read line|
| read line from server, print it | |
| read line from stdin, send to server        |   |
|  |read line, send back to client, print to stdout, wait on a read line|
| ... | ... |

If you get the sequence wrong, the programs will hang in a deadlock where one process (client or server) is waiting for the other.

First, start `Echo` server:

```bash
$ java Echo
client> who are you?
client> I can hear my voice!
...
```

Then, in another shell, run the client:

```bash
$ java Client
handshake> hello
who are you?
server> who are you?
I can hear my voice!
server> I can hear my voice!
...
```

Our [socket lecture](https://github.com/parrt/cs601/blob/master/lectures/sockets.md) is your reference material, but you can look up whatever you want on the net. Just be sure to get the program working yourself instead of copying and pasting from the web. The goal is to get simple socket programming into your head and writing the code yourself is a much stronger learning mechanism than reading socket code.

## Tasks

1.  Create a server program called `Echo` that has a `main` program that creates a `ServerSocket` listening at port 9000. This will be similar to [the server we discussed in class](https://github.com/parrt/cs601/blob/master/lectures/code/sockets/Server.java).
2.  Create a client program called `Client` that has a `main` program that opens a `Socket` to the server on your partner's computer (i.e., use their IP address, not `localhost`) at port 9000. This will be similar to [the client we discussed in class](https://github.com/parrt/cs601/blob/master/lectures/code/sockets/Client.java).

Each program will have a loop that reads input from the socket and writes output to the socket. The client will have an additional bit of code that reads from standard input to get user text.  Put `server> ` prefix on stuff you get from the server; the server should prefix lines it gets in the client with: `client> `.
