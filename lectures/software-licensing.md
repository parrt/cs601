Software licensing
=====

As a developer, you are responsible for never claiming somebody else's work as your own. This is true in school and at work. Naturally, we want to reuse as much code as possible but we must do so legally and ethically. The safest thing to do is to reuse the binary of a library as you are not creating a derivative work.

We all cut-and-paste little snippets of code from the web to get our job done and that is generally okay as long as it's not whole methods and so on. If that's the case, we must check the license and/or copyright notices to see if it's legal to do so. If the license seems to indicate we can use it, we still have to attribute it to the original author or authors. Usually the license makes this clear. A number of licenses are included below.

Protection from theft is critical for innovation as developers know that the rule of law prevents people from stealing their hard work. This is true for both copyright and patents and trademarks.

# Software copyright

By default, [American copyright law](http://en.wikipedia.org/wiki/Software_copyright) gives you copyright over your creations. This covers a tangible fixed piece of source code.  The law prevents someone from claiming your source code as their own, just as it does for a novel or textbook. You have copyright over your creation without having to explicitly state that is the case: [*Copyright exists from the moment the work is created*](http://copyright.gov/help/faq/faq-general.html#register).

Note that copyright is not the same thing as a patent or trademark. A copyright is like a book: a tangible fixed object. A patent on the other hand documents inventions or discoveries. In exchange for explaining everything about the invention, the inventor gets exclusive rights to use the invention for certain period of time.

# Trademarks

A trademark is a way to register your brand so that you can protect it. Coca-Cola and Google viciously protect their brand because they spend a lot of time and money creating a good brand. 

From [copyright.gov](http://copyright.gov/help/faq/faq-general.html#mywork): "*A trademark protects words, phrases, symbols, or designs identifying the source of the goods or services of one party and distinguishing them from those of others.*"

Again, protection under the law is required for investment.

# Software licenses

You should become familiar with software licensing and can look at the most common ones at the [open source initiative](http://opensource.org/licenses).

[BSD license](http://opensource.org/licenses/BSD-3-Clause). This is the license that I prefer as it allows everyone to use my software really without restriction commercially or otherwise. It only guarantees that I get credit for the work. Here are the rules you must follow if you use my software:

<blockquote>
Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following conditions are met:

1. Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer.

2. Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following disclaimer in the documentation and/or other materials provided with the distribution.

3. Neither the name of the copyright holder nor the names of its contributors may be used to endorse or promote products derived from this software without specific prior written permission.
</blockquote>

Notice that there are not restrictions for commercial use. If you redistribute the software or the binary, you have to provide my copyright notice.

BSD is a tiny license.

I noticed there is a two clause version now that does not have the (3) clause. Hmm... I might go without one next.

[MIT License](http://opensource.org/licenses/MIT). This is basically the same as the BSD license. Tiny license.

[Apache 2.0](http://opensource.org/licenses/Apache-2.0). Very much in the spirit of MIT/BSD but contains a [patent grant](http://www.apache.org/foundation/license-faq.html#PatentScope) to users of the software. That means that if I use that free software that happens to be covered by patents, I get a license to use that software without paying patent license fees.

This license is about five or six times the size of BSD.

[GNU public license (GPL) 3.0](http://opensource.org/licenses/GPL-3.0). I hate this license because commercial entities can't really use it if they plan on altering the software. If they do so, they have to provide their changes to the outside world. Yes, this is fair but simply means that companies won't use the software.  Just using the binary is okay for most companies though as it doesn't require releasing some of their proprietary software.

I recently had a case where a company derived a grammar from my grammar but did not want to release their fixes. Annoying, but the small price to pay for the common good.

My biggest problem with GPL is that it's basically a nasty virus. Once infected with the GPL the virus, your entire project is GPL.

[GNU Less public license (LGPL) 3.0](http://opensource.org/licenses/LGPL-3.0). This is a less restrictive version of GPL. The loosening seems to be that your entire project does not get infected with LGPL as it does with GPL. Only those portions that you modify are covered under LGPL. See [more on GPL vs LGPL](http://www.wikivs.com/wiki/GPL_vs_LGPL). GNU folks say "*using the Lesser GPL permits use of the library in proprietary programs; using the ordinary GPL for a library makes it available only for free programs*".

# Email from Eclipse concerning ANTLR/StringTemplate licensing

Over the years I've received lots and lots of email from various companies and even government organizations asking about the provenance of my software. They effectively want to know where every single line came from so that they are not infected. 20 years ago this was not an issue and I used to give away my stuff public domain. After a number of high-profile lawsuits from assholes, we software developers have to be much more careful. Hence, I provide a few of the sample questions I received from intellectual property attorneys at the Eclipse foundation to give you a flavor of how much they care about this.

From: VP of Marketing and Ecosystem at Eclipse foundation
Date: Tue, 20 Aug 2013 15:16:58 -0400
<blockquote>
Please pardon the intrusion.  We are currently reviewing Antlr 4.1 Runtime, and I came across the GraphicsSupport.java file (runtime/misc directory) that contains copyright to Cay Horstmann further down the file. 
 
For some reason, this issue has nagged at me.  As such, I thought I would quickly check in with you to confirm that in fact Cay did contribute this file to Antrl rather than it being used from a third party perspective.   I know how much attention you give to Antlr's CCO but I also do not see Cay's info listed under the CONTRIBUTOR'S list.
</blockquote>

From: Intellectual Property at Eclipse foundation
Date: Mon, 8 Jul 2013 11:19:31 -0400
<blockquote>
I work with Janet Campbell here at the Eclipse Foundation.  Hope things are going well for you.  I know you have helped us out in the past concerning Antlr Runtime.  We have just received a request to distribute StringTemplate version 3.2.1.  I know StringTemplate is under the BSD License.  I’d like your help to understand if contributions to StringTemplate are also covered by the Antlr Project Developer's Certificate of Origin [1]?
</blockquote>

From: VP of Marketing and Ecosystem at Eclipse foundation
Date: Tue, 30 Aug 2011 08:40:28 -0700
<blockquote>
I am not sure if you are aware that the Eclipse Foundation has an IP review process for any code that comes into an Eclipse project, including third party projects.   Antlr has successfully gone through that process since it is being used by a number of Eclipse projects.
</blockquote>

From: Intellectual property at Eclipse foundation
Date: Feb 26, 2010, at 1:08 PM
<blockquote>
I would usually begin an email with a little bit of background about
Eclipse, but I think you may already know about us so I won't bore you ;-).
You really helped us out a few years ago when Janet Campbell, our Director
of IP, was in touch with you:  you rewrote ANTLR runtime under the BSD. 

It is my understanding that at that time, you were in the process of
rewriting code for generator as well.  Just wondering; is generator in v3.2
a complete rewrite now?   If so, can you also confirm whether all
contributions since the rewrite have been accepted solely via the (very
simple and effective!) URL you've set up? 
</blockquote>

# Evaluating software for inclusion in company projects

The intellectual-property folks at the Eclipse foundation told me that they evaluate all code before allowing its inclusion in their project. They look in the documentation and websites for contributors and then they look at the code to see if there are any copyright holders. They tried to clarify ownership and so on with the primary author(s). They may approach the other copyright holders to confirm the answers to questions:

Identify all committer & contributors name and contact information.  For each individual, they seek the following information (replacing X with the project's name, and Y with the license used by the project):

1. Did you agree to contribute the code to the X project,
    a. to be licensed as open source under the Y agreement?; and now
	b. to be licensed as open source under the Y agreement?

1. Did you [yourself] write the code you contributed to the X project?

1. Does anyone else have rights to the code you contributed? [For example,
did you have an agreement with an employer giving the employer rights to all
code you wrote during that time?]

1. Can you estimate how much code you contributed to the X project?

1. *Optional* Do you know of any contributions to the X projects which were improperly copied from someone else?

1. *Optional* Do you know of any code in X to which a third party has rights inconsistent with the Y license?"

Based on the responses, Eclipse makes a decision about the risk level associated with including that software.

# ANTLR Project Contributors Certification of Origin and Rights

As a result of the issues I've had previously, I created the following contributors agreement and have all contributors to my software sign it with a github commit:

https://github.com/antlr/antlr4/blob/master/contributors.txt

This makes the ANTLR software squeaky clean from a licensing point of view. Every contribution is attributed to someone who agrees, in essence: *They are the contributor of the software and have the right to make it publicly available. If they do something naughty and inject tainted code, liability lies with that submitter.*