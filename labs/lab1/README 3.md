D# Lab 1 - Bash

This first lab should get you up to speed working with the command line, basic shell commands, an editor, and a small bash program.

## Background

Johns Hopkins University (JHU) quickly became known as *the* source for reliable data about COVID-19, early in the pandemic.
Their [Coronavirus Resource Center](https://coronavirus.jhu.edu) is an outstanding public resource, including graphics that are routinely seen in news media around the world.
They also provide all their data on GitHub!
One repository is provided by the [Center for Systems Science and Engineering (CSSE)](https://github.com/CSSEGISandData/COVID-19),
and another by the [Centers for Civic Impact](https://github.com/govex/COVID-19).
In this assignment you will use the latter repository, which we have already cloned into `~/cs50-dev/shared/COVID-19`.
(You can clone it onto your laptop, if you wish to play with it there, but the download is more than a gigabyte!)
Note: their data bots update GitHub hourly so, for the record, we cloned our shared copy Tue Mar 30 15:22:01 UTC 2021.

Specifically, we will work with the [US vaccination data](https://github.com/govex/COVID-19/tree/master/data_tables/vaccine_data/us_data).
That page provides documentation describing the data, which appears in two subdirectories.
The data is a spreadsheet formatted as *comma separated values* (csv) -- just a text file, one row of data per line, with the columns of each row separated by a comma.

You need to format your `README.md` file in Markdown syntax, and you will write two bash scripts that print their output in Markdown syntax.
(See the Markdown information and example at bottom of this page.)

## Preparation

Do all your work on the Linux server.

Clone the lab1 starter kit: [Accept the assignment](https://classroom.github.com/a/-1VQMFfJ), and clone the repository to your `cs50-dev` directory on `plank`.
It will look something like this, assuming your GitHub username is XXXXX, and abbreviating the bash prompt to `$`:

```
	$ cd cs50-dev/
	$ git clone git@github.com:CS50Dartmouth21FS1/lab1-XXXXX.git
	Cloning into 'lab1-XXXXX'...
```

*Notice that I started work by changing to my cs50-dev directory.*
The clone step will create a new directory `~/cs50-dev/lab1-XXXXX`.

Finally, `cd lab1-XXXXX` to begin work.

## Assignment

A. Edit the Markdown file `README.md` to add your name and your username.
  Add a `##` subsection header for each of the following tasks.
  For problems B-H, paste your proposed bash command/pipeline (but not its output) after the appropriate header, surrounded by Markdown's "triple-ticks" notation, which looks like this:
  
  
		```bash
		command args...
		```

B. Explore the directory `~/cs50-dev/shared/COVID-19`.
Find the pathname for the `hourly/vaccine_data_us.csv`spreadsheet.
Write a single bash command or pipeline to create a *symbolic link* to that file, within your `lab1-XXXX` directory.
The goal is that, when your current working directory is your `lab1-XXXX` directory, you can type

		less vaccine_data_us.csv

  and see the contents of that shared file.
  Much easier than remembering and typing the long pathname every time!

C. Write a single bash command or pipeline to print only the header line of the spreadsheet.

D. Extend that bash command or pipeline to print only the header line of the spreadsheet, but to present the header fields one per line, instead of a single comma-separated line.
For example, instead of seeing `Name,Class,Major` you would see

		Name
		Class
		Major

E. Write a single bash command or pipeline to print only the "New Hampshire" line(s) of the spreadsheet.

F. Write a single bash command or pipeline to print only the rows representing `All` vaccine types and only the `Province_State` and `Doses_admin` columns, separated by a comma.
  (Note `Province_State` includes some names that are neither a province nor a state; for generality, let's call those 'areas'.)

G. Extend that bash commandline to print the top-10 areas in terms of all types of doses administered, in decreasing order of the number of doses administered.
  Each line of output should contain the area name and the number of doses, separated by a comma.

H. Extend that commandline to edit each output line, adding a pipe (`|`) symbol at the beginning and the end, and replacing the comma with a pipe symbol.
  The result is almost in Markdown 'table' format.

I. Write a bash script `top10.sh` that is given the name of the csv file as its first/only argument, and prints to stdout a table in Markdown format.
  (If the first argument is missing, or names an unreadable file, print nothing but an error message, and exit non-zero.)
  Thus,

		./top10.sh vaccine_data_us.csv > top10.md

  will create a Markdown file `top10.md` from the csv data, adding a header to the output of the previous pipeline so the resulting Markdown file, when viewed in a Markdown-rendering tool, will look like this one:

| Area | Doses administered  |
| :--------- | --------: |
|California|17703550|
|Texas|11130215|
|Florida|9276711|
|New York|9056970|
|Long Term Care (LTC) Program|7718036|
|Pennsylvania|5893502|
|Illinois|5793846|
|Ohio|5168120|
|North Carolina|4654892|
|Michigan|4295979|

Note how I right-justified the numeric column, for readability.
Here is the Markdown for that table:

	| Area | Doses administered  |
	| :--------- | --------: |
	|California|17703550|
	|Texas|11130215|
	|Florida|9276711|
	|New York|9056970|
	|Long Term Care (LTC) Program|7718036|
	|Pennsylvania|5893502|
	|Illinois|5793846|
	|Ohio|5168120|
	|North Carolina|4654892|
	|Michigan|4295979|


J. Now, let's shift gears.
Write a bash script called `summarize.sh` that takes as arguments a list of zero or more pathnames, and prints to stdout a Markdown summary of those files.
Specifically, it should

* for pathnames that do not refer to a readable file, print an error message to stderr and skip to the next pathname.
* ignore files that do not end in `.sh`, `.c`, or `.h`.
* print a `##` subheader for each file, giving that file's basename (the basename is the rightmost component of the pathname)
* print the "header comment" of that file in Markdown's triple-ticks notation appropriate for the language of the file.
  The language is `bash` for a `.sh` file, and `c` for a `.c` or `.h` file.
  See above for an example of triple-ticks for bash.
* the "header comment" is simply the set all lines before the first blank line, if any, except the first line if it starts with `#!`.  (If there is no blank line, print all lines.)
* Your script should print nothing if no arguments are listed.
* Your script should behave properly if a pathname contains spaces or other special characters.
* Your script should have a brief header comment giving the script name, your name, the date, and a short summary of how someone can/should use the script.

> you might test your script like this:
> `./summarize.sh ~/cs50-dev/shared/examples/*`

**Clarification:**
An early version of the specs above had contradictory instructions about exit status and unreadable files.
If your script follows the older spec, and exits cleanly (but with non-zero status) on encountering an argument that does not represent a readable file, we will accept it as correct.

## What to hand in, and how

You should have three files in your `lab1-XXXX` directory:
`README.md`, as edited by you, and the two bash scripts `top10.sh` and `summarize.sh`.
You should add **only** these three files to your repo:

```
git add README.md top10.sh summarize.sh
```

See the [lab-submission instructions](https://www.cs.dartmouth.edu/~cs50/Labs/submit.md) for details of how to submit your lab.

## Hints

You will find some of the following commands useful; use `man cmd` to read about any command.
It's best to run `man` on `plank` (not your Mac, or other Unix machine) so you are sure to get the manual for the version of the command where your script will run.

* `less`
* `cut`
* `head`
* `tail`
* `grep`
* `sort`
* `tr`
* `sed`
* `basename`

`grep` and `sed` depend on *regular expressions*.
It is helpful to remember that `^` anchors a pattern to the start of a line and `$` anchors to the end of the line.

## Markdown example

[Read about Markdown](https://www.cs.dartmouth.edu/~cs50/Logistics/systems.html#markdown), and about [Markdown tables](https://github.com/adam-p/markdown-here/wiki/Markdown-Cheatsheet#tables).
If you want to preview a Markdown file with a desktop app, you'll have to either [scp](https://github.com/CS50Dartmouth21FS1/home/blob/main/knowledge/units/scp.md) the file to your laptop, or copy-paste from your ssh terminal into an empty window in one of those apps.
Another great tool is [HackMD](https://hackmd.io).
We will view your `README.md` on GitHub.com, so make sure it looks good there.

Here's a quick example of a simple Markdown file.

```markdown
	# this is a header
	
	Some normal text goes here.
	Markdown will join and wrap lines where needed.
	
	Use a blank line to indicate a new paragraph.
	
	## this is a subheader
	
	The following is the 'triple-ticks' notation, with optional language specifier to inform Markdown that the contents are bash.
	For best results, put a blank line between text and triple-ticks.
	
	```bash
	$ echo hello world
	hello world
	$
	```
	
	## another subheader
	
	some more text.
	
```
