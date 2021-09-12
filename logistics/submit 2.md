# How to submit your lab

This page provides instructions for submitting labs 1-6 and the Project.

## Use good Git practice throughout development

Periodically commit and push your changes to save a version of your code:

* **Add** all required files using an appropriate `git add` command-line.
* **Commit** all changes using an appropriate `git commit` command-line.
* **Push** all changes using an appropriate `git push` command-line.

You should *commit* a logical group of files whenever you reach a clean stopping place, even if not all functionality is yet written or tested.
You should *push* at least once a day; this serves as a sort of backup.

## <a id="submit">Submit when ready</a>

For example, consider the moment when you are ready for final submission of Lab1:

```bash
git commit file1 file2...  # commit all the files necessary for this lab
git push origin main       # push branch 'main' to the remote 'origin'.
git branch submit1         # create a new branch 'submit1'
git push origin submit1    # push branch 'submit1' to the remote.
```

Of course, you should adjust for the lab number (1..6) you are submitting;
for Lab 2, the branch name should be `submit2`, and so forth.

If this is your first push of this repo, git may remind you to

```bash
git push --set-upstream origin main
```

If you get an error about git being unable to open your editor, make sure you have [configured a default editor](https://github.com/CS50Dartmouth21FS1/home/blob/main/logistics/systems.md#set-editor).

**Important:** Check to ensure you have no [missing](#missing) files and no [extraneous](#extra) files.

We will clone your repo at 0h, 24h, 48h, and 72h after the original deadline.
We will start grading when we first see a branch `submit1`, and judge it late if that branch appeared after the deadline.
**Important:** we will be looking for `submit1` (or 2,3,4,5,6 according to the lab), not `submit` or `Submit1` or `submitted` or anything else.

## <a id="resubmit">Resubmit if absolutely necessary</a>

**If you need to update your submission,** simply merge the new content into the `submit1` branch.
*Be careful! don't just copy-paste the below commands; pick the right branch for the lab you mean to update.*

```bash
git commit file1 file2...    # commit whatever files you need to commit
git push origin main         # push commits to branch 'main' on GitHub remote
git switch submit1           # switch to the 'submit1' branch
git merge main               # merge the 'main' branch into the 'submit1' branch
git push origin submit1      # push commits to branch 'submit1' on GitHub remote
git switch main              # switch back to the 'main' branch (important!)
```

Use your web browser to view your remote on GitHub and make sure you see the new commit(s) on the `submit1` branch and the expected changes appear in the expected files.

**If you make such a change after the deadline**, please send the lead grader (Songyun Tao) a DM on Slack to let us know.
This will reduce the potential for confusion.

## <a id="missing">Do I have any files missing?</a>

Sometimes people don't realize they've committed and pushed only a subset of the files needed.
Here are two suggestions that can help ensure you've committed *and* pushed everything your teammates (or the graders!) will need.

**Be cautious.**
Run `git status` to have it print out the set of files that are staged, tracked but not staged, and not tracked.
If any of these should be committed, you'd better address those!
Even better, ask Git to show you everything it ignores, too:

	git status --ignored

And double-check that Git is not overlooking something critical.

**Be extra careful.**
Use your web browser to view your remote on GitHub and make sure there is a branch `submit1` (or whatever lab # it is) and it includes everything we'll need to grade your lab -- and nothing else.

> On the main page of your repo (in GitHub) you should see a button-shaped menu with the label 'main'; click on it and pick the relevant 'submit' branch.

**Be totally paranoid.**
Let's go a step further and fetch a fresh clone.
Visit GitHub in your browser and copy the git-clone string like `git@github.com:cs50spring2021/lab-yourname.git`; then make a fresh clone:

```bash
cd ~/cs50-dev
#  IMPORTANT: note the third argument below, to name the clone
git clone git@github.com:cs50spring2021/lab-yourname.git temp
cd temp
git checkout submit1  # or whatever lab number you're checking
```

Check to be sure it has all the files you expected (and no more).
For C programs, compile them as another check that all is well.

When done, clean up to avoid future confusion.

```bash
cd ..; rm -rf temp
```

## <a id="extra">Do I have any extraneous files?</a>

Your git repo (on GitHub) should be clean, that is, it should not have any extraneous files.
*You will lose points for including extraneous clutter in your submitted repo.*

As a general rule, you should never commit *derived* files (that is, files derived automatically from source files), temporary files, or backup files.
Thus, for example,

* no compiled binaries, such as `foo` compiled from `foo.c`
* no object files `*.o`  or library archive files `*.a`
* no editor backup files like `*~` or `*.bak`
* no core dumps `core`

The lab specs will often provide more specific instructions.

Use of a good `.gitignore` file and care when using `git add` will help avoid the addition of extraneous files.
Most lab starter kits come with a `.gitignore` file, which you can extend; we also include CS50-recommended rules as [.gitignore](https://github.com/CS50Dartmouth21FS1/examples/blob/main/.gitignore) in the examples repo.
