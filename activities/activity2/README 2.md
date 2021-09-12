# Activity 2 - C programming and Git

## Goals

1. To experience use of `mygcc` to compile C programs, to edit a C program, and to write some simple C code.
2. To experience working jointly on a common code base, using Git.

## Prepare

**Pick *one* person in your group to go first**, and [accept the assigment](https://classroom.github.com/g/0slLec6_) on GitHub Classroom.
They will be asked to join an existing group or to create a new group; they should create a new group using your CS50 group name.
Once finished, the rest of you should click through that link and pick the same group name from the list of existing groups.

Clone that repository and make yourself a new branch:

1. Log into plank; `cd cs50-dev`.
2. `git clone` the repository from GitHub Classroom, above.
3. `cd` into the resulting new subdirectory.
4. `git branch xxx`, where `xxx` is short and unique within your team; for example, I would use my nickname `dave`.
5. `git switch xxx` to make that your current branch.

Practice compiling and running the `guess6` program.
Note that `readline.c` must be compiled with it:
  
```
mygcc guess6.c readline.c -o guess6
```

## Individual tasks

Because you each created a new branch within your local clone, and those branches have different names (right?), your edits will remain separate from main, and separate from each other, even when you later push your branch to GitHub.
The branches can each then be merged into the `main` branch.

Divide the following tasks among your group members; groups of 3 people can skip the last task.

1. Modify `main()` to allow the player no more than 10 guesses - after the 10th incorrect guess, it should print the correct answer and exit.
2. Modify `askGuess()` to correct a bug in the range-checking code.
3. Pull `str2int()` out into a new pair of files, `str2int.c` and `str2int.h`, just like I did for `readLine()`.
4. Write a one-line bash script to compile `guess6` from its three components; because you can't use the alias `mygcc` inside a shell script, copy-paste the value of that alias into your script (you can see its value by typing `alias mygcc`).

If you finish first, offer advice to others.

## Merge your work

If you added any files, add them to git:

```bash
git add file...
```

Commit your changes:

```bash
git commit -a -m "...message..."
```

Use a descriptive message like "Fixed the bug in askGuess()".

Push your branch to GitHub; supposing your branch is called `dave`,

```bash
git push origin dave
```

Git will suggest that you issue a *pull request*, that is, a request that someone else 'pull' your changes into the main branch.
One group member should share their screen, visit the repo on GitHub website, and look for pull requests.
Pull each one, review the changes, and merge; then delete the branch.
You can [read more about pull requests](https://docs.github.com/en/github/collaborating-with-issues-and-pull-requests/about-pull-requests).

> If you get a conflict during merging, it may be because someone edited the same lines in the source file.
> We won't have time to go into the details of resolving merge conflicts today.
> You can [read more about resolving merge conflicts](https://docs.github.com/en/github/collaborating-with-issues-and-pull-requests/resolving-a-merge-conflict-on-github).
 
Finally, each of you can go back to main branch:

```bash
git switch main
git pull
```

This will pull all the updated changes down from GitHub to your local clone; to see what happened type `git log`.

Now all your changes are in main!
Test your code.

## Summary

This activity was an abbreviated form of *git flow*, which is the typical way teams use git for parallel development efforts.
In a real setting, with more time, each change would be carefully tested *before* merging it into the main branch.
We'll talk more about git flow in a future knowledge unit or activity, and you'll use it extensively in the final project.
