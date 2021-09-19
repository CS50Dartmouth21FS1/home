This section is for quick reference.

## Basic Operators

Nearly all operators in C are identical to those of Java.
Here are the basic operators:

- Assignment
    `=`
- Arithmetic
    `+, -, *, /, %`
- Priorities may be overridden with `( )`'s.
- Relational (all of these have the same precedence)
    `>, >=, <, <=`
- Equality
    `==` , `!=`
- Logical
    `&&` (and), `||` (or), `!` (not)

## Decrement and increment operators

Any (integer, character, or pointer) variable may be either incremented or decremented before or after its value is used in an expression.

For example:

`--fred`  will decrement `fred` before its value is used

`++fred`  will increment `fred` before its value is used

`fred--`  will get (old) value and then decrement `fred`

`fred++`  will get (old) value and then increment `fred`

Take a close look at the output of [increment.c](https://github.com/CS50Dartmouth21FS1/examples/blob/fall21s1/increment.c):

```bash
$ mygcc -o increment increment.c
$ ./increment 
Start; fred = 3 and a = 3
a = --fred; fred = 2 and a = 2
a = ++fred; fred = 3 and a = 3
a = fred--; fred = 2 and a = 3
a = fred++; fred = 3 and a = 2
$ 
```

## Bitwise operators and masking

Ultimately, every integer variable is just a bit string, so C has operators to allow you to manipulate an integer as a sequence of bits:

`&` (bitwise and), `|` (bitwise or), `~` (bitwise negation).

You can use these to check if certain bits are on, as in `(nextchar & 0x30)`, which computes a bitwise 'and' operation between the variable `nextchar` and the hexadecimal constant number `0x30`.

You can also shift bits to the left or right:

Shift operators `<<` (shift left), `>>` (shift right)

Note: results may vary based upon whether the type of the variable being shifted is "signed" or "unsigned".
See H&S pp.231-233.

## Combining operations with assignment

You can also combine an operation with an assignment;
for example, `a += 2` is equivalent to `a = a + 2`.

Explore the example [combined.c](https://github.com/CS50Dartmouth21FS1/examples/blob/fall21s1/combined.c).


## Precedence of operators

Expressions are all evaluated from left-to-right, and the default precedence may be overridden with brackets.

| Operator  |
| Precedence|
| :--------:|
| *highest* |
| `( )`     |
| `++ -- !` |
| `* / % |` |
| `+ - `    |
| `==  !=`  |
| `&`       |
| `|`       |
| `&&`      |
| `||`      |
| `?:`      |
| `=`       |
| `,`       |
| *lowest*  |
