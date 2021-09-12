# Activity - groups and pipelines

Meet your new CS50 team, and Learning Fellow; play with bash pipelines.

## Learning groups

1. Introduce yourself to your new group mates, and to your Learning Fellow.
2. Choose a name for your group.
   It must be suitable as a Git repository name; thus, it should not be too long, and include only the characters `[A-Z][a-z][0-9][-._]`.
   Keep it tasteful, too.
3. (One of you) create a private channel in our Slack workspace, using your group name; invite the others.
   Now you can coordinate your group over Slack!
3. Send me a DM to tell me your new group name (and current breakout room number).
3. Work with your group to solve the following problems.

## Billboard Hot 100

Let's play with the [Billboard Hot 100](https://www.billboard.com/charts/hot-100), which I extracted into a text file [`billboard.tsv`](billboard.tsv).
(`tsv` stands for *tab-separated values*.)
You can grab a copy, into whatever is your current working directory,

```
cp ~/cs50-dev/shared/examples/billboard.tsv .
```

Now, **working in your group (one person sharing screen within the breakout room)**, write pipelines to

1. print the list without the header lines
2. sort the list to start at 100 and countdown to 1
3. print only the list of artists
4. how many artists are in the top 100?
5. which artist had the most hits in top 100?
6. print only the list of titles
7. what's the most common word in a title? (much trickier)

Here "artist" refers to the full string of names; don't worry about picking apart lists of names.

Some useful commands: `cut`, `sed`, `sort`, `uniq`, `wc`, `head`, `tr`


## Where did I get that file?

I used this great [open-source package](https://github.com/guoguo12/billboard-charts);
on my Mac, I followed its instructions to install the package, then

```bash
$ python3
Python 3.9.2 (default, Feb 24 2021, 13:30:36) 
[Clang 12.0.0 (clang-1200.0.32.29)] on darwin
Type "help", "copyright", "credits" or "license" for more information.
>>> import sys
>>> import billboard
>>> sys.stdout = open('billboard.txt','w')
>>> chart = billboard.ChartData('hot-100')
>>> print(chart)
>>> ^D
$ sed -e "s/\. '/\t/" -e "s/' by /\t/" billboard.txt > billboard.tsv
```

That last line uses `sed` to convert the "dot space quote" into a tab, and the "quote space by space" into a tab.
The result is a three-column file (with a two-line header), which should be easier to parse.
