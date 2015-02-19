# DNS -- Domain Name Service

## What is it?

A distributed database that maps domain names to IP addresses using a series of distributed DNS servers. Example query using UNIX tool:

```bash
$ nslookup www.usfca.edu
Server:		208.67.222.222
Address:	208.67.222.222#53

Non-authoritative answer:
Name:	www.usfca.edu
Address: 69.90.186.72
```

It is distributed so there isn't a single point of failure. A single server would also get absolutely pounded and would be extremely expensive to maintain.

We used to just use `/etc/hosts` file and regularly made copies from some main server, but this didn't scale very well obviously.

DNS servers: Root server and 13 root servers have globally well-known IP addresses. {*a-m*}.root-servers.net. We cache on local box, then multiple dns server, of the line all the way back to the authority.

*TLD* = top-level domain like .com, .net, .us, .cn

* second level: cnn.com, google.com
* machine level: www.cnn.com, foo.google.com

This provides a hierarchy with the TLDs at the root on down to the machine level. The leaves are the machine names.

Mappings:

* name to IP address
* multiple names to IP
* single name points to multiple IPs, if DNS servers decide to do that

There are different types that we will look at:

* `A` - name = IPv4 address
* `MX` - domain/name : responsible smtp server

Here is a zone file:
```
;
; db.linux.org.au - Example DNS data file for linux.org.au. domain
;

linux.org.au. IN SOA torvalds.linux.org.au. mike.cox.linux.org.au. (
        1995070201      ; Serial
        10800           ; Refresh = 3 hours
        3600            ; Retry = 1 hour
        604800          ; Expire = 1 week
        86400 )         ; Minumum TTL = 1 day

; Name servers
linux.org.au.                   IN NS           cox.linux.org.au.
linux.org.au.                   IN NS           torvalds.linux.org.au.

; Host addresses
becker.linux.org.au.            IN A            192.168.0.1
card.linux.org.au.              IN A            192.168.0.2
cox.linux.org.au.               IN A            192.168.0.3
eckhard.linux.org.au.           IN A            192.168.0.4
lord.linux.org.au.              IN A            192.168.0.5
torvalds.linux.org.au.          IN A            192.168.0.6
youngdale.linux.org.au.         IN A            192.168.0.7

; Host aliases
god.linux.org.au.               IN CNAME        torvalds.linux.org.au.
```
*Registrar*

*Authority*: can set TTL (time to live), propagation delay

DNS can be used for load balancing

## How do we look things up?
From Wikipedia:

<img src="http://upload.wikimedia.org/wikipedia/commons/7/77/An_example_of_theoretical_DNS_recursion.svg">

<img src="http://upload.wikimedia.org/wikipedia/commons/0/09/DNS_in_the_real_world.svg">

We ask the root server who handles the TLD for, say, `.org`. It responds with the org name server. That server is queried for the authority for  `foo.org`. Then we ask that authority for the IP address of foo.org.

## Reverse lookup

Given an IP address, what is the associated domain name?

We use `in-addr.arpa` domain preprended with reversed IP addres. E.g., to look up 192.0.2.5, use 5.2.0.192.in-addr.arpa, which points back to its designated host name.

DNS Cache poisoning. Spoofing with paypa1.com vs paypa1.com. Unicode issues: "o" in a foreign language looks like zero in english. micr0s0ft.com

DNS is a strategic national concern for all nations, which is why other countries want the root servers to become managed by the UN. Currently managed by the US Department of Commerce.

To register new domain:

pick second-level name within a top-level domain, foo.com
pay a fee, 6$ of which goes to ICANN; the rest is profit for the registrar
no alteration of the root server is necessary, only the .com TLD table needs to be updated to include foo.com, which points at the authority for foo.com
identifies the authority for the second-level name, usually the registrar such as Go Daddy or enom.com will manage this for you.
The authority is different than the actual IP target of your foo.com. The authority has the data that translates foo.com to an IP. Modifying the authority means modifying zone files. A and MX records indicate how to translate domain names to IP and also who the mail handler is for a domain. this is where you set the TTL
