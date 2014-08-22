"Git on it"
=====

Git is the assembly language of revision control systems and is very hard to use from the commandline so I restrict myself to a few memorized commands. Fortunately, development environments such as intellij know how to use it and you can focus on high-level operations such as: commit, diff, pull/push from/to remote repository. To truly understand what's going on however it's best to understand the individual commands from the commandline.

## "Git on it"

Let's start out by looking at how you will work on the first project by cloning from your github repository within the [CS601 organization](https://github.com/USF-CS601-F14).

### Cloning a repository

Go to github and locate the repository of interest, such as your first project:

https://github.com/USF-CS601-F14/parrt-doublekey

Then locate the SSH URL on the right-hand gutter of the webpage and click the clipboard icon to get it into your paste buffer:

![github ssh url](figures/github-ssh-url.png)

the URL is actually something like ``git@github.com:USF-CS601-F14/parrt-doublekey.git`` and, to get it onto your local disk, you clone it:

```
$ cd ~/cs601/projects
$ git clone git@github.com:USF-CS601-F14/parrt-doublekey.git
$
```

This makes a ``parrt-doublekey`` directory in the current directory, which in this case is ``~/cs601/projects``. The ``parrt-doublekey`` directory should look like this:

![](../projects/figures/map-hier.png)

You will also notice a hidden directory that contains all of the stuff needed by git to manage your repository locally:

```
$ ls .git
COMMIT_EDITMSG  ORIG_HEAD       description     info            packed-refs
FETCH_HEAD      branches        hooks           logs            refs
HEAD            config          index           objects
$
```

Technically speaking you never have to think about github ever again as you can continue to commit and modifies repository forever. Github will only ever see changes you push to it, which we will do shortly.

Similarly, if you ever want to throw this copy of the repository out with all the changes you have made, just remove the directory. You don't need to notify anybody. Certainly github does not care if you throw away a clone of its repository.

### Adding files

To add a file, such as your hash table, just create the file and notify ``git``:

```
$ ~/cs601/projects/parrt-doublekey/src/cs601/collections
$ ... created DoubleKeyHashMap.java ...
$ git add DoubleKeyHashMap.java
$
```

To commit the "change" (the addition of a file) to the repository, we ``commit``:

```
$ git commit -a -m 'add start of my hashtable'
[master abd47d9] add start of my hashtable
 1 file changed, 1 insertion(+)
 create mode 100644 src/cs601/collections/DoubleKeyHashMap.java
$
```

The ``-a`` means "all changes" and ``-m`` means message. From ``git help commit``:

<blockquote>by using the -a switch with the commit command to automatically
           "add" changes from all known files (i.e. all files that are already
           listed in the index) and to automatically "rm" files in the index
           that have been removed from the working tree, and then perform the
           actual commit;
</blockquote>

### Changing files

If you want to make a change to one of the files, just do so and do another commit:

```
$ ... alter DoubleKeyHashMap.java ...
$ git commit -a -m 'tweak my hashtable'
[master 7624de2] tweak my hashtable
 1 file changed, 2 insertions(+)
```

### Deleting files

To delete a file, just remove it with the operating system as usual and perform a commit.

```
$ rm Useless.java # assume Useless.java is already in the repository
$ git commit -a -m 'get rid of a useless file'
[master 20bddef] get rid of a useless file
 1 file changed, 0 insertions(+), 0 deletions(-)
 delete mode 100644 src/cs601/collections/Useless.java
$
```

### Push your changes to github

We have been working on a local copy of the repository stored at github. To send our changes from the local copy to github (assuming we used ``clone`` to make the local copy), we use ``push``:

```
$ git push origin master
Counting objects: 8, done.
Delta compression using up to 12 threads.
Compressing objects: 100% (5/5), done.
...
   e5d5910..c764081  master -> master
...
$
```

The ``origin`` refers to github in our case and ``master`` is the name of the branch in which we are working. More on branches shortly.

### Pulling changes from github

If you move to a new computer, or your local copy gets wiped out for some reason, you need to pull from github any changes pushed to it that your local copy does not have. We use ``pull`` for this.

If everything is up to date, you will see the followng:

```
$ git pull origin master
From github.com:USF-CS601-F14/parrt-doublekey
 * branch            master     -> FETCH_HEAD
Already up-to-date.
$
```

Otherwise you will see a message saying that some changes will come down from github to your local copy.

## Why use revision control?

### Solo programmer, local disk use

If you are working solo, from a single machine, and you have a regular backup mechanism in your development environment or from the operating system like Time Machine (OS X), you can get away without a formal revision system.

There are lots of important operations that can be faked without a revision system.  It's a good idea to keep track of versions of the software that work or other milestones. In the old days, people would make a copy of their project directory corresponding to important milestones like "Added feature X and it seems to work." You can do comparisons using a diff tool in between directories.

Even working solo, revision control allows you to keep a clean master source but easily try out new features that can be merged back in if they are successful. If you are working on that feature and someone wants you to fix a bug in the released version, you can easily switch branches back to the master. Then you can fix the bug and push a new version and easily flip back to the feature branch. And you can bring in that bug fix to your feature branch from the master branch easily.

It's often the case that I decide to abandon some changes and want to revert back to where I started, whatever "started" means. Revision control makes this easy.

There are often multiple versions of a single product that you have to maintain. Keeping all versions synchronized just by comparing directories is a nightmare.

Whether your IDE does it or a revision control system doesn't, I find it very important to look back at recent changes to see what changes have introduced a bug. Or I decide to abandon a small piece of what's going on and flip a file back to an old version.

### Solo programmer, sharing across machines

In order to work on that software from your home machine and a laptop for example, you have to make copies. That introduces the possibility that you will overwrite the good version of your software. Or, you will forget that you had made changes on your laptop but have now made a budget changes on your desktop.  Resolving things can be tricky and error-prone.

As a side benefit, pushing your repository to a remote server gives you a backup automatically.

### Multiple programmers

When you add another person to the project, people end up mailing code around but it's difficult to perform a merge. My experience watching students do this reveals that two versions of the software always appear. Both students shout that their version is better and that the other version should be abandoned.

In my experience, no matter how you try to fake multiple states of the source code and share, merging changes to work on the same code base is a nightmare.

Once in a while I go back and I look at the history of changes. Sometimes I want to know who screwed this up or I want to see the sequence of changes that I made or that were made by somebody else.

Although I do know a single outlier company with two programmers in it that do not use revision control, I believe that every company you will encounter uses it. For that reason alone, you need to learn revision control to be functional in a commercial setting.

## Central versus distributed repositories

The [git-scm book](http://git-scm.com/book/en/Getting-Started-About-Version-Control) has a good description an illustration of how multiple computers can share a central repository of code. If you are only going to do things locally, I suppose you could get away with the original RCS (By Walter Tichy, who was a professor of mine at Purdue University!). I have also used a central repository mechanism: CSV, SVN, and perforce (p4). That is cool, but its main weakness is that losing the server means you lose all history. The central server record the database of all changes. Git and mercurial lowly clone the entire repository there is no central control. On the other hand, we can pretend that there is a central control; i.e., github. We use it like a central repository but it does not have the single point of failure of previous systems.

## Why git?

Because of http://github.com. Full-stop. Hg (Mercurial) is *way* simpler and would be my preference.

http://github.com adds dramatically to the benefits because of its extra functionality. We will discuss this later but forking/pull requests are insanely awesome because it makes it easy for someone to contribute to your project or for you to contribute to another one. They also have good code review facilities and issue management.

## "Git 'er done"

### Pure local use

All you have to do is create a directory, and then run:

```
$ cd test
$ git init
```

and you have a git repository. Then you just add files and do a commit

```
$ cat > t.c
$ git add t.c
$ git commit -a -m 'initial add'
```

Then you can make changes and do another commit. Make sure use the ``-a`` command.  By the way, deleting a file is also considered a change but you can also use ``git rm file.c``.

If you make a change and want to know how it's different from the current repository version, just use diff:

```
$ ... tweak t.c ...
$ git diff t.c
...
```

If you screw up and want to toss out everything from the last commit, to a reset and make sure you use the hard option:

```
$ ... tweak whatever you want ...
$ git reset --hard HEAD
```

which throws out all changes since the last commit. If all you want to do is revert uncommitted changes to a single file, you can run this:

```
$ git checkout -- filename
```

I *think* they call that funny ``--`` thing "sparse mode." See? Git is the assembly code of revision systems.


One of the other things I often have to do is to [fix the commit message](http://stackoverflow.com/questions/179123/edit-an-incorrect-commit-message-in-git) that I just wrote in a commit command.

```
$ git commit --amend -m "I really wanted to say this instead"
```

If you forgot to add one of the files and you wanted in a previous commit, you can also use amend. Just add the file and use amend:

```
$ git add t2.py
$ git commit --amend --no-edit
```

Finally, if you want to figure out what changes you have made such as adding, deleting, or editing files, you can run:

```
$ git status
On branch master
Your branch is up-to-date with 'origin/master'.

Untracked files:
  (use "git add <file>..." to include in what will be committed)

	.idea/
	tests/

nothing added to commit but untracked files present (use "git add" to track)
$
```

#### With a remote server like github

When you're working by yourself and without branches, a remote server acts like a central server that you can push and pull from. For example, I push from my work machine and pull to my home machine or my laptop. And then reverse the process with changes I make at home over the weekend.

To begin the process of working from a remote server, we clone from that server. For example, from github, I can clone my build system called ``bild``:

```
$ cd ~/projects
$ git clone git@github.com:parrt/bild.git
...
```

which will create a ``bild`` directory in my ``projects`` directory.

Once I have commit all of my changes and I'm ready to go home, I push to the origin:

```
$ git push origin master
```

From home, I do:

```
$ git pull origin master
```

The ``origin`` is the alias for the original server we cloned from and ``master`` is our master branch, which we can ignore until we look at branches.

To look at the remote system alias(es), we use:

```
$ git remote -v
origin	https://github.com/parrt/bild.git (fetch)
origin	https://github.com/parrt/bild.git (push)
$
```

## Common operations

After ``grep``ing through much my bash history (I use git from command-line often), I see the following commands / operations: ``add``, ``branch``, ``checkout``, ``commit``, ``config``, ``diff``, ``fetch``, ``log``, ``merge``, ``pull``, ``push``, ``remote``, ``reset``, ``rm``, ``stash``, ``status``. A histogram shows different things depending on what I'm doing. When I'm working on course materials like this, I see almost exclusively ``commit``, ``push``, ``add``, and ``pull`` as I tweak things, push them to github from work, and then pull them in from home.

When developing code, I primarily ``push``, ``pull``, ``branch`` (asking what branch I am in), ``commit``, ``add``, ``checkout`` (switch branches), ``remote`` (check which server I am talking to), and then a few ``fetch`` and ``reset`` commands. I do a lot of ``diff`` but usually from within my development environment so that is not shown in the commandline history.



