In the [fork unit](https://github.com/CS50Dartmouth21FS1/home/blob/main/knowledge/units/fork.md), we saw how an HTTP server could fork (copy itself) to create a *process* to serve each inbound client.

One unfortunate side-effect of forking a *copy* of the parent server process is that the parent and child have no means to communicate after they've forked.
Any data structures created by the parent are duplicated in the child.
Any changes the child makes to those data structures are made on the child's *copy*, and not seen by the parent.

Perhaps that's good.
But if the goal of the server is to manage some kind of shared state - like a game server to which clients provide updates and from which the server updates the clients - the server really needs a way to handle multiple clients simultaneously *and* manage a shared data structure.

One solution: use *threads* instead of *processes*.
The concept is similar - each thread has a separate flow of control through the server code - but different, because all the threads operate in the same copy of data memory.
Thus, they all have access to the same data structures.

There are specific libraries that support the creation of new threads, and for means to coordinate thread access to shared data structures.
We won't have time in this course to look into those details.

> This rest of this knowledge unit was developed several years ago and not updated for recent iterations of the course.

## Why threads?

From YoLinux:

> The POSIX thread libraries are a standards based thread API for C/C++.
It allows one to spawn a new concurrent process flow.
It is most effective on multi-processor or multi-core systems where the process flow can be scheduled to run on another processor thus gaining speed through parallel or distributed processing.
>
> Threads require less overhead than "forking" or spawning a new process because the system does not initialize a new system virtual memory space and environment for the process.
>
> While most effective on a multiprocessor or multi-core systems, gains are also found on uniprocessor systems which exploit latency in I/O and other system functions which may halt process execution.
(i.e., one thread may execute while another is waiting for I/O or some other system latency.) Parallel programming technologies such as MPI and PVM are used in a distributed computing environment while threads are limited to a single computer system.
All threads within a process share the same address space.
A thread is spawned by defining a function and its arguments which will be processed in the thread.
The purpose of using the POSIX thread library in your software is to execute software faster.

## What's *POSIX*?

It's not a Dr. Seuss character !

POSIX is an acronym for "Portable Operating System Interface [for Unix]" which is the name of a set of related standards specified by the IEEE.

## What's a thread?

What is a thread? Well we have studied the forking of process to support concurrency.
Threads are units of control that execute within the context of a single process representing multiple strands of indepenent execution.

What is the difference between forking processes and threads?
Well typically when a process is forked it executes as new independent process with its own PID and a copy of the code and resources of the parent process.
It is scheduled by the OS as a independent process.
A process has a single thread by default called `main()`.
Threads running in a process gettheir own stack and run concurrently and have access to process state such as open files, global variables, etc.

In this unit, we will just look at a number of simple examples of code to illustrate how threads are created and how we can implement mutual exclusion using `mutex` for shared resouces.
These notes are not meant to be exhaustive - they are not.

For a in depth look at pthreads read the following tutorial - it may help answer questions that you may have not covered in the class: [POSIX Threads Programming, Blaise Barney, Lawrence Livermore National Laboratory](https://computing.llnl.gov/tutorials/pthreads/)

Also, type `man pthread` for information on syntax, etc.

### Thread Creation

You have to write C code to create a thread.
If you do it right, when it runs that C code will create a new thread of execution that runs at the same time!
Consider the simple example ([print_i.c](https://github.com/CS50Dartmouth21FS1/examples/blob/main/threads/print_i.c)), and its output:

```bash
$ ./print_i
1
2
2
3
3
4
4
5
5
6
6
^C
$ 
```

### Unpredictability

You cannot always predict the exact sequence of execution of threads running in parallel.
Lots of factors affect how often and long a thread actually gets to execute: the amount of real memory, number of processors and/or cores, and what the thread's code is trying to do (e.g., I/O, computation, system call, etc.), among others.

In the example [random.c](https://github.com/CS50Dartmouth21FS1/examples/blob/main/threads/random.c), what gets output first?
We do not know what order the values will be written out.
If the main thread completes before the `print_i()` thread has executed then it will die.
Here's the output from several runs:

```bash
$ ./random
c

$ ./random
c

$ ./random
c

$ ./random
c
a
a
b

$ ./random
c
a
a
b

$ ./random
c

$ 
```

### Functions that are not *thread safe*

Most (but not all) of the libraries you get with `gcc` are thread safe.
This means that regardless of how many threads might be executing the very same function's code,

-   the code still works as expected,
-   the threads cannot interact or interfere with each other,
-   if there is any data shared by all the threads, that data can only ever be accessed by one thread at a time

This is usually accomplished by ensuring all the variables used by the thread are stored on its stack (rather than global variables).
If there is shared data, access to that shared data is typically serialized using some sort of mutual exclusion mechanism (more on this later).

Consider the output of the [unsafe.c](https://github.com/CS50Dartmouth21FS1/examples/blob/main/threads/unsafe.c) example, first with the `c = NULL` commented out, and then with it not commented out.

```bash
# comment out the "c = NULL;" line
$ vi unsafe.c

$ mygcc -o unsafe unsafe.c -lpthread

$ ./unsafe
last_letter ( a is abcdefg and i is 5)
last_letter ( a is xyz and i is 2)
z
g
Ended nicely this time

# uncomment the "c = NULL;" line
$ vi unsafe.c

$ mygcc -o unsafe unsafe.c -lpthread

$ ./unsafe
last_letter ( a is abcdefg and i is 5)
last_letter ( a is xyz and i is 2)
Segmentation fault (core dumped)

$ 
```

Can you see what happened?

### Mutual Exclusion (mutex) locks

Mutual Exclusion (mutex) is a mechanism to help manage concurrency in programs.
You can think of a *mutex* as a lock that controls when a process can enter specific sections of code.
There are two functions:

-   `pthread_mutex_lock(mutex)`:

    If the mutex is locked, this function will block until the lock is unlocked; then, it will lock the mutex.  On return, the mutex is locked.

-   `pthread_mutex_unlock(mutex)`:

    If the mutex is locked by some prior `pthread_mutex_lock()` function, this function will unlock the mutex; otherwise it does nothing.

In the example [mutex.c](https://github.com/CS50Dartmouth21FS1/examples/blob/main/threads/mutex.c), our goal is to ensure that at any time there is only one thread calling function `print()`.
Here's the output, first with the mutex commented out, and then with the mutex uncommented.

```bash
# mutex commented out
$ mygcc -o mutex mutex.c -lpthread

$ ./mutex
1: I am
1: I am
2:  in i
2:  in j

$ vi mutex.c
# mutex uncommented

$ mygcc -o mutex mutex.c -lpthread

$ ./mutex
1: I am
2:  in i
1: I am
2:  in j
$ 
```

### Deadlocks

Of course, you have to be careful with mutex - you could end up in a deadlock!
See the [deadlock.c](https://github.com/CS50Dartmouth21FS1/examples/blob/main/threads/deadlock.c) example.

Here's the output with the `sleep(1)` commented out, and then with the `sleep(1)` uncommented.

```bash
# sleep(1)'s commented out
$ mygcc -o deadlock deadlock.c

$ ./deadlock
I am in i
I am in j

# sleep(1)'s uncommented
$ mygcc -o deadlock deadlock.c

$ ./deadlock
$ 
```
