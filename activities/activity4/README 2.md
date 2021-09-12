# Activity 4 - C programming with structs and pointers

## Goals

1. to use `structs`, arrays of `structs`
2. to better understand use of pointers for allocated memory, and function parameters

## Prepare

**Pick *one* person in your group to go first**, and [accept the assigment](https://classroom.github.com/g/EchZo59N) on GitHub Classroom.
They will be asked to join an existing group or to create a new group; they should create a new group using your CS50 group name.
Once finished, the rest of you should click through that link and pick the same group name from the list of existing groups.

Clone that repository and make yourself a new branch:

1. Log into plank; `cd cs50-dev`.
2. `git clone` the repository from GitHub Classroom, above.
3. `cd` into the resulting new subdirectory.
4. Copy the file `covid-blank.c` to `covid.c`
5. One member of the team share screen and edit that file, filling in BLANKs.
6. Build and run the program with `make test`.

Things to notice:

* a long-ish `main()` function (because of extensive error checking) but good section-header comments to indicate overall flow.
* use of the `file` module from Lab3
* A big `struct` for holding each row of data from the csv file
* a `typedef` to define a new type for that struct
* character pointers (for strings) inside the struct; where do they point?
* an array of structs - how do you allocate memory for it?
* extensive error-checking code throughout
* an opportunity for you to write the  'contract' for the `extractData` function (read about [function contracts](https://github.com/cs50dartmouth21FS1/home/blob/main/knowledge/units/contracts.md))
* detailed 'contract' for the `csvBurst` function
* in the `csvBurst` function, a 'moving pointer' approach to parsing the csv string
* chopping the csv string by dropping in null characters
* creating an array of pointers into the csv string
