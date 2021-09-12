This unit covers the following:

-   The Unix operating system;
-   The concept of a *shell*;
-   The concept of a command line;
-   Logging on to a Unix machine;
-   Looking at the home directory and its files;
-   Some simple shell commands;
-   Logging out.

This and subsequent units are not a detailed presentation of the Unix OS and its programming tools -- it would require a whole term to cover all that material in detail.
We cover just enough about Unix and its tools to navigate our way around the system, write some basic shell scripts, and use its programming tools.

You'll note that we stick to the basics - we use a standard shell, we work at the command line, we use standard text-oriented editors.
We do not use any IDE (integrated development environment) or GUI (graphical user interface).
Why?
Because we believe it is important for you to understand the lowest common denominator, on which everything else is built, because you may need to work at this level someday.
Indeed, work on many embedded systems limits you to text-oriented, command-line interfaces only.
It's a good skill to have.

## OS and the shell

Unix is an *operating system*: "system software that manages computer hardware, software resources, and provides common services for computer programs." [Wikipedia](https://en.wikipedia.org/wiki/Operating_system)
In essence, it is the software that lays directly on top of the hardware, providing abstractions (like files and folders) to allow programmers to conveniently write applications.

> Unix arose in the 1960s; many derivatives came later, including Linux, FreeBSD, and MacOS.
> Except when it *really* matters, I use the term 'Unix' to refer to them all.

In their book *Program Design in the Unix Environment* (1984), Rob Pike and Brian Kernighan described the Unix philosophy:

> Much of the power of the Unix operating system comes from a style of program design that makes programs easy to use and, more important, easy to combine with other programs.
[...] This style was based on the use of tools: using programs separately or in combination to get a job done, rather than doing it by hand, by monolithic self-sufficient subsystems, or by special-purpose, one-time programs.

The core of the Unix operating system is called the *kernel*; the earliest human interface to the operating system was thus a program called the *shell* [[Wikipedia](https://en.wikipedia.org/wiki/Shell_(computing))].
Indeed, every Unix programmer needs to be comfortable working at the shell prompt, the *command line*.

## The command line

Unix was originally developed for computers with hardwired 'terminals', each of which was basically an electronic typewriter - a printer with a keyboard.
There were no graphical displays, and the concepts of 'windows' and 'mouse' had not yet been invented.
To interact with the computer, the user types on the keyboard, and the computer echos the keystrokes on the paper.
The system is controlled by typing *commands*, most of which print results for the user to see.
The interaction occurs over the *command line*.

Modern Unix systems support rich graphical user interfaces, but under the hood they all support the command line.

On my Mac I can launch 'Terminal', an application that emulates a traditional hardware terminal; it launches the *shell* -- a program that serves as my interface to the underlying MacOS (Unix) kernel.
The shell begins by printing a prompt, leaving my cursor at the end of the line so I can type a command.

```bash
[MacOS:~]$ 
```

Your prompt may look different - there are different shells and shells allow users to customize their prompts.
You'll see a few formats in the examples below.
Traditionally, shells like bash (used here) end their prompt with `$`.

But we're going to immediately jump off the laptop and over to Linux.

## CS50 Linux

In CS50 you will connect over the Internet to a set of Linux servers hosted at the Thayer School.
Before continuing, follow these [instructions](https://github.com/CS50Dartmouth21FS1/home/blob/main/logistics/systems.md#linux) to set up your Linux account... so you can experiment as you read the following.

**[:arrow_forward: Video: What do Linux servers look like?](https://dartmouth.hosted.panopto.com/Panopto/Pages/Viewer.aspx?id=d1217102-8db8-4f4c-a112-acfa00aebe7d)**

## Logging into a remote machine using ssh

**[:arrow_forward: Video: Logging in and viewing files](https://dartmouth.hosted.panopto.com/Panopto/Pages/Viewer.aspx?id=a9e2f6c3-998b-49bc-89e5-acfb0160e426)**

We can log into Thayer's Linux servers over the Internet, using the **s**ecure **sh**ell (`ssh`) command.
The `ssh` command establishes an encrypted connection between your laptop and the other server.

Below, I remotely log in as user `d31379t` (my Dartmouth NetID) to the server named `plank` by giving its full Internet hostname - `plank.thayer.dartmouth.edu`.

```
[MacOS:~]$ ssh d31379t@plank.thayer.dartmouth.edu
d31379t@plank.thayer.dartmouth.edu's password: 

  << You are currently using 4.01M of your 5.00G home directory quota. >>

d31379t@plank:~$ echo hello class!
hello class!
d31379t@plank:~$ logout
Connection to plank.thayer.dartmouth.edu closed.
[MacOS:~]$ 
```

It asked for my password, which is my usual Dartmouth password.

> If your connection failed, or timed out, double check that your computer is on the Dartmouth VPN or physically on the Dartmouth network.

The `echo` command takes an optional list of arguments, and simply prints them out.
Here I gave it two arguments.

I then used the command `logout` to leave, or "log out of" the `plank` system.

> It is good practice to `exit` or `logout` of any shell window, rather than just closing the window... you may need to properly exit background jobs or unsaved changes.
> The shell will warn you if you have left any background jobs running.

Let's log back in.
Notice how it sometimes pauses quite a while after entering your password.

```
[MacOS:~]$ ssh d31379t@plank.thayer.dartmouth.edu
d31379t@plank.thayer.dartmouth.edu's password: 

  << You are currently using 4.01M of your 5.00G home directory quota. >>

d31379t@plank:~$  echo The presence of this file disables login notification of your disk-quota usage. >  .notfsquota
d31379t@plank:~$ 
```

That pause was due to plank computing my disk utilization.
To save time, I created a new file `.notfsquota` which, when present, causes the login procedure to skip that quota calculation.
(I'm still subject to the quota, but I really don't care to see it every time I log in!)
Here, the `>` redirects the output of `echo` to the file named next; if that file does not exist, it is created; if it exists, it is overwritten.
We can see the contents of that file with the `cat` command:

```bash
d31379t@plank:~$ cat .notfsquota
The presence of this file disables login notification of your disk-quota usage.
d31379t@plank:~$ 
```

Why `cat`?
It is short for 'concatenate', because it prints each of the files listed in its arguments, one after another.
Here, of course, just one file.

Most commands quickly produce some output and then exit.
Some will run too long - perhaps printing too much output; you can stop (kill) the command, forcing it to exit, by typing control-C `^C` at the keyboard.
One silly program, `yes`, just prints an infinite sequence of `y` characters until you kill it:

```bash
d31379t@plank:~$ yes
y
y
y
^C
d31379t@plank:~$ 
```

Some commands ask for your input, and continue to read input until they read an "end of file" (EOF); if they are reading from your keyboard, you can cause the program to detect an EOF by typing control-D (`^D`) at the beginning of an input line.
Below I typed three lines of text, the (`^D`) at the start of the sixth input line, to the `sort` program:

```bash
d31379t@plank:~$ sort
sort
dartmouth
brown
yale
harvard
princeton
^D
brown
dartmouth
princetonsort
harvard
princeton
yale
d31379t@plank:~$ 

```

After sort reached EOF it sorted the input it had received and printed the sorted list.

Notice the difference between `^C` and `^D`; the former kills the program immediately, whereas the latter causes it to detect EOF when it reaches that point in reading input from the keyboard.


## Viewing files

As we saw above, you can print the contents of any file with the `cat` command, so named because it concatenates all the files listed as arguments, printing one after the other.
For very long files, though, the output will quickly scroll off your terminal.

So someone developed a program called `more`, which would fill the screen, then wait for you to press the space bar when you wanted to see more (get it?).

That was great, but someone else thought they could make an even better program, and they called it `less` (because less is more, get it?).

The syntax is `less filename` and `more filename`.
In each case you press space to see another screenfull, and `q` to quit early.
Take a look at the `man` pages to get the details of each.

Similarly, `head` and `tail` display a number of lines (selectable via switches, of course) at the beginning and end of a file, respectively.

See what these do: `cat /etc/passwd`, `head /etc/passwd`, `tail /etc/passwd`, `more /etc/passwd`, and `less /etc/passwd`.
The file `/etc/passwd` lists many, if not all, of the accounts on the system, and information about each account.

## Editing files

Long before there were windows and graphical displays, or even screens, there were text editors.
Two text editors are in common use on Unix system today: `emacs` and `vi`.
Actually, there is an expanded/improved version of `vi` called `vim`, which is quite popular.

You should try both and become comfortable with at least one.
Yes, it's tempting to use an external graphical editor (like Sublime), but there are times when you *must* use a text-only editor and thus you should get used to it.

See [about editors](https://github.com/CS50Dartmouth21FS1/home/blob/main/logistics/systems.md#editors) for some resources that can help you learn `emacs` or `vim`.
