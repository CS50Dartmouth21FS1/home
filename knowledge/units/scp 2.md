This unit is a quick tutorial in how to copy files to and from plank.

**[:arrow_forward: Video](https://dartmouth.hosted.panopto.com/Panopto/Pages/Viewer.aspx?id=6819c17e-d2bd-491e-b6f7-acfb01694f84)**

## scp - Secure copy

The `plank` server and other Thayer Linux servers all share the same filesystem - so you can log into any of them, and you'll see the same home directory.

But what if you have a file on your laptop and want to copy it to `plank` -- or vice versa?
The simplest approach is the `scp` (secure copy) command, which should be available at the shell on any MacOS or Linux laptop (maybe Windows too?).

The `scp` command's most common usage is as follows:

```
	scp from to
```

where `from` is the source file and `to` is the destination file.
They could both be a simple pathname, in which case `scp` simply copies files within the local file system, just like `cp`.
But if either one is of the form `username@host:pathname`, referring to the `pathname` on server named `host` after logging into account `username`, `scp` copies the file across the network (in encrypted form).

For example, to copy a file called `somefile` from my Macbook to `plank`, and save it in the `cs50-dev` directory:

```
[MacOS:~]$ scp somefile d31379t@plank.thayer.dartmouth.edu:cs50-dev/somefile
d31379t@plank.thayer.dartmouth.edu's password: 
somefile                                      100%    0     0.0KB/s   00:00    
```

To copy the file `README.md` from my `cs50-dev` directory on `plank` to my Macbook:

```
[MacOS:~]$ scp d31379t@plank.thayer.dartmouth.edu:cs50-dev/README.md README.md
d31379t@plank.thayer.dartmouth.edu's password: 
README.md                                                100% 5357     2.3MB/s   00:00    
```

In these examples, I used the same filename on both sides, but I could have used a different name if desired.

