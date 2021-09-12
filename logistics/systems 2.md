# CS50 Systems

In this course we write all of our code in C and Bash; we develop and run all our code on Linux servers; and we share and manage our code through GitHub repositories.
This page provides some details and links to reference material.

---

## Getting help

If you find yourself stuck or confused about Linux or any of the tools we use, start by perusing this document.
If you can't find what you need, here are some places to look:

* For help with **Dartmouth-supported services** check the 
[Knowledge Portal](https://services.dartmouth.edu/TDClient/1806/Portal/KB/),
which has helpful pages about 
[Canvas](https://services.dartmouth.edu/TDClient/1806/Portal/KB/?CategoryID=15999&SIDs=6799),
[Panopto](https://services.dartmouth.edu/TDClient/1806/Portal/KB/?CategoryID=20104),
[Ed Discussion](https://services.dartmouth.edu/TDClient/1806/Portal/KB/ArticleDet?ID=136531),
[VPN](https://services.dartmouth.edu/TDClient/1806/Portal/KB/?CategoryID=13404),
[Zoom](https://services.dartmouth.edu/TDClient/1806/Portal/KB/?CategoryID=17590),
and more.
If you're really stuck, email [help@dartmouth.edu](mailto:help@dartmouth.edu) or call their helpdesk for personalized help.
(The phone number is at the bottom of every page on the [Knowledge Portal](https://services.dartmouth.edu/TDClient/1806/Portal/KB/).)

* For help using the **Thayer Linux computer systems**, look [here first](https://kb.thayer.dartmouth.edu/article/361-linux-services).
If you don't find an answer there, search the `linux` catagories on our [Ed Discussion](https://edstem.org/us/courses/13476/discussion/) workspace to see if others may have had and resolved your issue or, if not, post a question there.
If neither of those help, then send email to [Thayer Computing Services](mailto:computing@thayer.dartmouth.edu) for support.
(Keep in mind they are only active during normal business hours.)

* For help about **CS50-specific issues**, use [Ed Discussion](syllabus.md#edstem).

## Ed Discussion

This is a [FERPA](https://www2.ed.gov/policy/gen/guid/fpco/ferpa/index.html)-compliant web-based platform we will be using for Q&A and discussion. You can access it directly from our [Canvas](https://canvas.dartmouth.edu/courses/49179) page.

See Dartmouth's [Guide to getting started with Ed Discussion](https://services.dartmouth.edu/TDClient/1806/Portal/KB/ArticleDet?ID=136531) to find out how it works, how to get started, and best practices.

## <a id="linux">Linux systems</a>

You will do all your programming and lab submission on Linux servers at Dartmouth's Thayer School of Engineering.
<!-- @CHANGEME - insert term-specific repo link -->
To set yourself up, follow these [instructions](https://github.com/CS50Dartmouth21FS1/cs50-dev).

The primary CS50 server is called `plank.thayer.dartmouth.edu`; you may also use equivalent servers called `babylonX.thayer.dartmouth.edu`, where `X` is 1 through 8.
(You can check their status [here](https://cluster-usage.thayer.dartmouth.edu).)
Your laptop must first be on the campus network, or on [Dartmouth VPN](https://services.dartmouth.edu/TDClient/1806/Portal/KB/?CategoryID=13404), to access these servers.

**In CS50 we support development only on Thayer's Linux systems.**  

Not MacOS, not Windows, not even other Linux systems.
Linux systems differ in subtle ways; MacOS is a form of Unix different from Linux, and Windows is even more different.  

**Our graders will use `plank` for all testing -- So should you!**.

### Tips for speedier login

1. If you are a Mac or Linux user, you can reduce typing by adding the following to your laptop's `.ssh/config` file (using your own NetID); then you can type `ssh cs50` and only have to enter your password to login.

	```
	Host cs50
	   Hostname plank.thayer.dartmouth.edu
	   User f12345x
	```

On MacOS, this file is `~/.ssh/config`, and you can create or append to it by opening that file in your favorite editor, or as follows (use your appropriate NetID):

```
echo >> ~/.ssh/config <<EOF
Host cs50
   Hostname plank.thayer.dartmouth.edu
   User f12345x
EOF
```

2. Every time you login, it reminds you about whether you are getting close to consuming all your disk-space quota:

	```
	<< You are currently using 47.39M of your 5.00G home directory quota. >>
	```

	That's nice, but it is slow.
	You can turn off this message by creating a file in your home directory ***on plank***.
	Although the mere presence of this file is sufficient, put some text in the file so you can remember why it is there:
	
	```bash
	echo The presence of this file disables login notification of your disk-quota usage. >  ~/.notfsquota
	```
	
	If you later wish to resume monitoring  your disk usage, just remove this file (then logout and login).  For a temporary check, simply `ssh`  to `plank` and run the following command:
	
	```bash
	tfsquota
	```

	There is also a simple [web portal](https://quota-lookup.thayer.dartmouth.edu/) to check your quotas.


## Unix

[Linux](https://en.wikipedia.org/wiki/Linux) is a flavor of the [Unix](https://en.wikipedia.org/wiki/Unix) operating system.
It is likely the most common OS in use today, and the one we use on all CS and Thayer systems.

Definitive documentation for the Unix tools we use can be found in 'man pages' (short for "manual pages") on `plank.thayer.dartmouth.edu`.
For example, to learn the details of the `ls` command, type `man ls`.

> You're welcome to employ your favorite search engine, but beware that there are subtle variations in UNIX tools across its many flavors and distributions.
> What you find online may not work on `plank` Linux.

For a deeper dive, there are many books on UNIX and its variants, including Linux (see below).

 -   [LinkedIn Learning](https://lil.dartmouth.edu) provides free online courses to Dartmouth users; search for Unix or Linux.
 -   *[Linux in a Nutshell](http://www.amazon.com/Linux-Nutshell-Ellen-Siever/dp/0596154488)*
 -   *[Unix, Linux, and variants](http://www.computerhope.com/unix.htm#04)*, free website listing common Unix commands
 -   [Homebrew](https://brew.sh), a tool for installing Unix tools on MacOS. Highly recommended if you want to use Unix tools on MacOS.
 -   *[Bash command line editing](http://www.math.utah.edu/docs/info/features_7.html)*
 -   *[A Practical Guide to Linux Commands, Editors, and Shell Programming](http://www.amazon.com/Practical-Commands-Editors-Programming-Edition/dp/013308504X/ref=sr_1_1?ie=UTF8&qid=1394757793&sr=8-1&keywords=Linux+Commands,+Editors,+and+Shell+Programming+Mark+G.+Sobell),* by Mark G. Sobell; an excellent, comprehensive, hands-on book on Linux and shell programming as well as Python, Perl, and MySQL.
 -   *[The Practice of Programming](http://www.amazon.com/Practice-Programming-Addison-Wesley-Professional-Computing/dp/020161586X/ref=pd_bbs_sr_2?ie=UTF8&s=books&qid=1199226460&sr=1-2),* by Brian W. Kernighan, Rob Pike; a great classic book on design and programming for Unix and C.
 -   *[Beginning Linux Programming, 4th Edition](http://www.amazon.com/Beginning-Linux-Programming-Neil-Matthew-ebook/dp/B004YK0KO8/ref=sr_1_1?s=books&ie=UTF8&qid=1393966477&sr=1-1&keywords=beginning+linux+programming),* by Neil Matthew, Richard Stones; A really good book covering, debugging, processes, threads, and socket programming in clear and easy manner
 -   *[Professional Linux Programming](http://www.amazon.com/Professional-Linux-Programming-Jon-Masters/dp/0471776130/ref=sr_1_cc_2?s=aps&ie=UTF8&qid=1399424116&sr=1-2-catcorr&keywords=masters+and+blum+linux),* by Jon Masters and Richard Blum; an oldie (2007) but a goodie.
 -   *[UNIX Network Programming](http://www.amazon.com/Unix-Network-Programming-Sockets-Networking/dp/0131411551/ref=sr_1_2?ie=UTF8&qid=1329757685&sr=8-2),* by W. Richard Stevens, 2003; Prentice-Hall.
 -   *[Linux System Programming](http://www.amazon.com/Linux-System-Programming-Talking-Directly/dp/1449339530)*


## Bash

When you log into `plank` over ssh, you are running the `bash` shell.
You will need to learn basic `bash` commands for getting around, and for writing scripts.
Here are some good references for bash and bash scripting.

-   [LinkedIn Learning](https://lil.dartmouth.edu) provides free online courses to Dartmouth users; search for Bash.
-   [Short bash tutorial](http://www.panix.com/~elflord/unix/bash-tute.html)
-   [GNU bash reference manual](https://www.gnu.org/software/bash/manual/bash.html)
-   [BASH Programming - Introduction HOW-TO](http://tldp.org/HOWTO/Bash-Prog-Intro-HOWTO.html)
-   [Advanced bash scripting guide](http://tldp.org/LDP/abs/html/)
-   *[Bash Pocket Reference: Help for Power Users and Sys Admins](http://www.amazon.com/Bash-Pocket-Reference-Power-Admins/dp/1491941596),* 2nd Edition, by Arnold Robbins; a short and useful book for about a dozen bucks.
-   [explainshell](http://explainshell.com), a *very cool* website that lets you type a bash command and get feedback on what it's supposed to do.  (It's drawing on the man pages.)
-   [ShellCheck](https://www.shellcheck.net), a bash-script syntax/style checker

## C programming

A vast amount of tutorial and reference material is available online, but there are a few excellent books you may consider:

* *C: A Reference Manual*, fifth edition, by Harbison and Steele, is a definitive reference book.
A copy is available in Baker Library, or you can order it from [Amazon](http://www.amazon.com/Reference-Manual-Samuel-P-Harbison/dp/013089592X/) or [Barnes & Noble](http://www.barnesandnoble.com/w/c-samuel-p-harbison/1002260874?ean=9780130895929).

* *The C Book*, second edition, by Mike Banahan, Declan Brady and Mark Doran, originally published by Addison Wesley in 1991.
This book is available online [here](http://publications.gbdirect.co.uk/c_book).

* *C Programming Language*, by Kernighan and Ritchie, was the  first book on C and is [still in print](http://www.amazon.com/Programming-Language-2nd-Brian-Kernighan/dp/0131103628/ref=sr_1_1?ie=UTF8&qid=1321068335&sr=8-1).
It is owned by most UNIX geeks -- not because it's such a great reference, but, well, just because.

* *Quick reference card*; not a book but a [two-page pdf](http://users.ece.utexas.edu/~adnan/c-refcard.pdf).

Many resources are also available online:

* *[The Pragmatic Programmer, 20th Anniversary Edition](https://pragprog.com/titles/tpp20/the-pragmatic-programmer-20th-anniversary-edition/)*, by Andrew Hunt and David Thomas. 2019.
* [97 things every programmer should know, by Kevlin Henney. O'Reilly 2010.](http://www.amazon.com/Things-Every-Programmer-Should-Know/dp/0596809484/ref=sr_1_2?ie=UTF8&qid=1363813033&sr=8-2&keywords=97+things+every+programmer+should+know)
* *[A First Book of ANSI C, Fourth Edition,](http://www.amazon.com/First-Book-Fourth-Introduction-Programming/dp/1418835560/ref=sr_1_1?s=books&ie=UTF8&qid=1363813105&sr=1-1&keywords=bronson+ansi+c)* [by Gary J. Bronson](http://www.amazon.com/First-Book-Fourth-Introduction-Programming/dp/1418835560/ref=sr_1_1?s=books&ie=UTF8&qid=1363813105&sr=1-1&keywords=bronson+ansi+c); a very good book for learning C
* [The Wikipedia entry for the C programming language](http://en.wikipedia.org/wiki/C_programming_language)
* [The USENET discussion Why is C good?](http://groups.google.com/group/comp.lang.c/browse_frm/thread/1ce1b07f3725e0c7?q=%22Why+is+C+good%3F%3F%3F%22&hl=en&pli=1)
* [LinkedIn Learning](https://lil.dartmouth.edu) provides free online courses to Dartmouth users; search for "C programming".



## Git and GitHub

In CS50 we use the `git`  distributed version control system, with repositories hosted on GitHub, for managing and sharing your source code.
Specifically, we use [GitHub](https://github.com) to manage your development process, submit your labs for grading, and share code with your project team.

**[Set yourself up](github.md) on GitHub**.

For help with [GitHub](https://github.com/), look for the Help item under the top-right menu on any GitHub page, which leads to the extensive [help site](https://docs.github.com/en).

Try  *[Visualizing Git Concepts with D3](https://onlywei.github.io/explain-git-with-d3/)* to experiment with git concepts.

Here are some other useful references for `git`:

- [Git "cheatsheet"](https://www.git-tower.com/blog/git-cheat-sheet)
- [Git Style Guide](https://github.com/agis/git-style-guide), for tips about how to be a stylish git user.
- Free ["Pro Git" book](http://git-scm.com/book) by Scott Chacon, plus the website has LOTS of additional git references, including videos!
- Some interactive tutorials
    - [try.github](http://try.github.io/levels/1/challenges/1)
    - [explain git](https://onlywei.github.io/explain-git-with-d3/)
    - [Visualizing git concepts](https://onlywei.github.io/explain-git-with-d3)
- Some other non-interactive video tutorials
  - [learn version control with git](http://www.git-tower.com/learn/videos)
  - [git tutorial (requires Ruby)](http://gitimmersion.com/index.html)
  - [resolving conflicts](https://help.github.com/articles/resolving-a-merge-conflict-using-the-command-line/)
- A [video game to help with learning git](https://www.rockpapershotgun.com/this-video-game-teaches-you-how-to-use-git-repositories)
- A very slick (and free) GUI for `git` called [SourceTree](https://www.atlassian.com/software/sourcetree) is very popular, as is the excellent licensed (not free) application [Tower<sup>2</sup>.](http://www.git-tower.com/)
- Reference for [common .gitignore files](https://github.com/github/gitignore)
- [What is a "detached HEAD"?](https://www.git-tower.com/learn/git/faq/detached-head-when-checkout-commit) *It's not what it sounds like :-)*
- [How to get yourself out of a real git mess](https://ohshitgit.com) - *warning, foul language*.

**Important note:**
git and GitHub recently changed the default name of the primary branch in new repositories; the conventional default name was `master`, and now it is `main`.
Throughout CS50 we use `main` as the initial, primary branch within our repositories.
However, there are still many online examples, tutorials, and tools that assume `master` is the name of the primary branch.
The functionality has not changed, only the name, so many of the techniques from those examples and tutorials will apply if you adapt them to this name change.

## 'screen' Terminal multiplexer

When you open a Terminal window and ssh to `plank`, you get one window, one shell.
Sometimes you want (or need) multiple windows, and sometimes your connection breaks and you lose everything you were doing.
The program called [screen](https://www.gnu.org/software/screen/) can help!
With `screen` you can have multiple terminal sessions running on `plank` and if you have to disconnect for some reason (e.g., to eat or sleep) when you return those `screen ` sessions will be right where you left them.
This is _really_ handy for man pages, debugging, and more, especially when working remotely!

Thayer Computing Services supports `screen`: read more about it [here](https://kb.thayer.dartmouth.edu/article/361-linux-services), under the heading for *Long-running processes*; ignore all the aspects about `krenew`.
Here's another [tutorial](https://www.geeksforgeeks.org/screen-command-in-linux-with-examples/) about screen; ignore the installation instructions, because it's already installed on plank.

> One catch: if you are an emacs user, screen's use of CTRL-a as a command prefix (e.g., to detach the screen) can be irritating... because emacs uses CTRL-a to move cursor to beginning of line.
> It may be possible to configure `screen` to bind a different character as a command prefix.

## 'tmux' Terminal multiplexer

Another popular terminal multiplexer is called `tmux`; some prefer it over `screen`.
Ham Vocke describes it this way:

> Within one terminal window you can open multiple windows and split-views (called “panes” in tmux lingo).
> Each pane will contain its own, independently running terminal instance.
> This allows you to have multiple terminal commands and applications running visually next to each other without the need to open multiple terminal emulator windows.

- [A quick and easy guide to tmux](https://www.hamvocke.com/blog/a-quick-and-easy-guide-to-tmux/) and
- [A guide to customizing your tmux conf](https://www.hamvocke.com/blog/a-guide-to-customizing-your-tmux-conf/)
- [Nice set of configuration changes](https://github.com/hamvocke/dotfiles/blob/master/tmux/.tmux.conf)
- [All the tmux commands (CTRL-b + …)](https://tmuxguide.readthedocs.io/en/latest/tmux/tmux.html)

> One catch: if you are an emacs user, tmux's use of CTRL-b as a command prefix can be very irritating... because emacs uses CTRL-b for backspace.
> You can rebind tmux's command prefix to a different character, but choose wisely.
> This [online thread](https://superuser.com/questions/1287428/cant-rebind-ctrl-b-to-ctrl-a-in-tmux) hints at the needed config change.

## Editors

You will need to choose an editor for CS50, but we allow for personal choice.

We recommend `vim` or `emacs`, which are nearly always available on Unix systems.
The only editors that reliably work remotely over an `ssh` connection to a remote computer are `vim` (or `vi`) and `emacs`.
Other tools provide the illusion of editing server-housed files on your laptop.

**Vim**: `vim` is nothing fancy; it just works.
It is a "modal" editor (derived from `vi`) with many single-character commands that, when mastered, allow you to edit very quickly.  

  - ["Learn to use a real editor"](http://www.tonimueller.org/blog/posts/learn_to_use_a_real_editor_or_vim_tips_and_tricks/)
  - [vim tutorial](https://danielmiessler.com/study/vim/)
  - [An Extremely Quick and Simple Introduction to the Vi Text Editor](http://heather.cs.ucdavis.edu/~matloff/UnixAndC/Editors/ViIntro.html)

**Emacs**: `emacs` is powerful, extensible, and customizable.
Once you master the basic keystrokes, you can edit very quickly -- and even recompile your program from inside emacs.
See the [emacs tutorial](http://www2.lib.uchicago.edu/keith/tcl-course/emacs-tutorial.html)
and [emacs quick reference guide](https://www.gnu.org/software/emacs/refcards/pdf/refcard.pdf).

[**:arrow_forward: Video tour of vim and emacs.**](https://dartmouth.hosted.panopto.com/Panopto/Pages/Viewer.aspx?id=b333eba9-ab2e-4567-a1be-acfb00ec383e)

There are some free full-screen editors you can download on your laptop:  

**Atom**: [Atom](https://atom.io)  has great full-screen functionality, runs everywhere, and has a large collection of extensions.

**Sublime**: the [Sublime Text editor](https://www.sublimetext.com) can be used for free (as long as you don't mind declining the periodic requests to pay).
It is also highly extensible using Python & JSON.
See this great [tutorial](http://code.tutsplus.com/articles/perfect-workflow-in-sublime-text-free-course--net-27293).

**VScode**: Microsoft's [Visual Studio Code](https://code.visualstudio.com) is another great full-screen editor, runs everywhere, and also has a large collection of extensions.
See [Getting started with Visual Studio Code](https://code.visualstudio.com/docs/introvideos/basics).
But, **important warning:**
change the default location of the vscode database to reside on the local disk instead of your home directory.
To do this, go into `Settings>Remote>Extensions>C/C++` and change the value of `C_Cpp.default.browse.databaseFilename` to:
`/tmp/vscode-${USER}/${workspaceFolderBasename}/browse.vc.db`
You can also do this manually by sshing to any Thayer system and creating/editing `~/.vscode-server/data/Machine/setting.json` to add this parameter:

	"C_Cpp.default.browse.databaseFilename": "/tmp/vscode-${USER}/${workspaceFolderBasename}/browse.vc.db" 

> TL;DR: from the nice Thayer Computing folks:
"The problem appears to be with the [IntelliSense](https://code.visualstudio.com/docs/editor/intellisense) database that the C/C++ Extension uses. When this is first run, it trolls all system libraries and caches all the symbols it finds. This, although it is resource-intensive, is a one-time-per-launch activity and happens reasonably quickly. However, the extension then seems to read and write the database it creates constantly. Since this is, by default, located in a user's nfs home directory, it creates thousands of small reads and writes per second, resulting in a backup of system calls and increase in load average. If enough users use this, it will overwhelm the system and cause a denial of service."

### <a id="set-editor">Setting your favorite EDITOR</a>

Once you have settled on a preferred editor, you should make that information available to apps that might open an editor for you.
(One common example: `git commit` opens an editor for you to review a list of files to commit, and to type a commit message.)
Even if you normally use a remote editor (like vscode) you should choose an editor (vim or emacs) for occasions when you need to make edits directly on plank.

The most universal method is to define the `EDITOR` shell variable.
We placed some examples in the file `~/cs50-dev/dotfiles/profile.cs50`, but you need to uncomment one of the lines to select the editor you prefer.
Changes to this file take effect the next time you login; to effect them immediately, you can `source ~/cs50-dev/dotfiles/profile.cs50`.

Optionally, you can set a preferred editor specifically for git; for example, `git config --global core.editor emacs` (or vim).
If set, git will ignore `EDITOR` and use this setting.
We recommend the universal approach because many applications check `$EDITOR`, not just git.

## Whiteboarding

For some team activities, particularly the final project, it may be helpful to create a shared 'whiteboard' where you can draw diagrams.

* Zoom has a built-in whiteboard feature, but remember to screen-capture the board before the breakout rooms close!
* [Google Jamboard](https://jamboard.google.com) allows you to create (and save) shared whiteboard drawings.

If you have other suggestions, let the Professor know so this list can grow.

## Markdown

We use Markdown in CS50 for all documentation; it is also the standard for all documentation in GitHub.

A file with extension `.md` is assumed to be a text file in 'Markdown' syntax, which provides very simple (and readable) markup for headings, lists, italics, bold, hyperlinks, code snippets, and embedded images.
(This course website is written in Markdown and rendered with Jekyll.) Many source-code web portals (like GitHub) allow you to browse the files in your repository - automatically rendering any that are in Markdown format, making such files much nicer to look at than plain-text files.

You can edit Markdown files using any text editor, but there are some specialized editors available.
Markdown is easy to learn and there are many useful resources online.
Many CS50 students/groups have found [HackMD](https://hackmd.io) to be very useful for collaborating on Markdown documents.  

*Keep in mind, however, that Markdown syntax is not yet standardized, so each tool may render Markdown slightly differently; in CS50 we standardize on GitHub's flavor of Markdown.*

Tutorials:

 - GitHub's *[Mastering Markdown](https://guides.github.com/features/mastering-markdown/)* tutorial.
 - GitHub's *[Markdown cheatsheet](https://github.com/adam-p/markdown-here/wiki/Markdown-Cheatsheet).*
 - Brett Terpstra's *[Write better Markdown](http://brettterpstra.com/2015/08/24/write-better-markdown/)*, which recognizes that many flavors of Markdown exist.

Editors:

 - [HackMD](http://hackmd.io), a web-based Markdown editor for "live" editing of Markdown with a group.
 - [Markoff](https://robots.thoughtbot.com/markoff-free-markdown-previewer), a MacOS application for previewing Markdown files.
 - [Macdown](https://github.com/MacDownApp/macdown), a MacOS application for editing Markdown files.
 - [Typora](https://typora.io) is a nice Markdown editor for Mac and Windows.  

## valgrind

[valgrind](http://valgrind.org/) is a family of tools for dynamic analysis of programs (i.e., while the prpogram is running). In CS50 we use it to watch for memory leaks and related errors.
See also the [quick guide to valgrind](https://web.stanford.edu/class/archive/cs/cs107/cs107.1174/guide_valgrind.html).

## gdb

We recommend and support the use of the `gdb` debugging tool in CS50, and you may be expected to use it in later CS courses.
For details, see [the GDB manual](https://www.gnu.org/software/gdb/documentation/)
and this excellent [quick reference card](http://users.ece.utexas.edu/~adnan/gdb-refcard.pdf).

## make

[GNU make](http://www.gnu.org/software/make/manual/make.html) is a utility for determining what needs to be done (e.g., compile, link, copy, etc.) to build a program or other target, and then issues the commands to do it.

## X windows

We do not require or support X11 in CS50.
If you want to run windowing (not Windows&trade; itself) or graphical applications on the Linux servers, but allow them to pop open windows on your Mac or Windows laptop, we recommend [FastX](https://kb.thayer.dartmouth.edu/article/435-fastx).
