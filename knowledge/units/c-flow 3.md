Control flow within C programs is almost identical to the equivalent constructs in Java.
However, C provides no exception mechanism, and so C has no constructs like `try`, `catch`, and `finally`.

Conditional execution

```c
      if ( expression )
           statement1;

      if ( expression ) {
           statement1;
           statement2;
           ......
      }

      if ( expression ) {
           statement;
      } else {
           statement;
      }
```

Although the braces `{ }` are not required when the 'then' or 'else' clauses are a single statement, in CS50 we insist on including the braces every time.
Doing so helps to avoid mistakes that can occur if the indentation makes it look like a statement is part of a clause when it actually is not.
For example;

```c
      if ( expression )
           statement1;
           statement2;
      statement3;
```

will always execute `statement2` and `statement3`, even though it *looks* like `statement2` would only be executed when `expression` is true.
The compiler ignores indentation and treats the above as if it were written

```c
      if ( expression )
           statement1;
      statement2;
      statement3;
```

If that was not the programmers' intent, they should have written


```c
      if ( expression ) {
           statement1;
           statement2;
      }
      statement3;
```

Thus: always use braces, they'll avoid surprises.


## <a id="while-loop">while and do-while loops</a>

```c
      while ( conditional-expression ) {
           statement1;
           statement2;
           ......
      }

      do {
           statement1;
           statement2;
           ......
      } while ( conditional-expression );
```

As for the `if` statement, the braces `{ }` are not required when the loop body is a single statement, but in CS50 we insist on including the braces every time.


## <a id="for-loop">for loops</a>

```c
      for( initialization ; conditional-expression ; adjustment ) {
           statement1;
           statement2;
           ......
      }
```

Any of the components of the `for` statement's *for-expressions* may be missing; if the conditional-expression is missing, it is always true.
Infinite loops may be coded in C with `for( ; ; )` ... or with `while(true)` ...

As for the `while` loop, the braces `{ }` are not required when the loop body is a single statement, but in CS50 we insist on including the braces every time.

Note the above `for` loop is exactly equivalent to

```c
      initialization;
      while ( conditional-expression ) {
            statement1;
            statement2;
            ......
            adjustment;
      }
```


## <a id="switch">The switch statement</a>

The `switch` statement allows you to execute a different statement depending on the value of an expression, as follows:

```c
      switch ( expression ) {
           case const1 : statement1; break;
           case const2 : statement2; break;
           case const3 :
           case const4 : statement4;
           default : statementN; break;
      }
```

One of the few differences here between C and Java is that C permits control to "drop down" to following case constructs, unless there is an explicit `break` statement.


## <a id="break">The break statement</a>

The `break` statement causes control to break out of the innermost loop (`for` or `while`) or `switch` statement.

```c
      for ( expression1 ; expression2 ; expression3 ) {
           statement1 ;
           if( ... )
              break;
           statementN ;
      }
      // after 'break', execution continues here

      while ( expression1 ) {
           statement1 ;
           if( ... )
              break;
           statementN ;
      }
      // after 'break', execution continues here

      switch ( expression1 ) {
           case const1:
              statement 1;
              break;

           case const2:
              statement 2;
              break;

           case const3:
              statement 3;
              break;

           default:
              statement n;
              break;
       }
       // after 'break', execution continues here
```

## <a id="continue">The continue statement</a>

The `continue` statement causes control to jump to the bottom of the innermost enclosing loop (`for` or `while`), and continue looping from there.

```c
      for ( expression1 ; expression2 ; expression3 ) {
           statement1 ;
           if( ... )
              continue;
           statementN ;
           // after 'continue', execution continues here
      }

      while ( expression1 ) {
           statement1 ;
           if( ... )
              continue;
           statementN ;
           // after 'continue', execution continues here
      }
```

