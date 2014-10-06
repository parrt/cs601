Webmail Project
====

In this project you will build a website that acts as a web-based email client server like gmail or hotmail, but with some nice additions.  You will be required to submit functionality and design documents to complement your project as well as status reports every two weeks.

To give you an idea what an extremely successful project looks like, here are the videos Jean Bovet made of his project from years ago:

* [filter](movies/webmail/filter.mov)
* [move_delete](movies/webmail/html_move_delete.mov)
* [presentation](movies/webmail/presentation.mov)
* [search](movies/webmail/search.mov)
* [smartfolders](movies/webmail/smartfolders.mov)
* [spellchecker](movies/webmail/spellchecker.mov)

The completed project is due last day of class but you will do interim releases roughly every 2-3 weeks during the semester.  For these releases, you will demo your project for the instructor.

# Goals

The goal of this project is to produce something interesting and useful that requires you to combine the following knowledge:

* Java file I/O
* databases
* sockets
* Java Collections (data structures)
* multi-threaded programs
* protocols such as SMTP, POP
* web servers and web page generation
* client-side JavaScript / UI
* HTML
* search engine construction
* statistical pattern matching
* revision control systems (git)
* issue/release management
* UNIX (your server will run on linux)


Most people's projects end up being about 6-7000 lines of code.

# Features

v0.1, ...:
https://github.com/parrt/cs601-webmail-skeleton/releases

https://guides.github.com/features/issues/

| Points | Feature |
|--------|--------|
| | User registration (multiple users, each with their own POP server to pull mail from) |
| | User log in/out |
| | Edit user account to change password etc... |
| | Pull email from IMAP servers via your own IMAP protocol handler and display on website; mail is stored in the database |
| | Have a functional "check mail" button |
| | Send/reply to email using your own SMTP client |
| | User is able to search all of their mail for keywords; use a simple linear walk of the data rather than a sophisticed inverted index or search engine like Lucene. The user should be able to pick the field such as "from" or "subject" to search in. |
| | Sort mail folder display by various fields such as sender's email, subject, ... |
| | Support "folders" so users can direct incoming email to various folders manually. |
| | User is able to delete mail (sends to Trash folder and then have a "empty trash button") |
| | lucene (The user should be able to pick the field such as "from" or "subject" to search in.) |
| | attachments (send/receive) |
| | spell checking |
| | spam filtering |

# Grading

**The authors of the best 2 projects in the class will be excused from exam II.**

**You must complete the required level to pass the entire class.**

The evaluation of "quality" is necessarily a subjective measure, but I will do my best to quantify what I'm looking for and rate you all according to the same measure.  Each feature will be rated at each release (if it is complete).

*By definition, there is no late project*--your last working release will be considered in lieu of any unfinished project.  Missing a release in order is not acceptable.

The instructor will review your work for 10 or 15 minutes at each release.
