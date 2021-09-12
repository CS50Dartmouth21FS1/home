This unit aims

* to allow our server to serve multiple clients, concurrently
* to understand Unix [processes](#processes) - and how to fork them
* to acknowledge an alternative to processes: [threads](#threads)

## Our HTTP server

Recall our [client-server-http](https://github.com/CS50Dartmouth21FS1/examples/blob/main/client-server-http) example, in which we explored simple HTTP client and server programs.
The client was able to connect to real web servers, and the server was able to serve real web browsers, and of course our client could connect to our server.

The catch?
Any client connecting to our server will command our server's full attention until the client provides at least one line of input - and, if the first line is a valid `GET` command, until the client finally provides a blank line.
Our web server can be blocked by *any* program from *anywhere* in the Internet that simply opens a connection to the server.
No other clients can be served!

We need to enable our server to serve each client *simultaneously*, at whatever pace each client is prepared to move.
The solution: 'fork' a copy of the server for each new client!

## <a id="forking">A forking web server</a>

Take a look at the new [client-server-http-fork](https://github.com/CS50Dartmouth21FS1/examples/blob/main/client-server-http-fork) example.
The client is exactly the same.
The server is just a bit different.

In this new server, right after accepting a new client we arrange for the server process to 'fork' into two identical processes: the only difference between *parent* and *child* process is in the return value from the `fork()` system call.

```c
  // Start accepting connections, and allow one connection at a time
  listen(list_sock, LISTEN_BACKLOG);
  while (true) {
    // accept connection and receive communication socket (file descriptor)
    int comm_sock = accept(list_sock, 0, 0);
    if (comm_sock == -1) {
      perror("accept");
    } else {
      // start a new process to handle the new client
      if (fork()) {
        // parent process
        close(comm_sock);
      } else {
        // child process
        close(list_sock);

        printf("Connection started\n");
...
        printf("Connection ended\n");
        exit(0);
      }
    }

    // reap any zombies
    while (waitpid(0, NULL, WNOHANG) > 0) {
      ;
    }
  }
```

The action is right in the middle, where our server process calls `fork()`.
Upon return from that call, there are *two* processes running this same code, each with a totally independent *copy* of the memory, and with *all* the same file descriptors open.
Each checks the value returned from `fork()`.

The *parent process* (the original server) receives a non-zero return value.
It immediately closes the brand-new socket connected to the new client, and moves on.

The *child process* (a new copy of the server) receives a zero return value.
It immediately closes the older listening socket, for which it has no use, and proceeds to handle the client exactly as we had before.
When finished, it calls `exit(0)`, ending its life as a process.

## <a id="processes">Processes</a>

A *process* is a running *program*.
You write a program, compile it, and save the result in an *executable file*.
That file just sits there until somebody runs the program.
When the program is run, e.g., from the shell command line, Unix creates a new *process* with a copy of that program's code and allows it to run.
The `fork()` function call clones a running process, causing Unix to create a new process with a *copy* of all of the existing process's state; the new process does not start from scratch.

The `ps` command prints a list of our current processes.

```bash
$ ls
Makefile	file.h		httpclient.c	httpserver.c
README.md	file.o		httpclient.o	httpserver.o
file.c		httpclient*	httpserver*
$ ps -o pid,ppid,state,comm
  PID  PPID STAT COMM
39382 39381 S    -tcsh
40546 39382 S+   man
40547 40546 S+   sh
40548 40547 S+   sh
40551 40548 S+   sh
40552 40551 S+   /usr/bin/less
40368 40367 S    -tcsh
40531 40368 S    bash
$ 
```

Each line shows the *process identifier* (pid), which is an integer incremented for each new process on the system; the *parent process identifier* (ppid), which identifies the process that forked that process, the *process status* (see below), and the *command* running in that process.

I have a few shells open, and a `man` running `less`, but nothing else of interest.

Now I'll run `./httpserver` in different window, and check `ps` again.
To focus our attention, I'll grep for `http`:

```bash
$ ps -o pid,ppid,state,comm | grep http
40590 40531 S+   ./httpserver
$
```

Processes are typically either Running (R), Sleeping(S), Waiting for I/O(D), Stopped(T), or defunct (Z) (a.k.a., a *Zombie*).
Everything we see above is Sleeping (S), waiting on some input.

Next I'll run `./httpclient` with suitable parameters; it finishes quickly.
After it's done, let's `ps`:

```c
$ ps -o pid,ppid,state,comm | grep http
40590 40531 S+   ./httpserver
40661 40590 Z+   (httpserver)
$ 
```

Huh?  now there are two `httpserver` processes, the second a child of the first, but that's surprising because I thought the child should have exited.
It did indeed exit (and die) -- but its parent has not yet noticed.
The child is now a *zombie* (status `Z`), taking up space in the system process table even though it is no longer running.
It stays there until the parent calls `wait`, or until the parent dies.
If we don't do something about it, a lot of zombies can accumulate.
Eventually, the entire system grinds to a halt, because the system's internal process table is full.
(Don't do this to your classmates!)

## <a id="zombies">Zombies</a>

A responsible program will clean up after itself.
Notice the loop at the bottom of the main `while` loop, outside the `if` statement that splits parent and child:

```c
    // reap any zombies
    while (waitpid(0, NULL, WNOHANG) > 0) {
      ;
    }
```

Because it is after the `if` statement, only the parent process runs this code.
The function `waitpid()` call causes the caller to wait until one of its child processes exits, and return some status information.
The first parameter (`0`) means that we are interested in hearing about the death of any child.
The second parameter (`NULL`) indicates that we don't actually care about why it died, or its exit status.
The third parameter (`WNOHANG`) indicates that we don't actually want to 'wait' for any children to die - just return immediately if all our children are still alive.
