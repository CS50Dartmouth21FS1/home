# Solution - groups and pipelines

Solution to the Billboard 100 problem.

```
1. sed 1,2d billboard.tsv
2. sed 1,2d billboard.tsv | sort -nr
3. sed 1,2d billboard.tsv | cut -f 3
4. sed 1,2d billboard.tsv | cut -f 3 | sort -u | wc -l
5. sed 1,2d billboard.tsv | cut -f 3 | sort | uniq -c | sort -nr | head -1
6. sed 1,2d billboard.tsv | cut -f 2
7. sed 1,2d billboard.tsv | cut -f 2 | tr '[:space:]' \\n | sort | uniq -c | sort -nr | head -1
```
