# EMail Internet Protocols

## Introduction

To send a piece of email, you need a mail client (even if it's telnet) that connects to an *SMTP* (Simple Mail Transfer Protocol by Jonathan B. Postel, 1982) and provides a packet of email with a target email address user@domain.com.

The SMTP server contacts the MX DNS record target for a domain at port 25. When sending mail, you also must contact an SMTP server such as our outgoing mail server smtp.usfca.edu:

```
~/tmp $ telnet smtp.usfca.edu 25
Trying 138.202.192.18...
Connected to smtp.usfca.edu.
Escape character is '^]'.
220 smtp.usfca.edu ESMTP Postfix
...
```

The server again uses the SMTP protocol to talk to the target server. The target server realizes that the user is allowed to receive mail for that domain on that machine, saving the mail in `/var/spool/mail/user` (on UNIX).

*POP*, Post Office Protocol, is another server that listens at port 110 on the target MX record box where mail is stored for a user. A POP client connects and asks for mail for that user.

```
$ telnet cs601.cs.usfca.edu 110
Trying 138.202.171.19...
Connected to cs601.cs.usfca.edu.
Escape character is '^]'.
+OK POP3 cs601.cs.usfca.edu v2001.78rh server ready
...
```

## How Mail is Routed

To figure out which machine receives mail for domain.com, you do an MX (mail exchange) record look up like this:

```
$ dig -t mx smtp.usfca.edu
dig -t mx usfca.edu
...
;; ANSWER SECTION:
usfca.edu.		2436	IN	MX	20 usfca.edu.s9a2.psmtp.com.
usfca.edu.		2436	IN	MX	30 usfca.edu.s9b1.psmtp.com.
...
```

## SMTP and sending mail
Reference: [SMTP](http://www.ietf.org/rfc/rfc0821.txt)

SMTP (Simple Mail Transfer Protocol) takes an envelope (header) and some data, an email message text, and routes it to a recipient's SMTP mail server for local delivery. SMTP is both a relay, such as `smtp.usfca.edu`, and a local delivery system.

The receiving SMTP server uses the envelope to decide who to deliver to locally (or relay to another server). The envelope is for routing and data is just appended (with route headers at top) to bottom of `/var/spool/mail/user` (on UNIX).

The protocol is just `HELO`, `MAIL FROM`, `RCPT TO`, `DATA`, then the email message text following by a `.` on a line by itself.

```
$ telnet smtp.usfca.edu 25
Trying 138.202.192.18...
Connected to smtp.usfca.edu.
Escape character is '^]'.
220 smtp.usfca.edu ESMTP Postfix
HELO cs.usfca.edu   
250 smtp.usfca.edu
MAIL FROM: <parrt@cs.usfca.edu>
250 Ok
RCPT TO: <support@antlr.org>
250 Ok
DATA
354 End data with <CR><LF>.<CR><LF>
This is a test
so nothing really
.
250 Ok: queued as 1A0C183F
QUIT
221 Bye
Connection closed by foreign host.
```

Something like this was stored on antlr machine machine:

```
From parrt@cs.usfca.edu  Mon Sep  8 13:38:16 2014
Return-Path: <parrt@cs.usfca.edu>
X-Original-To: support@antlr.org
Delivered-To: support@antlr.org
Received: by www.antlr.org (Postfix, from userid 8)
	id 11AC034184E1; Mon,  8 Sep 2014 13:38:16 -0700 (PDT)
X-Spam-Checker-Version: SpamAssassin 3.3.1 (2010-03-16) on www.antlr.org
X-Spam-Level: 
X-Spam-Status: No, score=-2.4 required=5.0 tests=BAYES_00,MISSING_SUBJECT,
	RCVD_IN_DNSWL_MED autolearn=ham version=3.3.1
Received: from na6sys009bog020.obsmtp.com (na6sys009bog020.obsmtp.com [74.125.150.80])
	by www.antlr.org (Postfix) with SMTP id 571783418401
	for <support@antlr.org>; Mon,  8 Sep 2014 13:38:09 -0700 (PDT)
Received: from smtp.usfca.edu ([138.202.192.18]) by na6sys009bob020.postini.com ([74.125.148.12]) with SMTP
	ID DSNKVA4TsLHDv64xsKZ/2MS3OjP94hhKdiiA@postini.com; Mon, 08 Sep 2014 13:38:09 PDT
Received: from cs.usfca.edu (maniac.cs.usfca.edu [138.202.170.154])
	by smtp.usfca.edu (Postfix) with SMTP id 1A0C183F
	for <support@antlr.org>; Mon,  8 Sep 2014 13:37:49 -0700 (PDT)
Message-Id: <20140908203801.1A0C183F@smtp.usfca.edu>
Date: Mon,  8 Sep 2014 13:37:49 -0700 (PDT)
From: parrt@cs.usfca.edu
To: undisclosed-recipients:;

This is a test
so nothing really
```

Note that the SMTP server adds some routing information. Normally, your mail client will add some header information like `From`, `To`, `CC`, and the following:

```
...
Content-Type: text/plain; charset=US-ASCII; format=flowed
Subject: test
...
```

All `To`, `CC`, `Bcc` addresses are sent to the server via `RCPT TO` as part of the envelope. The `Bcc` addresses are just not added as headers in the data part of the message like `To` and `CC` are.

It is pretty easy to forge From addresses as SMTP doesn't check anything.

## POP and retrieving mail

Reference: [POP](http://www.freesoft.org/CIE/RFC/1725/)

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

