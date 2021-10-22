Ever run into this problem during a `git pull`?

```
Auto-merging file.c
CONFLICT (content): Merge conflict file.c
Automatic merge failed; fix conflicts and then commit the result
```

This is a *merge conflict*; they can be messy to resolve.  Git can usually figure out how to resolve these merge conflicts automatically, but not always.  When git can't resolve a merge conflict, don't worry: git will make sure you know either when you try to merge or the next time you use `git status`.

## What causes merge conflicts?

One common cause is concurrent editing of the same file.

1. Alice and Bob clone the same repository.
2. Bob makes changes to a file in the repository; add, commit, push.
3. Alice makes a change to the same file in the same line as Bob.
4. Alice adds, commits, and then pushes... which gets an error because her clone is out of date.

```bash
$ emacs README.md
$ git commit README.md -m "made some changes"
[main 3d2af64] made some changes
 1 file changed, 1 insertion(+)
$ git push
To github.com:cs50spring2021/demo.git
 ! [rejected]        main -> main (fetch first)
error: failed to push some refs to 'github.com:cs50spring2021/demo.git'
hint: Updates were rejected because the remote contains work that you do
hint: not have locally. This is usually caused by another repository pushing
hint: to the same ref. You may want to first integrate the remote changes
hint: (e.g., 'git pull ...') before pushing again.
hint: See the 'Note about fast-forwards' in 'git push --help' for details.
$ 
```

To resolve that error, Alice tries a `git pull`... which reports the merge conflict:

```bash
$ git pull
remote: Enumerating objects: 5, done.
remote: Counting objects: 100% (5/5), done.
remote: Compressing objects: 100% (2/2), done.
remote: Total 3 (delta 0), reused 3 (delta 0), pack-reused 0
Unpacking objects: 100% (3/3), 326 bytes | 163.00 KiB/s, done.
From github.com:cs50spring2021/demo
   e05f510..7a92782  main       -> origin/main
Auto-merging README.md
CONFLICT (content): Merge conflict in README.md
Automatic merge failed; fix conflicts and then commit the result.
$ 
```

Dang!  a merge conflict.
As a result, git modified the conflicting file to add some 'markers':

```bash
$ cat README.md 
# demo repo

This is a small repo for demonstrating merge conflicts.

<<<<<<< HEAD
This line was added by Alice, in the local clone.
=======
This line was added by Bob.
>>>>>>> 7a92782aa4bec41785407b0e71651396eb10e941
$ 
```

GitHub calls this a _competing line change merge conflict_. The lines between the `<<<<<<<` and `=======` are the local lines that conflict with the lines between the `=======` and the `>>>>>>>` on the remote repo.

Alice manually resolves the conflict, by editing the file; she decides to combine them:

```bash
$ emacs README.md
$ cat README.md 
# demo repo

This is a small repo for demonstrating merge conflicts.

This line was added by Alice, in the local clone, and
This line was added by Bob.
$ 
$ git add README.md
$ git commit -m "resolved README.md merge conflict"
[main 665235f] resolved README.md merge conflict
$ git push
Enumerating objects: 10, done.
Counting objects: 100% (10/10), done.
Delta compression using up to 16 threads
Compressing objects: 100% (4/4), done.
Writing objects: 100% (6/6), 629 bytes | 629.00 KiB/s, done.
Total 6 (delta 1), reused 0 (delta 0), pack-reused 0
remote: Resolving deltas: 100% (1/1), done.
To github.com:cs50spring2021/demo.git
   7a92782..665235f  main -> main
$ 
```

## Another kind of merge conflict

What happens if one person removes a file and others on the team don't know? 

1. Alice makes changes to a file, then `git add`, and `git commit` **only**.
2. Bob removes that same file in his copy of the repository, and `git add`, `git commit`, **and** `git push` .
3. Alice tries `git push` and gets an error reporting the missing file.

Alice then has to decide whether to keep the file (and her changes).
If so, Alice adds it back to git.  
For example, if the file is named "file.md" and Alice wanted to keep it, she should

```git
git add file.md
git commit -m "Resolved merge conflict by keeping file.md"
git push
```

If Alice decides to accept the file's removal, she has to update her repository:

```git
git rm file.md
git commit -m "Resolved merge conflict by removing file.md"
git push
```



## Git merge conflicts can occur in other ways too 

- `git stash`
  - This temporarily saves (or *stashes*) changes you've already made to your working copy so you can begin work on something else, and then return to finish that work later. Stashing is handy if you aren't quite ready to commit when you suddenly need to stop working on the next feature to fix a bug in the main branch, or if you need to pull someone else's changes to test something.
- `git rebase`
  - This is used to change the commit that a branch is based on to be based on some other commit.  It essentially rewrites history, so be very careful.  There's nearly always a better way.
- `git cherry-pick`
  - This allows you to pick a commit from one branch and apply it to another.  One good use is when a commit is applied to the wrong branch, although you could repair this situation other ways. Like `rebase`, be _sure_ you _really_ want to do this as it can cause other errors and conflicts.



## Git commands that can help explore the merge conflict

- `git status` will tell you which files are in conflict
- `git log --merge` after a failed merge will show a list of commits that are in conflict between branches
- `git diff` will show changes between commits, commit and work diretory, etc.

## Git merge conflicts must be resolved, but not necessarily _right now_

If you really can't deal with the conflict right now, or you think there may be some other way to resolve it, you can always cancel the  merge by using  `git merge --abort`.  This puts your branch back the way it was before you tried to merge.

## A far better solution

The best way to handle merge conflicts is to *avoid them.*
We advise you to follow the *Git Flow* style of development, described in the next [unit](https://github.com/CS50Dartmouth21FS1/home/blob/fall21s1/knowledge/units/git-flow.md).
