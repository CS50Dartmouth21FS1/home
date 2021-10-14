In C, the names of symbols (variables, constants, functions, types, etc.) must commence with a letter or the underscore character (thus, one of `A-Z a-z _`) and be followed by zero or more letters, underscores, or digits (thus, `A-Z a-z 0-9 _`).
Most C compilers, such as `gcc`, accept names to be up to 256 characters in length.
(Some older C compilers only supported variable names with up to 8 unique leading characters and keeping to this limit may be preferred to maintain portable code.)
Names are case sensitive, so `MYLIMIT`, `mylimit`, `Mylimit` and `MyLimit` are four different names.
By convention, all-caps names are typically used for constants (such as `BUFSIZE`, `AVAGADROS_NUMBER`, `MAXUSERS`),
and lower-case names are used for variables and function names.

For names that represent a compound word, you may encounter several naming styles, such as

* **camelCase**: writing compound words with the first letter of each word capitalized, except for the first word's first letter, which is not capitalized.

* **PascalCase**: writing compound words just as in camelCase with the first letter of the first word also capitalized.
(In Java it is common to use this case for class names, but Camel case for member names.)

* **snake\_case**: writing compound words with an underscore between each word with little, if any, capitalization.

> For an interesting discussion of these and other naming conventions, and why they are so important, check out this [devopedia post](https://devopedia.org/naming-conventions).

Any programming project should pick a naming style and *stick with it*.

**In CS50 we define a specific naming convention in our [Coding Style](https://github.com/CS50Dartmouth21FS1/home/blob/fall21s1/logistics/style.md),**
and your code is expected to follow that style.
