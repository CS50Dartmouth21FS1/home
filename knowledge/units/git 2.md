In this unit, we dive into Git to better understand its core concepts and commands.
We learn several things:

-  the concept of version control
-  The git workflow and the Three State Model
-  git setup
-  basic git commands
-  the concept of a *commit history*
-  the concept of *branches*
-  the concept of *remotes*
-  git commands to commit, branch, merge, push, and pull

We cannot describe everything about git in these notes.
See our list of [git resources](https://github.com/CS50Dartmouth21FS1/home/blob/main/logistics/systems.md#github).

[:arrow_forward: Video demo](https://dartmouth.hosted.panopto.com/Panopto/Pages/Viewer.aspx?id=3abb3714-8c8b-4404-a7f6-ad0201356d04) of most of these concepts and tools.
(apologies for the low video resolution!)

## Version control

It may be a surprise to you, but you have probably used your own 'version-control system' before.

Whenever you edit a Google Doc, for example, the application saves a history of your edits.
Whether you have been editing the document for several days, or even several months, you can click a link and see the day-by-day history of changes to the file – or choose to revert to an earlier version of the file.
Google Docs automatically and transparently provides this version-control feature.

*Version-control systems* are just tools to help you do just that: to maintain multiple versions of files or a set of files over time.

In software development, such systems are usually called *source-code control systems* (sccs).
Today, **git** is the most common source-code control system, inheriting ideas from earlier systems like svn, cvs, rcs, and sccs, all of which are still occasionally seen in use.

Git and these other systems maintain an organized historical record of an entire software project, potentially including hundreds of source files spread across dozens of directories, and shared with dozens or hundreds of other developers.

In every such system, a devloper explicitly *commits* an updated version of one or more files to the ongoing *repository* of files (unlike Google Docs where updates occur automatically and transparently).
The result is a *commit history* -- a sequence of commits over time -- that reflect the changing state of the source code.

Git and its predecessors also allow developers to compare the current state of files to earlier commits, or to revert the current files to their contents from an earlier commit.

Although git is extremely powerful, and its features sometimes complicated, anyone can learn the basic use of git with a little practice.

## Git repositories and clones

The *repository* is a core concept in git: it is simply a directory of files and subdirectories, managed as a unit.
In geek speak, we call it a *repo*.

It is possible to have multiple copies of a git repository; each is called a *clone*.
When working alone, you typically have exactly one clone of your repository.
When working in a team, every member of the team has their own clone, usually on their own computer, and each considers it their *local repository*.
Every clone contains the entire history of the repository, all the way back to its original commit.
Every team member can work totally independently, or even offline, editing files and committing changes to their own clone.
We'll postpone for a moment how those changes are exchanged among team members.

## Git workflow

When you edit files in your local repository, you need to understand that git conceives of three different 'areas' that hold versions of the file: the *working copy*, the *staging area*, and the *local repository*.

The *working copy* is the file you see in your editor, or in a directory listing.

The *staging area* is where files are (virtually) copied when you *add* them (aka *stage* them) for the next commit.

The *local repository* contains versions committed in earlier commits.

Let's work through a simple example.

1. Create a new directory and `cd` into it.
   This directory is where you will establish your new project, holding your working copy of all files in that project.
2. Initialize git with `git init`.
   Now this directory is a git repository.
   (It creates a hidden subdirectory `.git` where it keeps all of its information; never touch anything inside there!)
3. Run `git branch -m main`; we'll explain why later.
3. Create a README.md file, in Markdown format, to describe the contents of this project.
4. Run `ls` and you'll see README.md.
5. Run `git status`, which will tell us what git thinks about the status of our repository.
  Notice that it lists `README.md` under "untracked files"; git recognizes it as a new file, but does not yet consider it to be part of the repository - it is not "tracked by git" in an effort to track the history of this software project.

If we want git to start tracking the file, we need to move `README.md` to the *staging area*; that is, we want git to *stage* the file to be included in the next commit (snapshot) I make to the repository.

```
git add README.md
git status
```

The result should show that `README.md` is ready to be committed, i.e., to be included in the next "commit".

Finally, to move this file from the staging area to the local repository, so it becomes part of the historical record of the project, we need to commit it into our repository.
This step will create a new snapshot - which git calls "a commit", to the repository.
Since every commit is an important event, we always include a short explanatory message with the commit.
The message needs to be meaningful and not just a description of the change you made.
For example, a message like "changed 41 to 42" isn't helpful since that change is obvious; instead, your message should indicate *why* you changed 41 to 42 is a useful message.

```
git commit -m "...message..."
```

or just

```
git commit
```

In the second case git will open your [default editor](https://github.com/CS50Dartmouth21FS1/home/blob/main/logistics/systems.md#set-editor) and will wait for you to enter a message and hit Save.

## Git log

To see the history of commits, use the `git log` command.
If the log gets long, it will page like you are using `less`, so you can hit the space bar for the next page, or hit `q` to quit the listing.

Notice that every commit has a unique *hash* - a long string of characters and numbers.
Although meaningless, each is unique and represents a particular commit in the history.
They can often be abbreviated to the first seven characters.

## Git diff

To compare your working copy with the prior commit, run `git diff`; it will highlight all the changes in all the files, using a `less`-like interface to scroll if there are many.

To compare with an earlier commit, mention the commit's hash (obtained from `git log` on the command line):

```bash
git diff 56fd541
```

## Git branches

We refer above to the commit history as a sequence of commits.
In the simplest repo, that may be true, but for most serious work it is common to create *branches* in the commit history.

Consider this scenario: you spent the morning developed a nice new piece of software, tested it, confirmed it meets all the specifications exactly.
You committed your code to the repository and celebrated with a nice mug of cappucino.
Then your client calls, and urgently requests a new feature; let's call it `fribble`.
You could start editing right there in your working copy, adding and updating code to create this new feature, confident that (in the worst case)  you could roll back to the working version in your earlier commit.
But what if the client calls again and says "hang on, we have a higher-priority feature request; set aside that work you've been doing and add this other feature by tomorrow!"
This priority feature is called `urgent`.

This is when you really need branches!

Right after that cappucino you should create a branch:

```
git branch fribble
git switch fribble
```

Now you are working "on the fribble branch"; any commits you make here will happen on that `fribble` branch, not the original `main` branch.
You might be working here for days or months, but you can always switch back to `main` if needed.

```
git commit -a -m "setting aside work on fribble for now..."
git status       # make sure you have no untracked files that missed the commit!
git switch main  # switch back to work on the main branch
```

*Note that it's best to commit any changes before you switch.*

When the priority call arrived, you could make a new branch for it, too.
Assuming you are already working on main,

```
git branch urgent
git switch urgent
```

Now you are working "on the urgent branch"; any commits you make here will happen on that `urgent ` branch, not the original `main` branch.

To see the list of branches in your local repo,

```
git branch
```

The branch labeled with `*` is the current branch – the one on which your new commits will occur.

To switch to a different branch,

```
git switch otherbranch
```

or, in older versions of git, which did not have the `switch` command,

```
git checkout otherbranch
```


## Git merging

Eventually, when you complete work on the feature, and feel like it is ready for testing, you can merge it back into the `main` branch:

```
git commit ...     # any final changes to fribble branch...
git switch main    # switch back to the main branch
git merge fribble  # merge in the fribble branch
```

Git will try to merge everything automatically; if `main` has not changed since the `fribble` branch was created, this merge will happen seamlessly.

Sometimes, however, there is a *conflict* - such as the same line being edited in different ways on the two branches - and then it is left to the user to figure out what to do.
Read more about [merge conflicts](https://docs.github.com/en/github/collaborating-with-issues-and-pull-requests/resolving-a-merge-conflict-on-github) here.

We will look later at a more sophisticated approach, called *git flow*, for managing and merging branches.

In any case, the result of merging two branches creates a new commit that represents that moment in history; such a commit is called a *merge commit*.


## Visualizing git branches

Check out this [website](https://git-school.github.io/visualizing-git) that can help to visualize commits, branching, and merging.


## Git remotes

So far everything we've said imagines you are the only developer, and there is only one clone of the git repository: on your own computer (or, in the case of plank, in your own account).

Git clones can live on servers, too.
These *remote repositories* (often just called *remotes*) usually live on a special-purpose git server, such as those run by GitHub or GitLab.

If you already have a local repository, you can set up a remote copy on GitHub.
Visit GitHub in your browser and go to your account home.

1. Click on the *New Repository* button and give it a name; by convention, its name should be the same as the directory name holding your local repository, but they need not be.
2. Decide whether you want your repository private (initially for you only) or public (literally the whole world).
3. Click *Create repository*.

Follow the instructions to "add a remote" to your existing repository.

If your existing branch is called `main`,

```
git remote add origin git@github.com:username/reponame.git
```

adds a remote named `origin` (just a convention, but a very strong convention) that refers to the new repo on GitHub; here, of course, you should provide the `git@github` address you copied from GitHub.

Then, if your existing branch is called `main`,

```
git push -u origin main
```

"pushes" all the commits on branch `main` to the remote named `origin`.
The inclusion of `-u` sets that remote as the default "upstream" remote for the future; that means you can, after more commits, simply type 

```
git push
```

and not need to specify which remote or which branch.

You can give collaborators access to your GitHub repo; they can then *clone the repo*, that is, to download a copy of the repository history and create a local directory that holds a working copy of the repository.
This new *clone* is their *local repository*, and it is tied to the same *remote* on GitHub.
If they then then make some commits, and push changes to that remote, your local repository (clone) is out of date with respect to its remote copy on GitHub; to update your local copy,

```
git pull
```

or, to be explicit,

```
git pull origin main
```

To pull updates to the `main` branch from the remote called `origin`.

To see your list of current remotes,

```
git remote -v
```

## Branch 'master' vs 'main'

The git software ecosystem is in transition.
The default branch name for new repos has long been `master`; in recent years, tools (like git) and services (like GitHub) are starting to default to `main` (as we do in CS50).

Just in case, when creating a fresh repo, you should rename the initial branch to ensure it has the expected name (`main`):

```
git init
git branch -M main
```

> If you forget, you can rename the branch later, but be careful: if you have collaborators sharing your remote, you'll need to coordinate very carefully.

Many older online git documents and tutorials refer to "the master branch"; although in CS50 we use "the main branch", as do newer git tutorials, the concepts are identical.
We've noticed newer documents referring to "the base branch" as a generic reference to "the 'master' or 'main' branch, whichever you use in your repo."

## gitignore

Normally git allows you to add any sort of file to a git repository.
It is good practice, however, to commit only the *source files*, and never any *derived files*, that is, files derived from source files.
The principle is that other users can clone your repository and rebuild from source, re-creating the derived files when needed.
The inclusion of derived files can lead to conflicts, and (because derived binaries are very large) can explode the size of your repository.

It is also a good idea to exclude editor-produced backup files, operating-system files (like `.DS_Store` on MacOS) that are meaningful only to the local user, and so forth.

So, git looks for a file called `.gitignore` in the current working directory *and any parent directory, up to the root of the repository*, to see what files you want it to ignore.
For CS50 we recommend this [gitignore](https://github.com/CS50Dartmouth21FS1/examples/blob/main/.gitignore) file, and place it in the root directory of every lab starter kit.
This file excludes common patterns, like the `.o` object files produced by the C compiler and `~` backup files produced by emacs.

**You will need to add/extend gitignore files.**
Specifically, you'll need to add the name of any compiled binary file; by doing so, git won't warn you that the file is "untracked" (making your `git status` easier to read) and you won't accidentally add it to your repository.

> The most common mistake is to type `git add .`, which adds every non-ignored file in your current directory and, recursively, all subdirectories.
> Never take this approach!

For example, suppose you are writing a software system with two programs, `server` and `client`.
To keep the code organized, you have two subdirectories, which (sensibly) are also called `server` and `client`.
Here's how you might set up the `.gitignore` files:

```bash
$ cp ~/cs50-dev/shared/examples/.gitignore .gitignore
$ echo server >> server/.gitignore
$ echo client >> client/.gitignore
$ git add .gitignore server/.gitignore client/.gitignore 
$ tree -a
.
|-- .gitignore
|-- README.md
|-- client
|   |-- .gitignore
|   |-- README.md
|   `-- client.c
`-- server
    |-- .gitignore
    |-- README.md
    `-- server.c

2 directories, 8 files
$ 
```

The top-level `.gitignore` will cover the usual patterns that should be ignored throughout, but then each subdirectory lists the files that should be ignored *within that directory*.
I find this approach works well and enables each directory to describe its own needs, more clearly than lumping everything into one top-level gitignore file.

## GitHub

You'll note that all of the above information is based on command-line use of git, even for repositories stored on GitHub.
We recommend this practice for CS50.

Most importantly, however, we urge you **never to edit files on GitHub**, via the browser.
We've seen too many CS50 students create conflicts and confusion this way.
Make all your edits in a local clone, and commit and push in the usual way as described above.
