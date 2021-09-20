We've actually been using a module with a built-in unit test, for a couple of weeks: `readlinep.c`.
That file ends with the following:

```c
/* ********************* UNIT TEST *************** */

#ifdef UNIT_TEST
/* A simple unit test: 
 * open a file given as command-line argument, 
 * read it line by line,
 * and print each line surrounded by [brackets].
 */
int
main(const int argc, char* argv[])
{
  if (argc != 2) 
    exit(1);

  FILE* fp = fopen(argv[1], "r");
  if (fp == NULL) {
    printf("can't open %s\n", argv[1]);
    exit(2);
  }

  char* line;
  while ( (line = freadLinep(fp)) != NULL) {
    printf("[%s]\n", line);
    free(line);
  }
  fclose(fp);
}
#endif
```

It's not a comprehensive test, but it does allow someone to feed it a variety of input files (perhaps in a regression test) to test conditions like

* empty file
* one-line file
* multi-line file
* 0-character line (empty line)
* 1-character line
* 2-character line
* 80-character line
* 81-character line
* 82-character line
* long line, perhaps 10,000 characters
* file with EOF at the beginning of a line
* file with EOF in the middle of a line, i.e., a line that does not end with a newline character.

Think about why I chose each of those specific cases.

