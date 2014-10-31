Project management introduction and grab bag of goodies
====

Let me start out by giving a nod to [Jeff Atwood](http://blog.codinghorror.com/) and Joel Spolsky for working tirelessly to help programmers get better and build better software. You'll note that many of the links on this page come from or are linked to from their blogs. Note: these are the guys that bring us [stack overflow](the Stack Exchange network of Q&A sites).

This page is an attempt to grab a few important links for my students in a semi-organized but nonexhaustive way.

For a bit of advice, you can also check out my [Little Nybbles of Development Wisdom](http://parrt.cs.usfca.edu/doc/devnybbles.html).

# Ooops

A modern example, [Software glitches leave Navy Smart Ship dead in the water](http://gcn.com/Articles/1998/07/13/Software-glitches-leave-Navy-Smart-Ship-dead-in-the-water.aspx). *The ship had to be towed into the Naval base at Norfolk, VA, because a database overflow caused its propulsion system to fail* [snicker].<br>
<img src="http://upload.wikimedia.org/wikipedia/commons/a/a1/USS_Yorktown_%28CG-48%29%3B04014806.jpg" width=220>.

And [How an ancient shipbuilding project failed](http://vimeo.com/95284690).<br>
<img src="figures/dasboot.png" width=220>

For more examples of roadkill, see [The Long, Dismal History of Software Project Failure](http://blog.codinghorror.com/the-long-dismal-history-of-software-project-failure/)

## High-level risk factors

 Steve McConnell, author of the famous [Code Complete](http://www.amazon.com/Code-Complete-Practical-Handbook-Construction/dp/0735619670/ref=la_B000APETRK_1_1?s=books&ie=UTF8&qid=1414613811&sr=1-1), wrote in [Software Estimation: Demystifying the Black Art](http://www.amazon.com/exec/obidos/ASIN/0735605351/codihorr-20):

<blockquote>
size is easily the most significant determinant of effort, cost, and schedule. The kind of software you're developing comes in second, and personnel factors are a close third. The programming language and environment you use are not first-tier influences on project outcome, but they are a first-tier influence on the estimate.
</blockquote>

So, in order of importance to project "difficulty" or probability of failure:

1. **size** (smaller is better obviously)
2. **type of software** (xray machine? space probe? crappy website you and your little buddies make?)
3. **personnel factors** (can't hire the right developers? Half the developers won't talk to the other half? many are working remotely?)

## Starting off on the wrong foot

Of course, we can easily start off on the wrong foot and dramatically increase chances of failure:

An article by Zef Hemel, [Pick Your Battles](http://zef.me/4235/pick-your-battles/), on how picking bleeding edge software can really make you bleed. There really is something to be said for proven technology even if it's not cool. The funny thing is that it's hard to hire developers if you're not doing the latest cool thing. I know one company that is starting to use Scalar so they attract developers but still use proven Java technology.

This highlights why I have this critical rule from my [Little Nybbles of Development Wisdom](http://parrt.cs.usfca.edu/doc/devnybbles.html):
<blockquote>
Do not rely on anybody else's software for your core application unless you really trust and have tested the library or service. If you have to use other software for a critical component, make sure you get the source.
</blockquote>

In that same article, I point out:

<blockquote>
Programmers are curious beasts, which is normally a good thing. However, watch out that they don't find new technology X and demand to use it because "it's so cool." At the same time, don't let management force X on you to make your software buzzword compliant. [give epicentric story.]
</blockquote>

Zef's advice: *go and build amazing applications. Build them with the most boring technology you can find. The stuff that has been in use for years and years.*

# Why it's hard to to manage software projects

[Why are software development estimates regularly off by a factor of 2-3 times?](http://www.michaelrwolfe.com/2013/10/19/50/)

[Why do dynamic languages make it more difficult to maintain/develop large code bases?](http://programmers.stackexchange.com/questions/221615/why-do-dynamic-languages-make-it-more-difficult-to-maintain-large-codebases/221658#221658)

[Why writing software is not like engineering](http://parrt.cs.usfca.edu/doc/software-not-engineering.html). And a similar article [You're not an engineer](http://nic.ferrier.me.uk/blog/2013_04/you-are-not-an-engineer).

# Software development ideas

[We've tried to fix it](http://en.wikipedia.org/wiki/List_of_software_development_philosophies), which obviously didn't and doesn't work or we wouldn't be having this conversation.

Some [slides on lifecycle management](http://courses.cs.washington.edu/courses/cse403/14sp/lectures/lecture02-lifecycle.pdf) from University of Washington CSE403.

Some extremely experienced developers conclude the opposite of such "control freakism". For example, [Real Ultimate Programming Power](http://blog.codinghorror.com/real-ultimate-programming-power/):
<blockquote>
**In the absence of mentoring and apprenticeship**, the dissemination of better programming practices is often conveniently packaged into processes and methodologies.
</blockquote>

He lists some of the key principles without going to some full-blown development religion...er...philosophy:

1. [DRY](http://en.wikipedia.org/wiki/Don't_repeat_yourself)  Don't repeat yourself.
1. [KISS](http://en.wikipedia.org/wiki/KISS_principle) Keep it simple, stupid.
1. [YAGNI](http://en.wikipedia.org/wiki/You_aren't_gonna_need_it) You aren't gonna need it.

Another way to look at that is:

1. Avoid code duplication
1. Use the simplest possible thing that would work
1. Don't design, implement, or include anything you don't need right now

I'd also add: **Use as few machines and system components as possible.**

But that's not enough structure for most developers; why? Let's begin by looking at what kind of developers there are.

# Categorizing developers

## Developer skill spectrum

Developers go through some fairly well-defined "development" as they acquire skill and experience. Slides taken from [Developing Expertise: Herding Racehorses, Racing Sheep](http://www.infoq.com/presentations/Developing-Expertise-Dave-Thomas):

**Level 1: Beginner**

* Little or no previous experience
* Doesn't want to learn: wants to accomplish a goal
* No discretionary judgement
* Rigid adherence to rules

**Level 2: Advanced Beginner**

* Starts trying tasks on their own
* Has difficulty troubleshooting
* Wants information fast
* Can place some advice in context required
* Uses guidelines, but without holisitic understanding

**Level 3: Competent**

* Develops conceptual models
* Troubleshoots on their own
* Seeks out expert advice
* Sees actions at least partially in terms of long-term plans and goals

**Level 4: Proficient**

* Guided by maxims applied to the current situation
* Sees situations holistically
* Will self-correct based on previous performance
* Learns from the experience of others
* Frustrated by oversimplified information

**Level 5: Expert**

* No longer relies on rules, guidelines, or maxims
* Works primarily from intuition
* Analytic approaches only used in novel situations or when problems occur
* When forced to follow set rules, performance is degraded

Ok, so what is the relationship between methodologies and skill level? [Level 5 means never having to say you're sorry](http://blog.codinghorror.com/level-5-means-never-having-to-say-youre-sorry/):

<blockquote>
I am not trying to encourage the level 5 means never having to say you're sorry attitude. "No rules" isn't shorthand for an air of smug superiority, although there's certainly no shortage of that in our profession. "No rules" means that we should actively seek out challenging development opportunities with lots of unknowns, work that takes considerable experience and skill. **The kind of work that cannot be done by beginners slavishly following a big-Em methodology**.
</blockquote>

From Joel Spolsky, [Why is it that some of the biggest IT consulting companies in the world do the worst work?](http://www.joelonsoftware.com/articles/fog0000000024.html):
<blockquote>
Beware of Methodologies. They are a great way to bring everyone up to a dismal, but passable, level of performance, but at the same time, they are aggravating to more talented people who chafe at the restrictions that are placed on them. 
</blockquote>

Joel says that the higher you go up the levels and developerland, the lower the value of methodologies/rigid-rules.

How not to be outsourced/downsized?  Don't be in a job that is governed by rules, because those are the easiest kinds of jobs to give to the lowest, i.e. cheapest, programmers.

## Us versus them

Or, are there only [Two Types of Programmers](http://blog.red-bean.com/sussman/?p=79)?

<blockquote>
The 20% folks are what many would call “alpha” programmers — the leaders, trailblazers, trendsetters, the kind of folks that places like Google and Fog Creek software are obsessed with hiring. These folks were the first ones to install Linux at home in the 90′s; the people who write lisp compilers and learn Haskell on weekends “just for fun”; they actively participate in open source projects; they’re always aware of the latest, coolest new trends in programming and tools.
...
The 80% folks make up the bulk of the software development industry. They’re not stupid; they’re merely vocational. They went to school, learned just enough Java/C#/C++, then got a job writing internal apps for banks, governments, travel firms, law firms, etc. The world usually never sees their software. They use whatever tools Microsoft hands down to them.
...
Shocking statement #1: Most of the software industry is made up of 80% programmers. Yes, most of the world is small Windows development shops, or small firms hiring internal programmers.
</blockquote>

In [Mort, Elvis, Einstein, and You](http://blog.codinghorror.com/mort-elvis-einstein-and-you/), Jeff Atwood was responding to negative feedback he got when he referenced the "two types of programmers" article: "*the very act of commenting on an article about software development automatically means you're not a vocational eighty-percenter*" and perhaps most importantly:

"***The other eighty percent are not actively thinking about the craft of software development.***"

Many programmers I see are obsessed with learning "best practices", memorizing the gang of four patterns book, closely following development strategies. *All this comes down to reading a bunch of bullshit instead of doing your job.* It reminds me of a friend that I had in college. He would spend all of his time getting pencils and paper and other things together in order so that he could start the homework, but he never started the homework.

If you're not thinking about the process by which you develop software, the subject of this course, your part of the 80% not 20%. Don't just write software. Think about how you write software or even write software to help you write software.

# Trying to control software development

[Parrt's control theory](http://parrt.cs.usfca.edu/doc/devnybbles.html): *You cannot control anybody or anything. You can only nudge or influence.*

We are all panicked about risk and failing when developing large pieces of software, with good reason. In order to control the process, we come up with all kinds of measurements. Then we try to go the other way and control the process by coming up with rules or formulas based upon these measurements that must be satisfied. Hahahaha.

Remember that just because a metric is good, "improving" it too far is a bad thing. Your resting heartrate is considered a good proxy for overall health. Improving it down to zero is not recommended.

Also remember: **Precision does not equal accuracy!**

Tom DeMarco in [Software Engineering: An Idea Whose Time Has Come and Gone?](http://www2.computer.org/cms/Computer.org/ComputingNow/homepage/2009/0709/rW_SO_Viewpoints.pdf) makes the analogy between controlling software development and controlling a teenager. Just because you can measure their height, grades, how many friends they have and so on doesn't mean that you can control them. Coming up with a rule that says: my child will have 10 good friends and get good grades is meaningless towards controlling that child.

## Rules? We don't need no stinkin' rules!

In [Software Engineering: Dead?](http://blog.codinghorror.com/software-engineering-dead/), Jeff Atwood says "*control is ultimately illusory on software development projects.*"

[James Bach on rules](http://www.satisfice.com/blog/archives/174):
<blockquote>
My style of development and testing is highly agile. I am agile in that I am prepared to question and rethink anything. I change and develop my methods. I may learn from packaged ideas like Extreme Programming, but I never follow them. Following is for novices who are under active supervision. Instead, I craft methods on a project by project basis, and I encourage other people to do that, as well. I take responsibility for my choices. That’s engineering for adults like us.
</blockquote>

[The Ferengi programmer](http://blog.codinghorror.com/the-ferengi-programmer/):
<blockquote>
Guidelines, particularly in the absence of experts and mentors, are useful. But there's also a very real danger of hewing too slavishly to rulesets. Programmers are already quite systematic by disposition, so the idea that you can come up with a detailed enough set of rules, and sub-rules, and sub-sub-rules, that you can literally program yourself for success with a "system" of sufficient sophistication -- this, unfortunately, comes naturally to most software developers. If you're not careful, you might even slip and fall into a Methodology. Then you're in real trouble.
</blockquote>

and

<blockquote>
Rules, guidelines, and principles are gems of distilled experience that should be studied and respected. But they're never a substute for thinking critically about your work.
</blockquote>

# Software development artifacts / tools

Bill de hÓra' [3 pillars](http://www.dehora.net/journal/2007/01/3_pillars.html):

<blockquote>
...the **version control system** is a first order effect on software, along with two others--the **build system** and the **bugtracker**. Those choices impact absolutely everything else. Things like IDEs, by comparison, don't matter at all. Even choice of methodology might matter less. Although I'm betting there are plenty of software and management teams out there that see version control, build systems and bugtrackers as being incidental to the work, not mission critical tools.
</blockquote>

## My thoughts on UML

I certainly create lots of diagrams and write copious notes when designing software, but they are super informal. Why? Well, what is the difference between writing something out by hand and using a word processor? Speed vs precision. Precision at the UML diagram level provides a false sense of control over reality. **Precision does not imply accuracy.** It usually just means you are more precisely wrong. Ha! Besides we already have precision: it's called code.

Why formalize? I often use a graphics tool, but why bother with some ugly diagrams specified by committee? Just draw something that gets the idea across.

Large programming/IT shops for low-tech companies like phone companies and accounting firms often try to manage large software development by having so-called analysts design an application with UML down the object level and then have programmers simply translate the design to code and implement it like machines. This doesn't work for two reasons:

1. there is no job satisfaction being the programmer, hence, you will not be able to hire good programmers
1. when you actually try to code something, you often have to make radical changes in the design. Usually these programmers are largely ignored by the upper echelon of the hiearchy.

UML can create some pretty pictures and humans like to make these diagrams, but they are super fragile. Changes at the method and field variable name occur constantly forcing constant changes in the UML diagram. You've just created another full time job; that costs money and adds a serious parallel-update dependency.

When you have a huge UML diagram, people will often post this on a large wall so you can see how everything fits together and so on. Turns out that finding your classes and their relationships without a computer is pretty tough as you need to put fingers down at lots of different locations to "hold your place" kind of like a twister game.

One of the reasons I get away with not using lots of formal diagrams is that I go to great lengths to write code that is very easy to understand from the overall level and the very specific.

In summary, diagrams should be as high level as possible given their intended use. Precision should be relegated to the code level. This isolates the design more from implementation detail changes. A semi-formal diagram is often useful, however, for communication with fellow programmers or dumbing things down for management. You can get totally caught up building pretty pictures rather than actually building something.

## Some useful diagrams

### Class Hierarchy

Draw Employee, Coder, Manager, CEO

### Package Grouping

StoreItems: {Book, Map, Paper, Pen, Stapler}

### Object Diagram

Car:Honda or Professor:Terence

### Association

Book &rarrow;->\*Chapter or Person->+address or Person->Employer

### Data flow (use case?)

DBManager -Books,Pens,...-> Store --> WebServer -| |->clientbrowser

### State diagrams

Really great example is a state diagram for HTML web pages:

```
     |------| next page
     v      |
[FAQ List] -|
     |      |
     | edit |
     v      |cancel
[edit faq] -|
     |      |
     |verify|
[verify faq]|
     |      |
     |------|submit
```

### Data structure diagrams

Similar to assocation diagrams, but specifically designed to show a formal data structure like List of List or Hashtable of Hashtable (useful for example, to do insertion sort; can alter a value and place in new location in constant time; kinda like radix sort).

```
a=2, b=0, c=1, d=1

|3|
|2| -> [a]
|1| -> [c,d]
|0| -> [b]
```

It helps to design algorithms if you can see the structure; you can say "ok, I'm here and I decide to walk here based on this" etc...

People doing research on software engineering seem to ignore the importance of specific algorithm design.  jGuru has some fairly hairy algorithms for doing document analysis etc... that had to be designed with data structure diagrams.  Tied into overall diagram then with words like "compute histogram", "Lexicon", etc...