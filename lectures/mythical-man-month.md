Mythical Man Month
====

Published 1975, Republished 1995; has been very widely read.   Frederick Brooks talking about his experience managing the development of OS/360 in the mid-60s. Brooks' project:

* was late
* was a memory hog
* cost many times more than estimate

# Software failure and scheduling

Software fails because:

1. Our techniques of estimating are poorly developed and reflect an unvoiced assumption that all will go well.
1. Our estimating techniques confuse *effort* with *progress*.
1. As our estimates are uncertain, no one has the courage to resist temptations to get things done in too short a time. It is better to wait for a good product and get a crappy one now. Schedule isn't monitored well. Urgency of boss forces programmers to agree to unrealistic schedules.  It is very hard to defend an estimate (good or bad); people use "hunches"
1. When the schedule slips, we add manpower.

Brooks says programmers are optimists (everything will go right etc...). **Incompleteness and inconsistencies become clear only during implementation.** He concludes that experimenting, "working out" are essential disciplines.

Brooks uses the following rules of thumb for scheduling:

* 1/3 planning
* 1/6 coding
* 1/4 component test an early system test
* 1/4 system test, all components in hand

No one single thing seems that difficult; "any particular paw can be pulled away." Simultaneous and interacting factors brings productivity to a halt.

A program is just a set of instructions that seems to do what you want. All programmers say "*Oh, I can easily beat the 10 lines / day cited by industrial programmers.*" They are talking about just coding something, not building a product.

A **product** (more useful than a program):

* can be run, tested, repaired by anyone
* usable in many environments on many sets of data.
* must be tested
* documentation

Brooks estimates a 3x cost increase for this.

*Plan to throw one away, you will anyway.* This book is ancient, but he says The only constancy is change itself and plan the system for change, which could come straight from the extreme programming books.


# Teams

The difference between a good programmer and bad programmer is at least:

* 10x in productivity
* 5x in program speed, space measurement

The 20k/yr programmer is more than 10x more productive than 10/yr programmer (1960's salaries...i hope) ;)

Data showed no correlation between experience and performance (but clearly there must be some).

"Small" team shouldn't exceed 10 programmers. (Sounds just like agile development now)

Brooks argues for an *aristocracy* because some people are just better than others.

Managers want small sharp team, but you can't build a very large system this way.

OS360 took about 5000 man years to complete. A team of 200 programmers would take 25 years (assuming simple linear partitionable tasks) Took only 4 years with 1000 people (quoting from book these numbers but don't seem to add up).

How to scale? Large system broken into subsystems assigned to surgical teams. Some coordination between surgical team leaders. Sounds just like agile; scrum and scrum of scrums.

# Conceptual integrity

The preservation of the conceptual integrity of the product.

* better to have one good idea than many bad or uncoordinated nonstandard ideas
* very important aspect of a large system.
* user has to know just that main concept; whole project makes sense. For example, in UNIX everything is a stream (files, devices, tty, ...)
* preserved by architect that designs system top-down.

In my experience, having a single mind behind ANTLR has made all tools, concepts hold together well. Most projects are "touched" by many grad students as they drift through a department and work on the tool for a prof.

Ratio of functionality / conceptual complexity is important. 

One or a few minds design, many implement.

Defending aristocracy he says: Even though implementators will have some good ideas, if they don't fit within the conceptual integrity, they are best left out.

# The Second System Effect

First system tends to be small and clean. Knows he/she doesn't know everything and goes slowly.

As the system is built, new features occur to them. They record these ideas for the "next system."

With the confidence of having built the previous system, the programmer builds the second system with **everything.** Tendency is to overdesign. Cites the IBM 709 architecture that is an update to 704. So big that only 50% of features are used.

Another version of the effect is to refine pieces of code or features from old system that just aren't that useful anymore.

I tend to consider the next system to be functionally exactly the same but with a much better implementation. A few new features are ok.

To avoid the second system effect, you can have concepts like feature x is worth *m* bytes and *n* ns of time.

Managers should hire chiefs that have at least two systems under their belt.

# Key takeaways

1. A program is not full product; keep that in mind when planning.
2. Adding programmers to fix a delay only makes it take longer.
3. Plan to throw one away, you will anyway.
4. Avoid the second system effect.