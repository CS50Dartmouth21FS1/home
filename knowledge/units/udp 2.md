This unit aims

* to introduce [UDP/IP](#udp) as an alternative to TCP/IP
* to write a simple UDP [chat program](#chat)

## <a id="udp">UDP instead of TCP </a>

There is another other important protocol that runs over IP, called the *User Datagram Protocol (UDP).*
It is less common for typical services, but is commonly used for media streaming and for some game servers.

UDP is a *connectionless* protocol, which means that the client never actually 'connects' to the server.
Indeed, the protocol makes no distinction between clients and servers - any UDP socket can be used to send a message (a *datagram*) to any other UDP socket, and any UDP socket can receive a datagram from any other host at any time.
This feature is both a strength and a weakness.

UDP is a *non-reliable* protocol, which means that the datagrams might be lost, duplicated, or reordered, on their way from the sending host to the receiving host.
(In contrast, TCP ensures that the every byte sent by the sender arrives, in order, exactly once.)
This feature is both a strength and a weakness.

Let's rewrite our original example, [client-server](https://github.com/CS50Dartmouth21FS1/examples/blob/main/client-server) with UDP "datagrams" instead of TCP "streams".
See the [client-server-udp](https://github.com/CS50Dartmouth21FS1/examples/blob/main/client-server-udp) example.

Our program still has a client and a server, even though UDP does not impose that structure on us.
Our client no longer uses `connect()` to make a connection to the server.
It simply uses `sendto()` to send a datagram to the server whenever it feels so inclined.
Here, we see that it loops over reading lines from stdin, sending each line to the server using `sendto()`:

```c
  // read a line from stdin (file descriptor = 0) and write to socket
  char *line;
  while ( (line = readlinep(stdin)) != NULL) {
    // send it to the server
    if (sendto(comm_sock, line, strlen(line), 0, 
               (struct sockaddr *) &server, sizeof(server)) < 0) {
      perror("sending in datagram socket");
      exit(6);
    }
    free(line);
  }
```
> We've left out some code above - echoing the message back to the client - and come back to it below.

Our server is also simpler than the TCP server; it does not need to call `listen()` and `accept()`; it simply loops over calls to `recvfrom()` and prints what it receives (and who it came from):

```c
  // Receive datagrams and print them out
  while (true) {
    char buf[BUFSIZE];        // buffer for reading data from socket
    struct sockaddr_in them;  // sender's address
    struct sockaddr *themp = (struct sockaddr *) &them;
    socklen_t themlen = sizeof(them);
    int nbytes = recvfrom(comm_sock, buf, BUFSIZE-1, 0, themp, &themlen);

    if (nbytes < 0) {
      perror("receiving from socket");
      exit(1);
    } else {      
      buf[nbytes] = '\0';     // null terminate string

      // where was it from?
      if (them.sin_family != AF_INET) {
        printf("From non-Internet address: Family %d\n", them.sin_family);
      } else {
        printf("From %s, port %d: '%s'\n", 
               inet_ntoa(them.sin_addr),
               ntohs(them.sin_port), 
               buf);
      }
    }
  }

```
> We've left out some code above - receiving a message back from the server - and come back to it below.

Notice that structure means the server is automatically concurrent - in the sense that it can handle many clients with just this one process.
(For a more complex server we may still need processes or threads, if the act of 'handling' a datagram could take a long time or could block the server on some other activity).

This client-server example actually supports bidirectional communication, by having the server send a message back to the client (right after printing it to the terminal):

```c
      // echo it back to the client
      if (sendto(comm_sock, buf, nbytes, 0, 
                 (struct sockaddr *) &them, sizeof(them)) < 0) {
        perror("sending in datagram socket");
        exit(6);
      }
```

The client waits to receive the message; thus, the client strictly alternates between waiting for input from the terminal and waiting for a message from the server:

```c
    // wait for a response
    char buf[BUFSIZE];        // buffer for reading data from socket
    struct sockaddr_in them;  // sender's address
    struct sockaddr *themp = (struct sockaddr *) &them;
    socklen_t themlen = sizeof(them);
    int nbytes = recvfrom(comm_sock, buf, BUFSIZE-1, 0, themp, &themlen);

    // if a response, print it
    if (nbytes < 0) {
      perror("receiving from socket");
      exit(1);
    } else {      
      buf[nbytes] = '\0';     // null terminate string
      puts(buf);
    }
```

## <a id="chat">UDP chat server</a>

In the above UDP client-server [example](https://github.com/CS50Dartmouth21FS1/examples/blob/main/client-server-udp), the client sends datagrams to a server, and the server echos them back to the client.
For each datagram, the client read a line of input from stdin.

Let's make that echo server into a simple chat application, allowing the user sitting at the server terminal to respond from stdin.

Our first attempt is straightforward; we replace the code that echoed the original datagram with a call to `readlinep()` and send that buffer instead.
From `chatserver1.c` in the new [example](https://github.com/CS50Dartmouth21FS1/examples/blob/main/client-server-udp-select):

```c
      // send a response back to the client
      printf("Response?: ");
      char *response = readlinep(stdin);
      if (response == NULL) {
        break; // on EOF, exit the loop
      }

      if (sendto(comm_sock, response, strlen(response), 0, 
                 (struct sockaddr *) &them, sizeof(them)) < 0) {
        perror("sending in datagram socket");
        exit(6);
      }
      free(response);
```

It works, but the chat is limited to strict back-and-forth chat, one line at a time in each direction.

The server:

```
$ ./udpserver
Ready at port 54136
[127.0.0.1@50655]: hello there!
Response?: good morning!
[127.0.0.1@50655]: how is the weather there?
Response?: about the same as it is where you are.

```

The client is expected to chat first:

```
$ ./udpclient 127.0.0.1 54136
: hello there!
[127.0.0.1@54136]: good morning!
: how is the weather there?
[127.0.0.1@54136]: about the same as it is where you are.
```

All looks to be going well, but then the server person steps away from her terminal, and the client person keeps chatting.
Imagine several seconds going by between each line of input below.

```
: well, goodbye
are you still there?
I have not heard back from you.
What's up?
um, hello?
why will you not talk to me?
```

At this point, the client person gives up and walks away.

Each line results in a datagram sent to the server.
The datagrams pile up there because our server program only listens for, and prints, one datagram at a time, then *blocks* to wait for input on stdin.

The server person comes back and finally sees the first line ("well, goodbye") and types a response.
Of course, the earlier datagram is waiting and immediately gets printed.
And so forth, until they've all been processed.
The resulting conversation is rather confusing to the server person.

```
[127.0.0.1@50655]: well, goodbye
Response?: ok, goodbye.
[127.0.0.1@50655]: are you still there?
Response?: yes
[127.0.0.1@50655]: I have not heard back from you.
Response?: yes you have.
[127.0.0.1@50655]: What's up?
Response?: talking to  you.
[127.0.0.1@50655]: um, hello?
Response?: hello?
[127.0.0.1@50655]: why will you not talk to me?
Response?: I *am* talking to you.
```

The client person is away from the terminal, distressed about why her friend has not replied.
Suddenly, all these replies finally come in.

```
[127.0.0.1@54136]: ok, goodbye.
: [127.0.0.1@54136]: yes
: [127.0.0.1@54136]: yes you have.
: [127.0.0.1@54136]: talking to  you.
: [127.0.0.1@54136]: hello?
: [127.0.0.1@54136]: I *am* talking to you.
```

Clearly, we need a better solution.

### <a id="select">Using select() to wait for input from two sources</a>

The solution is to use the `select()` Unix function, which waits for input to be ready on one or more file descriptors.
We'll ask `select()` to return when either stdin (file descriptor 0) or `comm_sock` has input read.

See `chatclient2.c` and `chatserver2.c` in the new [example](https://github.com/CS50Dartmouth21FS1/examples/blob/main/client-server-udp-select).

It takes a little setup to call `select`, as you'll see at the top of the loop.
But then we check the return value: if negative, there was an error; if zero, there are no file descriptors ready so there must have been a timeout; if positive, one or both of the stdin or the socket must be ready for input.
We check the flags to see which is ready, and handle input from each.

The client-side code looks like this:

```c
  // read from either the socket or stdin, whichever is ready first;
  // if stdin, read a line and send it to the socket;
  // if socket, receive message from socket and write to stdout.
  while (true) {              // loop exits on EOF from stdin
    // for use with select()
    fd_set rfds;              // set of file descriptors we want to read
    
    // Watch stdin (fd 0) and the UDP socket to see when either has input.
    FD_ZERO(&rfds);
    FD_SET(0, &rfds);         // stdin
    FD_SET(comm_sock, &rfds); // the UDP socket
    int nfds = comm_sock+1;   // highest-numbered fd in rfds

    // Wait for input on either source
    int select_response = select(nfds, &rfds, NULL, NULL, NULL);
    // note: 'rfds' updated
    
    if (select_response < 0) {
      // some error occurred
      perror("select()");
      exit(9);
    } else if (select_response == 0) {
      // timeout occurred; this should not happen
    } else if (select_response > 0) {
      // some data is ready on either source, or both

      if (FD_ISSET(0, &rfds)) {
        if (handle_stdin(comm_sock, &them) == EOF) {
          break; // exit loop if EOF on stdin
        }
      }

      if (FD_ISSET(comm_sock, &rfds)) {
        handle_socket(comm_sock, &them);
      }

      // print a fresh prompt
      printf(": ");
      fflush(stdout);
    }
  }
```

We have an almost identical scene in the server:

```c
  // read from either the socket or stdin, whichever is ready first;
  // if stdin, read a line and send it to the socket;
  // if socket, receive message from socket and write to stdout.
  while (true) {              // loop exits on EOF from stdin
    // for use with select()
    fd_set rfds;              // set of file descriptors we want to read
    
    // Watch stdin (fd 0) and the UDP socket to see when either has input.
    FD_ZERO(&rfds);
    FD_SET(0, &rfds);         // stdin
    FD_SET(comm_sock, &rfds); // the UDP socket
    int nfds = comm_sock+1;   // highest-numbered fd in rfds

    // Wait for input on either source
    int select_response = select(nfds, &rfds, NULL, NULL, NULL);
    // note: 'rfds' updated
    
    if (select_response < 0) {
      // some error occurred
      perror("select()");
      exit(9);
    } else if (select_response == 0) {
      // timeout occurred; this should not happen
    } else if (select_response > 0) {
      // some data is ready on either source, or both

      if (FD_ISSET(0, &rfds)) {
        if (handle_stdin(comm_sock, &them) == EOF) {
          break; // exit loop if EOF on stdin
        }
      }
      if (FD_ISSET(comm_sock, &rfds)) {
        handle_socket(comm_sock, &them); // may update 'them'
      }

      // print a fresh prompt
      printf(": ");
      fflush(stdout);
    }
  }
```

Indeed, the client and server are starting to look more and more alike.
The only difference is that the server runs and waits for a client to contact it, whereas the client is provided the address of a server to contact.
(We could make them very symmetric if we had them both bind to a specific port, and gave them each the host/port that they should contact.)

There's a little more complexity in this bidirectional chat.
Because UDP is connectionless, there is always the possibility of datagrams arriving from clients (or servers) other than the expected partner.
In our earlier program, that was fine, and a little fun.
In this program (not shown above), the server prints messages from other clients, and the client prints messages from other servers, but anything typed at stdin always goes to the first partner (ignoring new arrivals).
I made that choice only because the crude interface would just get too complicated if messages came from many places (to which client should I send the lines read from stdin?).
With a little effort, this chatclient and chatserver could support multi-client chatting.

Take a good look at the code for both `chatclient2.c` and `chatserver2.c` in the new [example](https://github.com/CS50Dartmouth21FS1/examples/blob/main/client-server-udp-select) - including the functions I did not include above - to better understand sockets, datagrams, and `select`.
Have fun!

#### Timeouts

Suppose you don't want `select` to block forever until there is input - suppose you need to periodically take care of some other background activity.
You can tell `select` to return after a *timeout* period, even if there is no input on any of the file descriptors.
It looks like this:

```c
    struct timeval timeout;   // how long we're willing to wait
    const struct timeval fivesec = {5,0};   // five seconds
    
    // Watch stdin (fd 0) and the UDP socket to see when either has input.
    FD_ZERO(&rfds);
    FD_SET(0, &rfds);         // stdin
    FD_SET(comm_sock, &rfds); // the UDP socket
    int nfds = comm_sock+1;   // highest-numbered fd in rfds

    // Wait for input on either source, up to five seconds.
    timeout = fivesec;
    int select_response = select(nfds, &rfds, NULL, NULL, &timeout);
    // note: 'rfds' updated, and value of 'timeout' is now undefined
    
    if (select_response < 0) {
      // some error occurred
      perror("select()");
      exit(9);
    } else if (select_response == 0) {
      // timeout occurred; do something
      ...
    } else if (select_response > 0) {
      // some data is ready on either source, or both
      ...
    }
  }
```
