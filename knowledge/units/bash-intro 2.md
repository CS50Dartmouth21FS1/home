In this unit we get an early feel for the shell.

**[:arrow_forward: Video](https://dartmouth.hosted.panopto.com/Panopto/Pages/Viewer.aspx?id=ad2d4e9b-521a-4ce1-ba72-acfb0163a642)**

## The shell

The shell provides an interface between the user and the kernel and executes programs called 'commands'.
For example, if a user enters `ls `then the shell executes the `ls` command, which actually executes a program stored in the file `/bin/ls`.
The shell can also execute other programs including scripts (text files interpreted by a program like python or bash) and compiled programs (e.g., written in C and compiled into binary code).
Even your own programs -- once marked 'executable' -- become commands you can run from the shell!

Although Unix (and the shell) have hundreds of commands, don't let yourself be overwhelmed; you will probably need only a couple dozen of them by the end of the term.

Unix has often been criticized for being very terse (it's rumored that its designers were bad typists).
Many commands have short, cryptic names and vowels are a rarity:

    cat, cut, cp, cd, chmod, echo, find, grep, ls, mv, rm, tr, sed, comm, vim

We will learn to use all of these commands and more.

Unix command output is also very terse - the default action on success is silence.
Only errors are reported, and error messages are often terse.
Unix commands are often termed 'tools' or 'utilities', because they are meant to be simple tools that you can combine in novel ways.

The shell interprets the first word on each command line as the *command*, and the remaining words as *arguments* to that command.
Arguments are separated by white space (spaces and tabs).
Many commands can be executed with or without arguments.
Others require arguments, or a certain number of arguments, (e.g., `cp sort.c anothersort.c`) to work correctly.

Any arguments are provided to the command as an array of words.
The first entry in the array is the command name itself.
In our `cp sort.c anothersort.c` example, argument 0 would be `cp`, argument 1 is `sort.c`, and argument 2 is  `anothersort.c`.
(This will matter to you when you start writing commands.)

Each command interprets its arguments according to its own rules.
One common convention is for the argument list to begin with a list of *options* (also known as *switches* or *flags*); these options are either a single letter preceded by a hyphen (e.g., `-v`) or a word preceded by two hyphens (e.g., `--verbose`).

For example, let's use the `ls` command and the `-l` (dash ell) option to list in long format the file `filename.c`.

    ls -l filename.c

Switches are often single characters preceded by a hyphen (e.g., `-l`).
Most commands accept switches in any order, though they generally must appear before all the real arguments (usually filenames).
In the case of the `ls` example below, the command arguments represent file or directory names.

Some commands also accept their switches grouped together.
For example, the following switches to `ls` are identical:

    ls -tla foople*
    ...
    ls -t -l -a foople*

As another example, the format of an `ssh` command line looks like this:

	ssh [options] [user@]hostname [command] [argument]...

I use the command `ssh d31379t@plank.thayer.dartmouth.edu` to indicate the user and host.

Note how I've described the syntax of the `ssh` command.
Those `[ ]` brackets and `...` are not literally part of the command - you never type them!
The `[ ]` are notation used to denote optional words; in the ssh syntax above, note the optional

    [command]

while the things outside of the `[ ]` , like `hostname`, must be specified.
Similarly, the `...` notation means that the prior word can optionally repeat.

Every command is different; these are just conventions.
You need to look at the manual for details.

## Getting Information using the online manual (man)

If you want the detailed syntax of a Unix command, read its page in the manual.
The Unix command `man` (short for *manual*) will find and print the 'man page' for the given command named in its argument; for example, `man ssh` produced the following:  

```man
SSH(1)                    BSD General Commands Manual                   SSH(1)

NAME
     ssh — OpenSSH SSH client (remote login program)

SYNOPSIS
     ssh [-46AaCfGgKkMNnqsTtVvXxYy] [-b bind_address] [-c cipher_spec]
         [-D [bind_address:]port] [-E log_file] [-e escape_char]
         [-F configfile] [-I pkcs11] [-i identity_file]
         [-J [user@]host[:port]] [-L address] [-l login_name] [-m mac_spec]
         [-O ctl_cmd] [-o option] [-p port] [-Q query_option] [-R address]
         [-S ctl_path] [-W host:port] [-w local_tun[:remote_tun]]
         [user@]hostname [command]

DESCRIPTION
     ssh (SSH client) is a program for logging into a remote machine and for
     executing commands on a remote machine.  It is intended to provide secure
     encrypted communications between two untrusted hosts over an insecure
     network.  X11 connections, arbitrary TCP ports and UNIX-domain sockets
     can also be forwarded over the secure channel.

     ssh connects and logs into the specified hostname (with optional user
     name).  The user must prove his/her identity to the remote machine using
     one of several methods (see below).

     If command is specified, it is executed on the remote host instead of a
     login shell.

     The options are as follows:

     -4      Forces ssh to use IPv4 addresses only.

     -6      Forces ssh to use IPv6 addresses only.

     -A      Enables forwarding of the authentication agent connection.  This
             can also be specified on a per-host basis in a configuration
             file.

 st

... and a whole lot more
```

This is just a snippet of the `man ssh` output.
The manual output includes all the nitty gritty details on options and about the command.
For many commands you can use the common option `--help` (two hyphens) to get a brief breakdown of the command and its switches.
This doesn't work for all commands (including `ssh`, interestingly), but in that case the use of `--help` is interpreted as an invalid command-line by ssh and it lists of the options anyway.

You can also use `man` to print information about common library functions, which are listed in different sections of the manual.
From the manual page for `man` (printed with command `man man`), we see there are 9 sections:

```bash
$ man man
...
      The table below shows the section numbers of the manual followed by  the  types
      of pages they contain.

      1   Executable programs or shell commands
      2   System calls (functions provided by the kernel)
      3   Library calls (functions within program libraries)
      4   Special files (usually found in /dev)
      5   File formats and conventions eg /etc/passwd
      6   Games
      7   Miscellaneous  (including  macro  packages  and  conventions), e.g. man(7),
          groff(7)
      8   System administration commands (usually only for root)
      9   Kernel routines [Non standard]

```

By default, `man` looks in section 1.
If you give a section number as the first argument, `man` will search that section instead.
Thus, `man printf` or `man 1 printf` will print the man page for the `printf` command (from section 1),
whereas `man 3 printf` will print information about the `printf` library function (from section 3).

> Caveat: You may be tempted to search for documentation about Unix commands (or library functions) online.
> You will certainly find useful material – and even man pages – but be warned: the details vary across Unix flavors, software versions, and even installations, so the local man pages are the definitive source of information.

Although the `man` command pauses at the end of each screenfull:

```man
Manual page xxx(n) line 1 (press h for help or q to quit)
```

If you enter '`h`' to see the help you will find many more commands than you're likely to ever use when reading `man` pages.
This is because the man-page reader is actually the `less` command of Unix.
I tend to use only a few:

* `f` or *space* (the spacebar) advances to the next screenful,
* `b` (or Page Up key) goes back to the previous screenful,
* `e` or down-arrow advances one more line,
* `y` or up-arrow goes back one line,
* `/` allows one to type a search phrase and hit return,
* `q` quits `man`, and returns to the shell prompt.


## The Shell's 'path'

**[:arrow_forward: Video](https://dartmouth.hosted.panopto.com/Panopto/Pages/Viewer.aspx?id=143b59d3-971b-4359-9726-acfb0164ccac)**

Typically, the shell processes the complete line after a carriage return is entered (by the user hitting the *Enter* or *Return* key) and then goes off to find the program that the command line specified.
If the command is a pathname, whether relative (e.g., `./mycommand`) or absolute (e.g., `/bin/ls` or `~/cs50-dev/mycommand`), the shell simply executes the program in that file.
If the command is not a pathname, the shell searches through a list of directories in your "path", which is defined by the shell variable called `PATH`.

The shell allows one to define *variables*; some have special meanings.
The `PATH` variable must be defined, with a colon-separated list of pathnames where commands can be found.
Take a look at your `PATH` by asking the shell to substitute its value (`$PATH`) and pass it as an argument to the `echo` command:

```bash
d31379t@plank:~$ echo $PATH
/thayerfs/home/d31379t/bin:/dartfs-hpc/admin/opt/el7/intel/compilers_and_libraries_2019.3.199/linux/bin/intel64:/usr/local/sbin:/usr/local/bin:/usr/sbin:/usr/bin:/sbin:/bin:/usr/games:/usr/local/games:/snap/bin:/thayerfs/apps/other:/thayerfs/apps/abaqus/bin:/thayerfs/apps/ansys/current/Framework/bin/Linux64:/thayerfs/apps/ansys/current/fluent/bin:/thayerfs/apps/cadence/bin:/thayerfs/apps/comsol/bin:/thayerfs/apps/eclipse:/thayerfs/apps/fiji:/thayerfs/apps/gams:/thayerfs/apps/idl/bin:/thayerfs/apps/julia/bin:/thayerfs/apps/maple/bin:/thayerfs/apps/mathematica/bin:/thayerfs/apps/matlab/bin:/thayerfs/apps/maya/bin:/thayerfs/apps/netgen/bin:/thayerfs/apps/paraview/bin:/thayerfs/apps/sagemath:/thayerfs/apps/synopsys/pcmstudio/bin:/thayerfs/apps/synopsys/sentaurus/bin:/thayerfs/apps/tecplot/bin:/thayerfs/apps/totalview/bin:/thayerfs/apps/turbovnc/opt/TurboVNC/bin:/thayerfs/apps/visit/bin:/thayerfs/apps/xilinx/bin
d31379t@plank:~$ 
```

> Wow! Thayer really puts a lot into the Shell's search path.

Where do the `sort` and `ls` commands reside?
Let's use another command to find out.

```bash
d31379t@plank:~$ which sort
sort is /usr/bin/sort
d31379t@plank:~$ which ls
ls is aliased to `ls -F --color=auto'
ls is /bin/ls
d31379t@plank:~$ 
```

We see that the `sort` command is actually a program stored in the file `/usr/bin/sort`.

The `ls` command, however, is actually a shell "alias".
The shell allows users to define "aliases", which act just like commands but are actually just a textual substitution of a command name (the alias) to some other string (in this case, `ls -F --color=auto`).
Thus, any time I type `ls blah blah`, it treats it as if I had typed `ls -F --color=auto blah blah`.
The `-F` option tells `ls` to add a trailing symbol to some names in its output; it adds a `/` to the names of directories, a `@` to the names of symbolic links (um, that's another conversation), and some other even specialized cases.
The `--color=auto` tells `ls` to print files of different types in different colors.

Of course, the shell then still needs to resolve `ls`.
It then searches the `PATH` to find an executable file with that name; in this case, it is found in `/bin`.
(If it found several, the shell will execute the first one, because it is found first in the PATH.)
Below you can see the effect of running `ls` (the alias) and `/bin/ls` (the raw command, without the `-F`).

```bash
d31379t@plank:~$ ls
bin/  cs50-dev/  dot@  go@  lib@  projects/  scripts@  staff/
d31379t@plank:~$ /bin/ls
bin  cs50-dev  dot  go	lib  projects  scripts	staff
d31379t@plank:~$ 
```

(I can't show the coloration here; try it yourself!)
