In this course we work only on Linux.  
But because MacOS is Unix too, you may find yourself wanting to use some of the cool Unix tools at the MacOS commandline.

There are three great commands you should know - they are not on Linux, because they interact with MacOS: `open`, `pbpaste`, `pbcopy`.

The `open` command will open a file for viewing in the relevant MacOS application; for example, for a photo file you might type `open photo.jpg` and see Preview launch and open that file; for an html file you might type `open index.html` and see Safari launch and render that page.

The commands `pbpaste` and `pbcopy` are a great fit into many pipelines.
The first command prints the MacOS 'clipboard' to its standard output, and the second copies its standard input into the MacOS clipboard.
For example; select some text in a window somewhere, then *cmd-C* to copy it to the clipboard, then

```bash
	pbpaste | wc
```
to count the lines, words, and characters in the clipboard.
Or,

```bash
	ls -l | pbcopy
```
saves the directory listing in the clipboard, where you might paste it into Slack or an email message or some document.

I've often used this trick to process a list of email addresses, one per line, to make a semi-colon-separated list:

```bash
	pbpaste | tr \\n \; | pbcopy
```

great!
