As noted in the [unit about Git Flow](https://github.com/CS50Dartmouth21FS1/home/blob/main/knowledge/units/git-flow.md), all development occurs on *feature branches*; no developer ever works on (or pushes to) the `main` branch.

To enforce this practice (and avoid mistakes!), set GitHub to [protect the main branch](https://docs.github.com/en/github/administering-a-repository/managing-a-branch-protection-rule):

1. From your repository's home page on GitHub, click *Settings*;
1. In the left menu, click *Branches*;
1. To the right of *Branch protection rules*, click *Add rule* button;
1. Under *Branch name pattern*, type `main`;
1. Under *Protect matching branches*,
	2. click *Require pull request reviews before merging*
	2. click *Include administrators*
1. At the bottom of the page, click green *Create* button.

Then, if anyone ever tries to push to `main`, perhaps by accident, they'll get this error:

```
$ git push -u origin main
Enumerating objects: 7, done.
Counting objects: 100% (7/7), done.
Delta compression using up to 16 threads
Compressing objects: 100% (5/5), done.
Writing objects: 100% (5/5), 738 bytes | 738.00 KiB/s, done.
Total 5 (delta 0), reused 0 (delta 0), pack-reused 0
remote: error: GH006: Protected branch update failed for refs/heads/main.
remote: error: At least 1 approving review is required by reviewers with write access.
To github.com:cs50spring2021/activity-git-flow.git
 ! [remote rejected] main -> main (protected branch hook declined)
error: failed to push some refs to 'github.com:cs50spring2021/activity-git-flow.git'
```

If a developer has accidentally worked on `main`, committing to `main` as they go, this error can be quite discouraging!
The solution is to create a branch, now.
(Yes, they should have done it *before* doing this work, but now it is the only way out of this mess.)

```
$ git branch feature
$ git switch feature
Switched to branch 'feature'
$ git push origin feature
Enumerating objects: 7, done.
Counting objects: 100% (7/7), done.
Delta compression using up to 16 threads
Compressing objects: 100% (5/5), done.
Writing objects: 100% (5/5), 738 bytes | 738.00 KiB/s, done.
Total 5 (delta 0), reused 0 (delta 0), pack-reused 0
remote: 
remote: Create a pull request for 'feature' on GitHub by visiting:
remote:      https://github.com/cs50spring2021/activity-git-flow/pull/new/feature
remote: 
To github.com:cs50spring2021/activity-git-flow.git
 * [new branch]      feature -> feature
```

Then they can create a pull request using the link provided.

Before they start work on a new feature, they should create a new branch for the new feature:

```
$ git switch main
$ git pull origin main
$ git branch feature2
$ git switch feature2
```
