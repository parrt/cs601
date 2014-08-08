# EMail Internet Protocols

## Introduction

To send a piece of email, you need a mail client (even if it's telnet) that connects to an SMTP (Simple Mail Transfer Protocol by Jonathan B. Postel, 1982) and provides a packet of email with a target email address user@domain.com.

The SMTP server contacts the MX DNS record target for a domain at port 25. When sending mail, you also must contact an SMTP server such as our outgoing mail server nexus.cs.usfca.edu:

```
~/tmp $ telnet nexus 25
Trying 138.202.171.19...
Connected to cs601.cs.usfca.edu.
Escape character is '^]'.
220 cs601.cs.usfca.edu ESMTP Sendmail 8.12.8/8.12.8; Thu, 6 Nov 2003
13:29:16 -0800
...
```

The nexus server again uses the SMTP protocol to talk to the target server. The target server realizes that the user is allowed to receive mail for that domain on that machine, saving the mail in /var/spool/mail/user.

POP, Post Office Protocol, is another server that listens at port 110 on the target MX record box where mail is stored for a user. A POP client connects and asks for mail for that user.

```
$ telnet cs601 110
Trying 138.202.171.19...
Connected to cs601.cs.usfca.edu.
Escape character is '^]'.
+OK POP3 cs601.cs.usfca.edu v2001.78rh server ready
...
```

## How Mail is Routed

To figure out which machine receives mail for domain.com, you do an MX (mail exchange) record lookahead like this:

```
$ dig -t mx cs.usfca.edu
...
cs.usfca.edu.           1D IN MX        10 nexus.cs.usfca.edu.
...
```

We have a special server called cs601.cs.usfca.edu to use for our class. Each person will get a userN@cs601 account for use in testing their webmail server; e.g., user01@cs601, user02@cs601, ...

The MX record for that machine is specifically that machine:

```
cs601.cs.usfca.edu.     1D IN MX        20 cs601.cs.usfca.edu.
```

implying that it handles the mail rather than nexus or stargate.

## SMTP and sending mail
Reference: SMTP

SMTP (Simple Mail Transfer Protocol) takes an envelope (header) and some data, an email message text, and routes it to a recipient's SMTP mail server for local delivery. SMTP is both a relay, such as smtp.cs.usfca.edu, and a local delivery system.

The receiving SMTP server uses the envelope to decide who to deliver to locally (or relay to another server). The envelope is for routing and data is just appended (with route headers at top) to bottom of /var/spool/mail/user.

The protocol is just HELO, MAIL FROM, RCPT TO, DATA, then the email message text following by a "." on a line by itself.

```
HELO cs.usfca.edu
MAIL FROM: <parrt@jguru.com>
250 2.1.0 <parrt@jguru.com>... Sender ok
RCPT TO: <user08@cs601.cs.usfca.edu>
250 2.1.5 <user08@cs601.cs.usfca.edu>... Recipient ok
DATA
354 Enter mail, end with "." on a line by itself
hi
.
250 2.0.0 hA6MGdgd031032 Message accepted for delivery
QUIT
```

Here is what was stored on cs601 machine:

```
Return-Path: <parrt@jguru.com>
Received: from cs601.cs.usfca.edu (parrt.cs.usfca.edu
[138.202.170.157])
        by cs601.cs.usfca.edu (8.12.8/8.12.8) with SMTP id
	hA6MGdgd031032
        for <user08@cs601.cs.usfca.edu>; Thu, 6 Nov 2003 14:20:04
	-0800
Date: Thu, 6 Nov 2003 14:20:04 -0800
From: parrt@jguru.com
Message-Id: <200311062220.hA6MGdgd031032@cs601.cs.usfca.edu>
Status:   

hi
```

Note that the SMTP server adds some routing information. Normally, your mail client will add some header information like From, To, CC, and the following:

```
...
Content-Type: text/plain; charset=US-ASCII; format=flowed
Subject: test
...
```

Using the protocol:

```
$ telnet cs601 25
Trying 138.202.171.19...
Connected to cs601.cs.usfca.edu.
Escape character is '^]'.
220 cs601.cs.usfca.edu ESMTP Sendmail 8.12.8/8.12.8; Thu, 6 Nov 2003
14:26:49 -0800
HELO cs.usfca.edu
250 mail.cs.usfca.edu
MAIL FROM: <parrt@jguru.com>
250 2.1.0 <parrt@jguru.com>... Sender ok
RCPT TO: <user08@cs601.cs.usfca.edu>
250 2.1.5 <user08@cs601.cs.usfca.edu>... Recipient ok
DATA
354 Enter mail, end with "." on a line by itself
Subject: foo
hi again
.
250 2.0.0 hA6MQngc031036 Message accepted for delivery
QUIT
221 2.0.0 cs601.cs.usfca.edu closing connection
```

At the POP server, you'll see:

```
Return-Path: <parrt@jguru.com>
Received: from parrt.cs.usfca.edu (parrt.cs.usfca.edu
[138.202.170.157])
        by cs601.cs.usfca.edu (8.12.8/8.12.8) with SMTP id
	hA6MQngc031036
        for <user08@cs601.cs.usfca.edu>; Thu, 6 Nov 2003 14:27:08
	-0800
Date: Thu, 6 Nov 2003 14:26:49 -0800
From: parrt@jguru.com
Message-Id: <200311062227.hA6MQngc031036@cs601.cs.usfca.edu>
X-Authentication-Warning: cs601.cs.usfca.edu: parrt.cs.usfca.edu
[138.202.170.157] didn't use HELO protocol
Subject: foo
Status:   

hi again
```

All To, CC, Bcc addresses are sent to the server via RCPT as part of the envelope. The Bcc addresses are just not added as headers in the data part of the message like To and CC are.

It is pretty easy to forge From addresses as SMTP doesn't check anything.

## POP and retrieving mail
Reference: POP

POP is pretty simple. You connect to the POP server, authenticate with a user and password (in the clear) and then ask for the messages. You have the option of deleting or removing the messages.

Here is a sample session to figure out how many messages user08's account has:

```
$ telnet cs601 110
Trying 138.202.171.19...
Connected to cs601.cs.usfca.edu.
Escape character is '^]'.
+OK POP3 cs601.cs.usfca.edu v2001.78rh server ready
USER user08
+OK User name accepted, password please
PASS user08
+OK Mailbox open, 1 messages
STAT
+OK 1 1125
QUIT
+OK Sayonara
Connection closed by foreign host.
```

To get message n, use RETR n. You will see the server respond with an acknowledgement and the message followed by "." on a line by itself:

```
+OK 1125 octets
Return-Path: <parrt@cs.usfca.edu>
Received: from tom.knowspam.net (tom.knowspam.net [64.49.216.142])
        by cs601.cs.usfca.edu (8.12.8/8.12.8) with ESMTP id
	hA6Jwggc031008
        for <user08@cs601.cs.usfca.edu>; Thu, 6 Nov 2003 11:58:42
	-0800
Received: from tom.knowspam.net (localhost [127.0.0.1])
        by tom.knowspam.net (Postfix) with SMTP id 02F59266639
        for <user08@cs601.cs.usfca.edu>; Thu,  6 Nov 2003 11:48:20
	-0800 (PST)
X-KS: 0e0f090601020f000f00090a0a050907
Date: Thu, 6 Nov 2003 11:59:11 -0800
Mime-Version: 1.0 (Apple Message framework v552)
Content-Type: text/plain; charset=US-ASCII; format=flowed
Subject: test
From: Terence Parr <parrt@cs.usfca.edu>
To: user08@cs601.cs.usfca.edu
Content-Transfer-Encoding: 7bit
Message-Id: <B0896A78-1093-11D8-AE6D-000A95891192@cs.usfca.edu>
X-Mailer: Apple Mail (2.552)
Status: RO

Testing...
Terence
--
Professor Comp. Sci., University of San Francisco
Creator, ANTLR Parser Generator, http://www.antlr.org
Co-founder, http://www.jguru.com
Co-founder, http://www.knowspam.net enjoy email again!
Co-founder, http://www.peerscope.com pure link sharing


.
```

You may ask for a list of message sizes:

```
LIST
+OK Mailbox scan listing follows
1 1125
2 1113
.
```

To delete a message, use DELE:

```
DELE 1   
+OK Message deleted
LIST
+OK Mailbox scan listing follows
2 1113
.
STAT
+OK 1 1113
```

Note that LIST shows only the 2nd message remaining.

To undo all the deletes, use RSET.

Most POP servers also implement the TOP command that gets the top n lines from a message plus the entire header:

```
TOP 1 1
+OK Top of message follows
Return-Path: <parrt@cs.usfca.edu>
Received: from tom.knowspam.net (tom.knowspam.net [64.49.216.142])
        by cs601.cs.usfca.edu (8.12.8/8.12.8) with ESMTP id
	hA6Jwggc031008
        for <user08@cs601.cs.usfca.edu>; Thu, 6 Nov 2003 11:58:42
	-0800
Received: from tom.knowspam.net (localhost [127.0.0.1])
        by tom.knowspam.net (Postfix) with SMTP id 02F59266639
        for <user08@cs601.cs.usfca.edu>; Thu,  6 Nov 2003 11:48:20
	-0800 (PST)
X-KS: 0e0f090601020f000f00090a0a050907
Date: Thu, 6 Nov 2003 11:59:11 -0800
Mime-Version: 1.0 (Apple Message framework v552)
Content-Type: text/plain; charset=US-ASCII; format=flowed
Subject: test
From: Terence Parr <parrt@cs.usfca.edu>
To: user08@cs601.cs.usfca.edu
Content-Transfer-Encoding: 7bit
Message-Id: <B0896A78-1093-11D8-AE6D-000A95891192@cs.usfca.edu>
X-Mailer: Apple Mail (2.552)
Status: RO

Testing...
.
```

To get a unique ID (UIDL) for each message do this:

```
UIDL
+OK Unique-ID listing follows
1 3faaa7fc00000001
2 3faaa7fc00000002
.
```

