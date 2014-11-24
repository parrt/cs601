Multi-programmer development processes with github
====

You should definitely read the excellent post by [Vincent Driessen on git branching](http://nvie.com/posts/a-successful-git-branching-model/). I have are used some of his graphics here.

I highly recommend [SourceTree from Atlassian](http://www.sourcetreeapp.com/) when you start dealing with lots of branches. It's an excellent way to visualize what's going on in your repository.

# Github vs local

Create repo on github then clone on **laptop**, **work** machines.

<img src=figures/david-repo.png width=200>

On each box do this:

```bash
$ cd ~/projects
$ git clone git@github.com:david/webmail.git
$ git remote -v
origin	git@github.com:david/webmail.git (fetch)
origin	git@github.com:david/webmail.git (push)
```

Then to freshen, do this:

```bash
$ cd ~/projects/webmail
$ git pull origin master
...
$ git commit -a -m 'my change msg'
$ git push origin master
```

# Single developer

## Branching

Each branch is tracking one train of thought, with the **master** branch considered the latest stable version of the source code.

*The master branch always compiles and runs at least the majority of the tests.*

We also use branches for bug fixes, releases, features, and possibly general development depending on whether you are working alone or in a team. 

<img src=figures/single-dev.png width=500>

As a real example, here you can see that I have merged an ANTLR `xpath` branch back into the master:

<img src=figures/xpath-branch.png width=500>

You can also see that I have abandoned some branches such as `parser-interpreter`.

Here is how to create a new branch and make some commits on that path:

```bash
maniac:master:~/antlr/code/antlr4 $ git checkout -b new-feature
Switched to a new branch 'new-feature'
maniac:~/antlr/code/antlr4 $ cat > blort.txt
random file
maniac:~/antlr/code/antlr4 $ git add blort.txt
maniac:~/antlr/code/antlr4 $ git commit -a -m 'add a new file'
[new-feature 623d94a] add a new file
 1 file changed, 1 insertion(+)
 create mode 100644 blort.txt
```

Here is how to push this new feature branch to the origin as a remote branch  (remember that branches only exist locally until you push them):

```bash
maniac:~/antlr/code/antlr4 $ git push origin new-feature
Counting objects: 3, done.
Delta compression using up to 12 threads.
Compressing objects: 100% (2/2), done.
Writing objects: 100% (3/3), 278 bytes | 0 bytes/s, done.
Total 3 (delta 1), reused 0 (delta 0)
To git@github.com:parrt/antlr4.git
 * [new branch]      new-feature -> new-feature
```

**Sidenote**: This pops up a pull request automatically on github:

<img src=figures/pull-new-feature.png width=600>

If you don't want your branch anymore or you have merged it, you can delete it:

```bash
maniac:new-feature:~/antlr/code/antlr4 $ git checkout master
Switched to branch 'master'
Your branch is up-to-date with 'origin/master'.
maniac:master:~/antlr/code/antlr4 $ git branch -d new-feature
error: The branch 'new-feature' is not fully merged.
If you are sure you want to delete it, run 'git branch -D new-feature'.
maniac:master:~/antlr/code/antlr4 $ git branch -D new-feature
Deleted branch new-feature (was 623d94a).
```

Remember that the branch is local and you should also delete it at github:

<img src=figures/del-new-feature.png width=600>

## Merging for single developer

To merge changes from one branch back into the master, follow this procedure:

1. `git checkout master`
1. `git pull origin master` to make sure you have the latest
1. `git checkout mybranch`
1. `git merge master` pull in any changes from the master you might have made
1. resolve any conflicts
1. compile
1. test
1. `git commit -a -m 'pulling changes from master into mybranch'`
1. `git checkout master`
1. `git merge mybranch` pulling your changes from that branch
1. as you are the only developer, there shouldn't be any changes so no need to resolve.
1. `git push origin master`

At this point your `mybranch` and `master` should be identical.

# Multi-programmer collaboration

## Sharing one origin

Each developer has a clone of the main "origin" repository on their local machine (see [Driessen's post again](http://nvie.com/posts/a-successful-git-branching-model/)):

<img src="http://nvie.com/img/centr-decentr@2x.png" width=500>

The term `origin` comes from the alias for that repository:

```bash
$ git remote -v
origin	git@github.com:david/webmail.git (fetch)
origin	git@github.com:david/webmail.git (push)
```

Branches are critical when everyone shares the same repository so that pushes are not done to the master branch.

Programmers can pull from other repositories as well as the origin. For example, here I have added a remote called `clair`:

```bash
$ git remote add clair https://github.com/clair/webmail.git
$ git remote add upstream git@github.com:org/webmail.git
$ git remote -v
origin	git@github.com:david/webmail.git (fetch)
origin	git@github.com:david/webmail.git (push)
clair	https://github.com/clair/webmail.git (fetch)
clair	https://github.com/clair/webmail.git (push)
```

I can then pull things from `clair` or `origin`.

## Forking from main repo

Perhaps a safer mechanism is for each developer to have a fork of the original repository. This way a specific *pull request* must occur to get changes into the original. Each developer then has their own complete sandbox and can push and pull from multiple computers without worrying about messing up the original repository. It looks like this:

<img src=figures/fork-clone.png width=400>

You will want to add an `upstream` remote (a convention):

```bash
maniac:master:~/antlr/code/antlr4 $ git remote -v
origin	git@github.com:parrt/antlr4.git (fetch)
origin	git@github.com:parrt/antlr4.git (push)
upstream	git@github.com:antlr/antlr4.git (fetch)
upstream	git@github.com:antlr/antlr4.git (push)
ericvergnaud	https://github.com/ericvergnaud/antlr4.git (fetch)
ericvergnaud	https://github.com/ericvergnaud/antlr4.git (push)
```

To bring changes from the original into your forked version you do this:

```bash
$ git checkout master
$ git pull upstream master # pull original to, say, laptop
$ git push origin master   # push back to my fork on github
```

## Pull requests

Pull changes from a fork into the original repository. It's the way that people can contribute to your project or people using repo forks on the same project can put features and fixes together. It's a way also to do code reviews before they go into the main repository. Here's an example pull request someone has concerning one of the grammars in `grammars-v4`:

<img src=figures/pull-request.png width=400>


## Merging for multiple developer

This assumes you are using the multiple forks approach with pull requests.

Let's say you are building a new feature in `mybranch` in your fork of some original repository which will appear as `upstream` in your remote list. This branch will be on your laptop, work machine, and you are github fork of the original. When you are ready to get it back into the original repository to share with your colleagues, you would send a request.

Before it is ready however, you have to follow a strict procedure to make sure you don't push anything into the master that doesn't compile or doesn't pass tests.

1. `git checkout master`
1. `git pull origin master` to make sure you have the latest from your fork
1. `git pull upstream master` to make sure you have the latest from original
1. `git push origin master`
1. `git checkout mybranch`
1. `git merge master` now this branch has all changes from the original repository from your colleagues
1. resolve any conflicts
1. compile
1. test
1. `git commit -a -m 'pulling changes from master into mybranch'`
1. `git push origin mybranch` push mybranch to the github repository. Github will now see this and automatically ask if you want to create a pull request from it; e.g.,
<img src=figures/pull-new-feature.png width=600>
1. create pull request by going to original repository (`upstream` from your perspective) at github website; it might be visible also at the `origin` too.
1. Use the website interface to merge pull request.
1. Delete the branch locally and on github
1. `git checkout master`
1. `git pull upstream master` bring in changes you just merged
1. `git push origin master` push it back to your fork from your laptop
