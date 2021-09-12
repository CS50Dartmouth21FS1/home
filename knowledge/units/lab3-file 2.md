This unit is a quick video walk-through of **file module** in the [Lab 3](https://github.com/CS50Dartmouth21FS1/home/blob/main/labs/lab3) starter kit, and its Makefile.

**[:arrow_forward: Video](https://dartmouth.hosted.panopto.com/Panopto/Pages/Viewer.aspx?id=7ddd9b83-9143-4d79-a0e6-ad0d011b437a)**

The module implements these handy functions:

```c
int file_numLines(FILE* fp);
char* file_readUntil(FILE* fp,    int (*stopfunc)(int c) );
char* file_readFile(FILE* fp);
char* file_readLine(FILE* fp);
char* file_readWord(FILE* fp);
```

See `file.h` for a full description of the behavior and interface to each function.

