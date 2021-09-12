In this unit we explore the Unix file system a little.

**[:arrow_forward: Video](https://dartmouth.hosted.panopto.com/Panopto/Pages/Viewer.aspx?id=057b21b7-6055-40f1-b990-acfb0165dbc7)**

## Files and directories

The Unix file system is a *hierarchical file system*.
The file system consists of a very small number of different file *types*.
The two most common types are *files* and *directories*.

A directory (akin to a folder on a MacOS or Windows computer) contains the names and locations of all files and directories below it.
A directory always contains two special files `.` (dot) and `..` (dot dot); `.` represents the directory itself, and `..` represents the directory's parent.
In the following, I make a new directory, change my current working directory to be that new directory, create a new file in that directory, and use `ls` to explore the contents of the new directory and its parent.

```bash
d31379t@plank:~/cs50-dev$ ls
dotfiles/  README.md  shared@
d31379t@plank:~/cs50-dev$ mkdir demo
d31379t@plank:~/cs50-dev$ cd demo
d31379t@plank:~/cs50-dev/demo$ echo this is a new file > README
d31379t@plank:~/cs50-dev/demo$ ls
README
d31379t@plank:~/cs50-dev/demo$ cat README
this is a new file
d31379t@plank:~/cs50-dev/demo$ cd ..
d31379t@plank:~/cs50-dev$ ls 
dotfiles/  demo/  README.md  shared@
d31379t@plank:~/cs50-dev$ ls demo
README
d31379t@plank:~/cs50-dev$ ls -al demo
total 39
drwxr-xr-x 2 d31379t thayerusers  24 Mar 27 18:10 ./
drwxr-xr-x 5 d31379t thayerusers 121 Mar 27 18:10 ../
-rw-r--r-- 1 d31379t thayerusers  19 Mar 27 18:10 README
d31379t@plank:~/cs50-dev$ 
```

The `-al` flags to `ls` ask for the long-format listing (`-l`) and to show all files (`-a`); more on that below.

Directory names are separated by a *forward* slash `/`, forming pathnames.
A *pathname* is a filename that includes some or all of the directories leading to the file; an *absolute pathname* is relative to the root (`/`) directory and begins with a `/`, in the first example below, or begins with `~` (shorthand for the home directory).
A *relative pathname* is relative to the current working directory.
Notice that a relative pathname can also use `.` or `..`, as in the final examples below.


```bash
[d31379t@plank:~/cs50-dev$ ls /thayerfs/home/d31379t
bin/  cs50-dev/  dot@  go@  lib@  projects/  scripts@  staff/
d31379t@plank:~/cs50-dev$ ls /thayerfs/home/d31379t/cs50-dev
dotfiles/  demo/  README.md  shared@
d31379t@plank:~/cs50-dev$ ls ~
bin/  cs50-dev/  dot@  go@  lib@  projects/  scripts@  staff/
d31379t@plank:~/cs50-dev$ ls ~/cs50-dev
dotfiles/  demo/  README.md  shared@
d31379t@plank:~/cs50-dev$ ls ~/cs50-dev/demo
README
d31379t@plank:~/cs50-dev$ ls demo
README
d31379t@plank:~/cs50-dev$ ls ./demo
README
d31379t@plank:~/cs50-dev$ ls ../cs50-dev/demo
README
d31379t@plank:~/cs50-dev$ 
```

You can always "go home" by simply typing `cd ~` or even just `cd`, either of which change your current working directory to home.

### <a id="basename">basename and dirname</a>

When writing scripts, you sometimes have a pathname and want to break it into the directory-name component (dirname) and file-name component (basename).
Fortunately there are two commands to help:

```bash
$ pathname=~/cs50-dev/shared/examples/.gitignore
$ echo "$pathname"
/thayerfs/home/d31379t/cs50-dev/shared/examples/.gitignore
$ dirname "$pathname"
/thayerfs/home/d31379t/cs50-dev/shared/examples
$ basename "$pathname"
gitignore
$ 
```

Notice the shell expanded the `~` to the full pathname for my home directory when making the initial variable assignment.

The `dirname` and `basename` commands can be useful in a script to extract the relevant portion of the pathname into another variable, e.g., 

```bash
$ dir=$(dirname "$pathname")
$ file=$(basename "$pathname")
```
(here we use command substitution, described further below.)

## Listing and globbing files

Here are a popular set of switches you can use with `ls`:

    -l list in long format (as we have been doing)
    -a list all entries (including `dot` files, which are normally hidden)
    -t sort by modification time (latest first)
    -r list in reverse order (alphabetical or time)
    -R list the directory and its subdirectories recursively


The shell also interprets certain special characters:
`*` matches zero or more characters,
`?` matches one character, and
`[]` matches one character from the set (or range) of characters listed within the brackets.
This behavior is called *globbing*; the shell actually replaces what you write with a list of matching filenames.

Here are some examples:

```bash
d31379t@plank:~/cs50-dev/demo$ ls
game    game.o    README     test2.txt  test4.txt  test6.txt  test8.txt
game.c  Makefile  test1.txt  test3.txt  test5.txt  test7.txt  test9.txt
d31379t@plank:~/cs50-dev/demo$ ls game*
game  game.c  game.o
d31379t@plank:~/cs50-dev/demo$ ls game.*
game.c  game.o
d31379t@plank:~/cs50-dev/demo$ ls test*txt
test1.txt  test3.txt  test5.txt  test7.txt  test9.txt
test2.txt  test4.txt  test6.txt  test8.txt
d31379t@plank:~/cs50-dev/demo$ ls test?.txt
test1.txt  test3.txt  test5.txt  test7.txt  test9.txt
test2.txt  test4.txt  test6.txt  test8.txt
d31379t@plank:~/cs50-dev/demo$ ls test[2468].*
test2.txt  test4.txt  test6.txt  test8.txt
d31379t@plank:~/cs50-dev/demo$ ls test[7-9].*
test7.txt  test8.txt  test9.txt
d31379t@plank:~/cs50-dev/demo$ ls *.*
game.c  test1.txt  test3.txt  test5.txt  test7.txt  test9.txt
game.o  test2.txt  test4.txt  test6.txt  test8.txt
d31379t@plank:~/cs50-dev/demo$ echo [a-z]*    
game game.c game.o test1.txt test2.txt test3.txt test4.txt test5.txt test6.txt test7.txt test8.txt test9.txt
d31379t@plank:~/cs50-dev/demo$ echo [A-Z]*
Makefile README
d31379t@plank:~/cs50-dev/demo$ 
```

### Hidden files
The `ls` program normally does not list any files whose filename begins with `.`  There is nothing special about these files, except `.` and `..`, as far as Unix is concerned.
It's simply a convention - files whose names begin with `.` are to be considered 'hidden', and thus not listed by `ls` or matched with by the shell's `*` globbing character.
Home directories, in particular, include many 'hidden' (but important!) files.
The `-a` switch tells `ls` to list "all" files, including those that begin with a dot (aka, the hidden files).

```bash
d31379t@plank:~$ ls -a
./             .cache/    .gitconfig         .muttrc@     .signature@
../            cs50-dev/  .gitconfig-fancy@  .notfsquota  .ssh/
.bash_history  .cshrc@    .gnupg/            .plan@       staff/
.bash_logout   dot@       go@                .profile     .subversion@
.bashrc        .emacs     lib@               projects/    .vimrc
bin/           .emacs.d/  .login@            scripts@
```

In this case, `ls` sorted them alphanumerically without regard to the leading dot.
In some other Unix or `ls` implementations, it lists all the "dot files" first, then all the rest.

Again, it appends `/` to directory names.
It appends an `@` to indicate the file is actually a *symbolic link*, meaning, a pointer to a file that actually lives elsewhere.

Here's an important example:

```bash
d31379t@plank:~/cs50-dev$ ls
dotfiles/  demo/  README.md  shared@
d31379t@plank:~/cs50-dev$ ls -l shared
lrwxrwxrwx 1 d31379t thayerusers 34 Mar 27 14:56 shared -> /thayerfs/courses/21spring/cosc050/
d31379t@plank:~/cs50-dev$ ls shared/
examples/
d31379t@plank:~/cs50-dev$ ls shared/examples/
args.sh*   guess1a.sh*  guess2.sh*  guess4.sh*     shifter.sh*
gitignore  guess1b.sh*  guess3.sh*  imagepage.sh*
d31379t@plank:~/cs50-dev$ 
```

Notice how `ls -l` printed `->` and the name of the target for the `shared` symlink.
Because it has a trailing `/` slash, we can see it is a directory.
When we `ls shared/` (adding the slash), `ls` shows the contents of that directory, which happens to be one subdirectory `examples`.
That's where we provide CS50 examples!
(Just one is there so far.)
No matter where your current working directory, you can always see the examples:

```bash
d31379t@plank:~$ ls ~/cs50-dev/shared/examples/
args.sh*   guess1a.sh*  guess2.sh*  guess4.sh*     shifter.sh*
gitignore  guess1b.sh*  guess3.sh*  imagepage.sh*
d31379t@plank:~$ 
```

> How did I create that symbolic link (aka symlink)?
> When my current working directory was `cs50-dev`, I typed the command
> `ln -s /thayerfs/courses/21spring/cosc050 shared`.
> The parameters for `ln` are
> `-s` to indicate a *symbolic* link,
> the first pathname is the target of the link,
> and the second pathname is the (new) link itself.

Back to the home directory.
To see just the dot files, let's get clever with the shell's glob characters:

```bash
d31379t@plank:~$ ls -ad .??*
.bash_history  .cshrc@     .gitconfig-fancy@  .notfsquota  .ssh/
.bash_logout   .emacs      .gnupg/            .plan@       .subversion@
.bashrc        .emacs.d/   .login@            .profile     .vimrc
.cache/        .gitconfig  .muttrc@           .signature@
d31379t@plank:~$ 
```

All of these "dot files" (or "dot directories") are important to one program or another; you are likely to have the following:

* `.bash_history` - used by bash to record a history of the commands you've typed
* `.bash_logout` - executed by bash when you log out
* `.bashrc` - executed by bash whenever you log in or start a new shell
* `.emacs.d/` - a directory used by emacs text editor
* `.profile` - executed by bash when you log in
* `.ssh/` - directory used by ssh, including your private key
* `.vim/` - a directory used by vim text editor
* `.viminfo` - used by vim text editor
* `.vimrc` - used by vim text editor

The bash history file is really handy.
After you run a command, bash stores it in the history file.
If you want to run it again, you can just type the up-arrow (or `⌃p`) and the shell will retype the same command again, but not hit enter.
You can move the cursor around and edit the command, and hit enter to run it.  If you type two up-arrows (or two `⌃p`), you'll get two commands ago, and so on.


## Your home directory and its files

Each user has a designated _home directory_.
Immediately after you log in using `ssh` you are in your home directory - that is, the shell's notion of your 'current working directory'
is your home directory.

We can look at our home directory pathname using the `pwd` (**p**rint **w**orking **d**irectory) command.
Let's see what we have inside our Linux box.

```bash
d31379t@plank:~$ pwd
/thayerfs/home/d31379t
d31379t@plank:~$ ls
bin/  cs50-dev/  dot@  go@  lib@  projects/  scripts@  staff/
d31379t@plank:~$ 
```

The tilde (`~`) above is shorthand for 'home'.
My home directory is `/thayerfs/home/d31379t`.

Above, the `ls` lists several things within my home directory.
Some I see to be subdirectories, because `ls` added `/` to their names.
What's in the `cs50-dev` subdirectory?

```bash
d31379t@plank:~$ ls cs50-dev
dotfiles/  README.md  shared@
d31379t@plank:~$ 
```

Notice how I used an argument to `ls` to tell it which directory to list.
I see that `cs50-dev` has a subdirectory `dotfiles`, a file `README.md`, and a *symbolic link* called `shared` -- it's like an alias.
Let's go down into that subdirectory and look more closely.

```bash
d31379t@plank:~$ cd cs50-dev
d31379t@plank:~/cs50-dev$ pwd
/thayerfs/home/d31379t/cs50-dev
d31379t@plank:~/cs50-dev$ ls
dotfiles/  README.md  shared@
d31379t@plank:~/cs50-dev$ ls -l
total 79
drwxr-xr-x 2 d31379t thayerusers  159 Mar 27 14:57 dotfiles/
-rw-r--r-- 1 d31379t thayerusers 5357 Mar 27 14:56 README.md
lrwxrwxrwx 1 d31379t thayerusers   34 Mar 27 14:56 shared -> /thayerfs/courses/21spring/cosc050/
d31379t@plank:~/cs50-dev$ 
```

I changed directory (`cd`) so now my *current working directory* is `cs50-dev`; note that `pwd` printed its longer pathname, and the shell nicely updated its prompt to remind us of our current directory.
When I run plain `ls` again, it listed what is now the current directory.
Then, I asked for more detail by using the `-l` (dash ell) switch, which means 'long' format.
It provided more information about each file and directory.
For example, the second line says `README.md` has permissions `-rw-r--r--`, owner `cs50 `, group `thayerusers`, size 5357 bytes, and name `README.md`.

`ls` shows directories with `d` leading their permissions and with a trailing slash `/` after their name.

You need appropriate permissions to be able to change to a directory, list its contents, or read/write/create its files; for example, here I try to list another user's home directory:

```bash
d31379t@plank:~$ ls /thayerfs/home/f002tzm
ls: cannot open directory '/thayerfs/home/f002tzm': Permission denied
d31379t@plank:~$ 
```

### Bash shell startup files

The `bash` shell looks for several files in your home directory:

* `.profile` - executed by bash when you log in
* `.bashrc` - executed by bash whenever you start a new shell
* `.bash_logout` - executed by bash when you log out

and it creates/updates another file

* `.bash_history` - used by bash to record a history of the commands you've typed

The `.bashrc` file is especially important, because `bash` reads it every time you start a new `bash` shell, that is, when you log in, when you start a new interactive shell, or when you run a new bash script.
(In contrast, `.profile` is only read when you login.)
In each case,`bash` reads the files and executes the commands therein.
Thus, you can configure your `bash` experience by having it declare some variables, define some aliases, and set up some personal favorites.

The default `.bashrc` file is pretty good.
For CS50 we asked you to add one line to that file:

```
source ~/cs50-dev/dotfiles/bashrc.cs50
```

That command tells bash to read the given file, which provides the following customizations:

```bash
# aliases used for cs50 development
alias mygcc='gcc -Wall -pedantic -std=c11 -ggdb'
alias myvalgrind='valgrind --leak-check=full --show-leak-kinds=all'

# aliases for safety
alias rm='rm -i'
alias cp='cp -i'
alias mv='mv -i'

# aliases for convenience
alias ls='ls -F --color=auto'
alias mkdir='mkdir -p'
alias which='type -all'
```

Notice that `#` indicates a comment in bash.

The `mygcc` alias adds some extra options to `gcc`, the C compiler.

The `myvalgrind` alias adds some extra options to `valgrind`, which we'll cover later in the term.

The "safety" aliases protect you from accidentally deleting or overwriting files.

The "convenience" aliases give you some handy shortcuts and make the output of some commands more useful.


## Locating files

Many times you want to find a file but do not know where it is in the directory tree (Unix directory structure is a tree - rooted at the `/` directory) .
The `find` command can walk a file hierarchy:

```bash
d31379t@plank:~$ find cs50-dev
cs50-dev/
cs50-dev/README.md
cs50-dev/demo
cs50-dev/demo/game.c
cs50-dev/demo/test9.txt
cs50-dev/demo/test1.txt
cs50-dev/demo/test7.txt
cs50-dev/demo/test6.txt
cs50-dev/demo/game
cs50-dev/demo/game.o
cs50-dev/demo/Makefile
cs50-dev/demo/test3.txt
cs50-dev/demo/README
cs50-dev/demo/test5.txt
cs50-dev/demo/test4.txt
cs50-dev/demo/test8.txt
cs50-dev/demo/test2.txt
cs50-dev/shared
cs50-dev/dotfiles
cs50-dev/dotfiles/README.md
cs50-dev/dotfiles/profile.cs50
cs50-dev/dotfiles/gitconfig
cs50-dev/dotfiles/emacs
cs50-dev/dotfiles/vimrc
cs50-dev/dotfiles/bashrc.cs50
cs50-dev/.git
```

and it goes on, because `cs50-dev` is a git repository and git hides a lot of files and directories under `.git`.

Indeed, what other git repos do I have?

```bash
d31379t@plank:~$ find . -name .git
./staff/.git
./cs50-dev/.git
./projects/kotz-bin-lib/.git
./projects/metahashcheck/.git
d31379t@plank:~$ 
```

and where might I have Markdown files?
Markdown file names (by convention) end in `.md`.

```bash
d31379t@plank:~$ find . -name \*.md
./staff/README.md
./staff/moss_scripts/README.md
./staff/grading_scripts/README.md
./staff/go/README.md
./cs50-dev/README.md
./cs50-dev/dotfiles/README.md
./projects/kotz-bin-lib/README.md
./projects/metahashcheck/README.md
d31379t@plank:~$ 
```

This example uses a wildcard `*` to print pathnames of files whose name matches a pattern; the backslash `\` is there to prevent the shell from interpreting the `*`, allowing it to be part of the argument to `find`, which interprets that character itself.

Let's just list the directories (type 'd') under `cs50-dev`:

```bash
d31379t@plank:~$ find cs50-dev/ -type d
cs50-dev/
cs50-dev/demo
cs50-dev/dotfiles
cs50-dev/.git
cs50-dev/.git/branches
cs50-dev/.git/info
cs50-dev/.git/refs
cs50-dev/.git/refs/heads
cs50-dev/.git/refs/remotes
cs50-dev/.git/refs/remotes/origin
cs50-dev/.git/refs/tags
cs50-dev/.git/logs
cs50-dev/.git/logs/refs
cs50-dev/.git/logs/refs/heads
cs50-dev/.git/logs/refs/remotes
cs50-dev/.git/logs/refs/remotes/origin
cs50-dev/.git/hooks
cs50-dev/.git/objects
cs50-dev/.git/objects/pack
cs50-dev/.git/objects/info
d31379t@plank:~$ 
```


## File type

Unix itself imposes almost no constraints or interpretation on the contents of files - the only common case is that of a compiled, executable program: it has to be in a very specific binary format for the operating system (Unix) to execute it.
All other files are used by some program or another, and it's up to those programs to interpret the contents as they see fit.
The great power of Unix, and the common shell commands, is that any file can be read by any program; the most common format are plain-text (ASCII) files that are formatted as a series of "lines" delimited by "newline" characters (`\n`, known by its ASCII code 012).

Unix does not interpret the file *name* in any way, although some programs do.
For example, the C compiler expects to find C code in `foo.c` and compiled object code in `foo.o`.
You might think a file with a name ending in `.txt` is a text file, or ending in `.py` is a Python script, but those are just conventions.


If you are unsure about the contents of a file (text, binary, compressed, Unix executabe, some format specific a certain application, etc.), the `file` command is useful; it makes an attempt to judge the format of the file.
For example, here's what it thinks of all the files in my solution to Lab 4:

```bash
d31379t@plank:~/labs-private/tse/crawler$ ls
crawler*   crawler.o  IMPLEMENTATION.md  README.md        testing.sh
crawler.c  DESIGN.md  Makefile           REQUIREMENTS.md  valgrind.sh
d31379t@plank:~/labs-private/tse/crawler$ file *
crawler:           ELF 64-bit LSB shared object, x86-64, version 1 (SYSV), dynamically linked, interpreter /lib64/ld-linux-x86-64.so.2, for GNU/Linux 3.2.0, BuildID[sha1]=e86cbbdd6a3e803576c87fdfe2d070edaa4b95a7, with debug_info, not stripped
crawler.c:         C source, ASCII text
crawler.o:         ELF 64-bit LSB relocatable, x86-64, version 1 (SYSV), with debug_info, not stripped
DESIGN.md:         UTF-8 Unicode text
IMPLEMENTATION.md: ASCII text, with very long lines
Makefile:          makefile script, ASCII text
README.md:         ASCII text
REQUIREMENTS.md:   UTF-8 Unicode text, with very long lines
testing.sh:        ASCII text
valgrind.sh:       ASCII text
d31379t@plank:~/labs-private/tse/crawler$ 
```
