Agile Software Development
====

_Updated Dec 3, 2007_

Writing software is a big fat mess and is usually tangled up with bureaucracy and Human collaboration issues.  As development progresses, software typically becomes more and more fragile and more and more buggy.  As we've discussed, it's rare that a system is delivered that actual does what the customer wants (let alone on time).

To fix the problem, people have tried to apply engineering principles to the problem.  That "analyze, design, implement, test" sequence is what I was taught as an undergraduate in the early 80's.  That _waterfall method_ produces software that is inflexible, late, and most importantly doesn't do what the user wants.  Apparently it's not the solution.

The problem is that users don't know what they want until you start showing them something.  Quoting Tom Burns:

<blockquote>
You know the least about a problem at the start of a project--that is why it doesn't make any sense to do your 'final' design at the beginning.  You need to familiarize yourself with the problem space by making something useful--by writing code.  Letting people use it and give feedback.  Then you should upgrade your design (refactor) as you discover what is truly important.
</blockquote>

The *single biggest lesson* I learned, and a point emphasized by _Agile Development_, the subject of this lecture, was that requirements never stay the same for even a few days.  You had better get used to it, or better yet, take advantage of it lest you drown.  Interestingly, there is a close relationship between what XP espouses and between what I ended up following at jGuru, most of which I learned on the job or by listening to Tom Burns, our CEO.

You should read this excellent blog entry by Erik Swan, CTO and cofounder of Splunk, describing [how software development is like managing the furniture in your living room](http://dev.splunk.com/2007/09/17/the-feature-magpie-phenomenon).

# Lifecycle stages

[UW course slides](http://courses.cs.washington.edu/courses/cse403/14sp/lectures/lecture02-lifecycle.pdf):

* Problem definition
* Requirements specification
* Design
* Coding
* Integration
* Testing
* Deployment
* Maintenance

Mix-and-match and loop to create your own methodology

Common ones:

* **code-and-fix**: write some code, debug it, repeat (i.e., ad-hoc)
* **waterfall**: standard phases (req., design, code, test) in order
* **evolutionary prototyping**: build an initial small requirement spec, code it, then "evolve" the spec and code as needed
* **staged delivery**: build initial requirement specs for several releases, then design-and-code each in sequence.

## Suitability

[Balancing Agility and Discipline: A Guide for the Perplexed](http://www.amazon.com/Balancing-Agility-Discipline-Guide-Perplexed/dp/0321186125)

Agile home ground:

* Low criticality
* Senior developers
* Requirements change very often
* Small number of developers
* Culture that thrives on chaos

Plan-driven home ground:

* High criticality
* Junior developers
* Requirements don't change too often
* Large number of developers
* Culture that demands order

# Agile in a nutshell

[Agile development](http://en.wikipedia.org/wiki/Agile_software_development) describes a number of lightweight development processes such as [SCRUM](http://en.wikipedia.org/wiki/Scrum_(development%29) and [Extreme Programming XP](http://www.extremeprogramming.org) that have the following common goals:

* Focused on the needs of the customer, collaborating with them

* Short and frequent release iterations with **working** software

* Individuals and interactions over processes and tools

* Responding to change not slavishly following a plan

and usually has the following key elements in its mechanism:

* Team works with customer to come up with a series of user stories in no particular order and then works with the customer to decide on priorities.

* Cost estimation is done by estimating difficulty not the number of hours. After awhile, the number of units of work done by a team per unit time stabilizes and forecasting and becomes more accurate.

* Develop software in short development cycles called _sprints_.  1-4 weeks usually. Iterations include everything from requirements analysis through coding and testing and documentation. The key is that the software the end is releasable. Can be internal or extra release. The deadline on a sprint is firm, but you can change the scope (number of stories or goals to be completed). Team members should announce early when they must drop tasks in order to meet the sprint deadline.

* All team members must be in the same place; no virtual development. They have daily _stand up meetings_ with all team members getting up *briefly* to say what they did yesterday and what they plan to do today.  *Transparency* is a key issue: the peer pressure helps increase velocity and quality.  This is also called a _scrum_. This is where the team decides on development priorities, how to add new stories, etc.  The team negotiates with each other about what to do now and what to do later.  The product/project managers provides influence from the customer. Some groups use a _ScrumMaster_ who is really just a facilitator for the meeting.  Here is the ScrumMaster from @(http://www.splunk.com, Splunk, Inc) who dons a rugby helmet for fun:
<img src=figures/david-scrum-leader.jpg width=150>

* _Scrum of scrums_. The managers meet with other managers once a week so that there can be communication between groups. This group discusses the status graph and open stories and dependencies between these stories.  Which tasks have been completed and which have been started. Bug metrics are displayed.

* Testing is an important part of agile development. One company CTO told me that they have 12,000 functional tests written in Java code that run constantly.  The developer gets e-mail when their code fails unit test. There is a build master, a rotating position, who watches the failed functional tests.  Previously no one watched these tests; no one was accountable.  Many bugs were allowed to propagate whereas now 95% of the functional tests or above are passing.

[Martin Fowler (of refactoring fame) says](http://www.martinfowler.com/articles/newMethodology.html#FromNothingToMonumentalToAgile):

* _Agile methods are adaptive rather than predictive_. Engineering methods tend to try to plan out a large part of the software process in great detail for a long span of time, this works well until things change. So their nature is to resist change. The agile methods, however, welcome change. They try to use processes that adapt and thrive on change, even to the point of changing themselves.

* _Agile methods are people-oriented rather than process-oriented_. The goal of engineering methods is to define a process that will work well whoever happens to be using it. Agile methods assert that no process will ever make up the skill of the development team, so the role of a process is to support the development team in their work.

[Ian MacFarland's XP slides from talk at USF](SLS-XP-Presentation.pdf).

# Agile Manifesto

Here are some fundamental principles to guide development.

Quoting from [Principles behind the Agile Manifesto](http://www.agilemanifesto.org/principles.html):

* Our highest priority is to satisfy the customer through early and continuous delivery of valuable software.

* Welcome changing requirements, even late in development. Agile processes harness change for the customer's competitive advantage.

* Deliver working software frequently, from a couple of weeks to a couple of months, with a preference to the shorter timescale.

* Business people and developers must work together daily throughout the project.

* Build projects around motivated individuals.  Give them the environment and support they need, and trust them to get the job done.

* The most efficient and effective method of conveying information to and within a development team is face-to-face conversation.

* Working software is the primary measure of progress.

* Agile processes promote sustainable development.  The sponsors, developers, and users should be able to maintain a constant pace indefinitely.

* Continuous attention to technical excellence and good design enhances agility.

* Simplicity--the art of maximizing the amount of work not done--is essential.

* The best architectures, requirements, and designs emerge from self-organizing teams.

* At regular intervals, the team reflects on how to become more effective, then tunes and adjusts its behavior accordingly.

# A case study

I had a conversation with a friend of mine, Mark Sambrooke, who is a product manager for a large software company here in the Bay Area. I made a number of notes as he described how agile development works in his team. Then, in the summer of 2007, I visited the company for a day to watch scrums in action and to interview developers.

A project begins by having lots of conversations with the customer.  Even if there is a single company as customer, there are usually multiple parties within such as users, the purchaser, the IT group, the manager of the project, and so on. So, later when deciding priorities, the product manager has to take all of those people into consideration.  The goal of these meetings is to produce a series of user stories in no particular order.  These simply reflect the kinds of things that users want to be able to do.  Once you have a list of stories, you go back to your development team. Who is on the development team?  The coders, the quality assurance people, and the documenters.  Your goal is now to size all of the stories (note: you are not estimating time--you are estimating size).  There are two important things to keep in mind when sizing stories. First, all members of the team must be taken into consideration.  Just because you can code something quickly does not mean that you can test it quickly; it might also take a very long time to document. You must take all time requirements into consideration.  Second, size must be relative not absolute.  Ask a human how tall building is and you will get a huge variance, but ask a human to order buildings by height and he/she is pretty good at it. The same is true of software.  You may not know how long something takes, but usually you know whether it takes more or less than another problem.

Sizing starts out by picking an average story from the entire list and deciding that it is maybe a 3. Then the other stories are sized relative to this and give them size numbers to reflect their relationship. Sizing numbers are done using the Fibonacci series: 1 2 3 5 8 13 21 34 ... The reason for this is that you avoid the temptation of false precision.  Remember that precision such as 14.5 does not imply accuracy.  The numbers might get large, but you may only use 10 different discrete size numbers.

After sizing, you must prioritize the stories in terms of order of development.  This can be done with the customer, but most likely a product manager decides on the priority given what he or she knows of the market and what all of the various customers have said.

From the sized stories, your team must break down the stories into tasks; e.g., create classes, update schema, make unit tests, write documentation, and so on.  These tasks are all written down on little cards.  This is the first time that you start talking about time. The process goes like this:

stories -> size stories in points -> create tasks measured in hours

These cards are usually less than a day's worth of work and so you can do a pretty good job of estimating the time requirements for each task. These cards are all placed on the left side of the wall.  Then to the right is a middle column of the team members names.  All the way to the right is an empty column where completed cards will go.

<img src=figures/07-24-07_0958.jpg width=300><img src=figures/people-cards.jpg width=200><img src=figures/done.jpg width=200>

Every morning, there is a standup meeting for 15 minutes where everyone on the team talks about what they did the day before and which tasks/cards they are going to do today.  They move the finished cards from yesterday to the right column next to their name. If they have not finished the task they update the remaining time on the card and leave it where it is.  The cards they intend to complete today, they move next to their name in the middle column.  There is considerable peer pressure to complete your tasks.  Everyone knows what everyone else is doing. QA members report just like the developers...they indicate what they have finished testing and who or what they are waiting on. You can make new tasks in the scrum and some tasks may split.  You can also decide to remove a goal or push to the next sprint.

During the standup meetings, there is considerable negotiation and discussion about which tasks to do next (i.e., task dependencies) and to discuss issues related to code integration and communication. The quality assurance and documentation people are in the room also.  They have tasks to complete as well.  Naturally there are plenty of other opportunities during the day for two people to get together and communicate directly.  There is no way to do the agile method with people in different locations.

The development process is broken down into multiple development cycles. A release is generally multiple cycles.  You would not want to see a new version of Microsoft Word come out every month.  The cost to IT groups would be very high.  (I pointed out that there are some things like websites that can easily stand very quick release cycles).  Mark uses a two to four week cycle for development.  At the end of each cycle everything is done including documentation and testing.  You have a very solid foundation upon which to continue.  You could actually do a release at the end of every development cycle.  Sometimes these cycles are called _sprints_.

For the first cycle or two, it is hard to estimate how many tasks you will complete.  Quickly, however, your team's work output will stabilize and you will get a velocity that you and your customer can use to compute trajectory towards a finished project.

<img src=figures/timeline.jpg width=200>

The yellow line in that image is the baseline slope chosen from the last sprint.  The line moves from the number of tasks completed in the previous sprint sloped negatively to zero crossing the x-axis at the end of the 30 day sprint.

This whole thing seems like anarchy, but it turns out there are a number of managers involved, including the product manager and the project manager, to ensure that everything is on track and to manage all of the communication details with other teams.

The optimal team size seems to be about six or seven, with a max of 10.  Mark has three developers, two quality assurance people, and one documentor. He indicated that a team of 20 is not working so well at his company.  Basically it's hard to keep track of what 19 other people are doing.

How then do you build a large project? You have multiple teams working on different pieces or components or plug-ins.  Multiple teams coordinated through the managers associated with each team. Mark's impression is that this would not work with inexperienced developers. That said, older developers seem to resist going to an agile method.

Overall time estimation: In the waterfall method, you make a wild ass guess (a "WAG") or simply say "yes" when the CEO asks you if you can have the product done by September 1. ;) With the agile method, your initial answer is "I don't know", but as time progresses you get a good sense of your team's velocity which makes it possible for you to start projecting when you might finish.  The closer you get to finishing, the more accurate your answer.  With the waterfall method, you actually have no idea up until you are actually totally done when it will be finished.  This is primarily because software development progresses at an unknown pace and even when you finish you need to do quality assurance, which could kick it back to development for an unknown amount of time.

# XP

I summarize @(http://www.amazon.com/Extreme-Programming-Explained-Embrace-Change/dp/0201616416, Extreme Programming Explained) by Kent Beck in this section and pepper it with experience I gained from building jGuru.com.

The four XP Values

1. Communication.  Problems can often be traced back to poor communication (TJP; cite class project that split,diverged because members didn't communicate).  Programmers might miss telling others of important design change or not talk to customers.  Manager might not ask programmer the right question.  TJP: example of somebody at jGuru delivering bad news about wrong version being sent to customer.

1. Simplicity.  "What is the simplest thing that could possibly work?"  It's very hard to implement something stupid and simple especially when you are always looking ahead to the future--you desire software that won't have to be changed later.  But worrying about the future implies you are listening to the "changes later cost more than changes now" philosophy.  The speed you gain from simplicity today can help fix speed problems later if you turn out to be horribly wrong 2% of the time.  TJP: You have no idea will survive even a few days.  Elegant software mostly comes from refactoring after the code has lived a few "generations."  Use an evolutionary, iterative refined process.  Use OO techniques and data driven software as much as possible to isolate things that might change.  Example: URL->pageclass map, config files, java->rdms mapping, managers, etc... 

1. Feedback.  Programmers use tests to get feedback about state of the system.  Customer asks for a feature, get immediate feedback about difficulty (TJP: I remember Tom Burns asking me all the time "How hard would it be to implement ...").  Someone tracking project gives feedback to programmers and customers.  Feedback also works on scale of weeks and months as customers write functional tests for features.  Code and put into production most important features first so you can learn from it.  In the old days, "production" meant you were done.  In XP, it's always under production.

1. Courage.  When you find a serious design flaw, have the courage to go fix it; your project will surely die w/o it.  Throw code away even if you've worked hard on it.  Sometimes rebuilding results in much better code.  Sometimes you have lots of choices for a design, have the courage to just try them out to see how they feel; toss losers and start over with promising design.

It takes courage to fix a fundamental design flaw or make a huge simplification in an existing system.  Try it out!  TJP: Tom/me in France building Karel language debugger.  Listen to others and try out huge simplifications.  I had to throw out my design and I wasted all that meeting time.

## Erich Gamma's summary:

* Code is the key activity

* Just-in-time software culture

* Frequent release cycles

* Make change your friend

* Communication is done with code

* Life cycle and behavior is defined in test cases (i.e., code)

* Problem reports are accompanied by failed test cases

* Improve code with refactoring

Does not imply that just start "daredevil" hacking.  You must be disciplined.

## Kent Beck's Summary:

* Review code all the time (pair programming)

* Everybody tests all the time (coders and users)

* Use simplest possible design that works

* Work to define and refine the architecture all the time

* Integrate and test several times a day

* Really fast iterations (releases): seconds, minutes, hours not weeks or months

"Extreme" implies "what is good, do to the extreme."

**Paradox**: What would you do if you had lots of time to build a project?  You'd build lots of tests, you'd restructure a lot, and you'd talk with the customer and other programmers a lot.  The normal mentality is that you never have enough time.  XP says that if you operate in this counterintuitive manner, you'll get done faster and with better software.

Designed for 2-10 programmers.  (TJP: note similarity with the 10-person surgical team suggested by Frederick Brooks in "The Mythical Man Month").

## What's the difference from other methodologies?

* Early and continuing feedback from short cycles

* Incremental planning; plan expected to evolve

* Flexibly schedules implementation to respond to business needs

* Relies on oral communication, tests, and code to describe system structure and intent

* Evolutionary design process that lasts as long as system lasts

* Close collaboration of programmers with ordinary skills

* Reliance on practices that work with both short-term programmer instincts and long-term project interests

## Overcoming Software Problems

* Schedule slips.  XP has short release cycles so slip is limited. Lots of feedback during dev with interim releases.  Do the highest priority item first so if you slip, you are missing just the low priority stuff.  TJP: project examples: some didn't do the required stuff first and some tried to build all at the same time.

* Project cancellation.  XP ask customer to choose smallest release that make most business sense--less to go wrong before going into production.

* System goes sour.  XP has lots of tests that are run and re-run when you change things or add features.  You program from confidence.  TJP: ANTLR project example: no tests and am always worried I'll break something!

* High defect rate.  XP again does unit tests and functional tests by users or from their perspective.

* Business misunderstood.  XP says customer is integral part of team.  Spec continuously refined as features appear.  TJP: As you get a chance to actually toy with something you see its problems and limitations.  Overdesign at first is a waste of brainpower.

* Business changes.  With short release cycle, XP will be less likely to get caught mid release.  Customer can change functionality for anything not yet done because programmers won't notice.  They are just walking down the list.

* False feature rich.  XP does only high priority stuff first.  TJP: my first large project in high school: bookstore.  I spent so much time doing cool features that didn't help functionality that I ran out of time and software never deployed.

* Staff turnover.  XP gives programmers lots of responsibility for their own work and gives them lots of feedback.  Less chance a programmer will become frustrated by being asked to do the impossible.

## Important XP Philosophy

* **Focus on quality**.  You can actually go faster in the long run if you build more reliable software.  Making boatloads of tests give you the confidence to write faster and with less stress knowing that you are not going to break something.  Everybody wants to work on a good system.  If you don't focus on quality, the system will decay and no one will want to work on it.

* **Focus on scope**.  Just as when you write software, having to care about less is a big help.  By figuring out the minimum workable requirements and sticking to it, you will get better software and on time.  "Customers can't tell us what they want.  When we give them what they say they want, they don't like it."  This is always true.  Customer doesn't know what they want at first.  Only seeing some actual software can they refine and limit the scope etc...  **Use the softness of requirements as an opportunity, not a problem.**

# Cost estimation and planning

_This information is derived from Ian McFarland's slides from above and by talking to a number of my colleagues that use agile development commercially.  This stuff really works!   I also note that it is very similar to how Tom Burns and I built the jGuru.com server._

The application is broken down into a large number of so-called "user stories" that describe one particular customer-visible task, operation, process. For some agile development styles, developers actually write the user stories down on little cards that they can put up on the wall; these are then organized according to a point system.

Estimating time is difficult, but programmers find it fairly easy to estimate difficulty.  So, estimate difficulty not time.  Each story is broken down into units of work whose difficulty can be quantized. Some people advocate 3 levels:

1. I know how to do it and can do it in less than a day
1. I know how to do it, but it will take some work
1. I don't know how to do it; it is nontrivial

Some teams prefer a value from 1 to 10. Others prefer the Fibonacci sequence.  The key is having a small number of quantization levels otherwise you are right back to poor estimation.  Very large stories are broken down into smaller stories that fit within the max cost.

In agile development, not only is the customer involved in picking features, the customer is given control of prioritizing the features.  It is up to them to balance the cost of development with how soon they need the various feature.

After a month or two of many quick releases and completion of many user stories, a team's work output stabilizes to a known value.  This tells the customer exactly how much they can "spend" each week.  They are free to realign priorities and spend the work anyway they want.  Ultimately, this breeds a great deal of trust between the customer and the developers, resulting in much better communication and a much better product. This work output is sometimes called the _velocity_.