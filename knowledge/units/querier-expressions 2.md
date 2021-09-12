The querier supports queries that combine words with the *and* and *or* operators.
Let's think about how one evaluates an expression involving operators.
Consider an arithmetic analogy.

## Arithmetic expressions

Consider the following arithmetic expression:

```c
sum = a + b + c + d
```

Since addition is a *left-associative* operator, this means the same thing as

```c
sum = (((a + b) + c) + d)
```

This means we can scan the expression from left to right, accumulating a sum as we go, effectively like this:

```c
sum = 0
sum = sum + a
sum = sum + b
sum = sum + c
sum = sum + d
```

Here, the `sum` acts as an *accumulator*.
(Indeed, many early hardware architectures include an explicit register called an 'accumulator'.)

We often see this approach generalized in code:

```c
int n = 5;
int array[n] = {42, 34, 12, -5, 19};
int sum = 0;
for (int i = 0; i < n; i++)
	sum += array[i];
printf("sum = %d; average = %f\n", sum, (float) sum / n);
```

### Precedence

What if you have a mixture of operators, with precedence?

Consider the following arithmetic expression:

```c
sum = a + b * c + d
```

Both addition and multiplication are *left-associative* operators, but multiplication takes *precedence* over addition.
Thus, we implicitly rewrite the above expression as follows:

```c
sum = ((a + (b * c)) + d)
```

or, in sequence,

```c
sum = 0
sum = sum + a 
prod = 1
prod = prod * b
prod = prod * c
sum = sum + prod
sum = sum + d
```

Notice how we 'step aside' from the sum for a moment while we compute the product `b * c` ... using an exactly analogous process.  `prod` is an accumulator for the product; it is initialized to the *multiplicative identity* (1) instead of the *additive identity* (0), for reasons I hope are obvious.
But then we just multiply in each of the successive items, one at a time.

This generalizes to longer expressions like

```c
sum = a * b + c * d * e + f + g * h * i
```

becomes

```c
sum = 0
prod = 1
prod = prod * a
prod = prod * b
sum = sum + prod
prod = 1
prod = prod * c
prod = prod * d
prod = prod * e
sum = sum + prod
prod = 1
prod = prod * f
sum = sum + prod
prod = 1
prod = prod * g
prod = prod * h
prod = prod * i
sum = sum + prod
```

Let's add some indentation to make this a little easier to read:

```c
sum = 0
	prod = 1
	prod = prod * a
	prod = prod * b
sum = sum + prod
	prod = 1
	prod = prod * c
	prod = prod * d
	prod = prod * e
sum = sum + prod
	prod = 1
	prod = prod * f
sum = sum + prod
	prod = 1
	prod = prod * g
	prod = prod * h
	prod = prod * i
sum = sum + prod
```

Notice what I did with `f`, and that I *never add anything to `sum` other than `prod`*.

This structure should give you a hint about how you might write code to evaluate such expressions...
if you have a `product` function to scan the expression left to right from a given starting point, accumulating a product of individual items until it sees a `+` or the end of the expression, you can then write a function `sum` that scans the expression left to right from the start, accumulating a sum of products by calling `product` at the start and after each `+`.

## Query expressions

Let's map this new insight onto the TSE query syntax.
That syntax combines *words* with two operators: *and*, *or*.
Each operator is left-associative, just like *multiply* and *add*.
The *and* operator takes precedence over the *or* operator, just like *multiply* takes precedence over *add*.
Although the syntax allows *and* to be omitted, you should mentally insert an *and* wherever an operator is missing.

Of course, we're not doing arithmetic; we're querying a document index for documents that contain words.
The result of a single-word query is the set of all documents which contain that word.
In a multi-word query expression, the "value" of each word is the set of documents matching that word, and the *and/or* operators manipulate those sets: the *and* operator computes a *set intersection*, and the *or* operator computes a *set union*.

The same *accumulator* idea works here, too.
For a sequence of words connected by *and*,

```
result = A and B and C       # original query
result = ((A and B) and C)   # rewritten, left-associative

result = A            # there is no "intersect identity", so just assign
result = result ^ B   # here I use ^ to represent set intersection
result = result ^ C
```

For a sequence of words connected by *or*,

```
result = X or Y

result = {}           # empty set, the "union identity"
result = result v X   # here I use v to represent set union
result = result v Y
```

And for a more complex query we need to leverage precedence:

```
result = X or A and B and C or Y        # original query
result = ((X or (A and B and C)) or Y)  # rewritten, per precedence

result = {}
result = result v X
	temp = A
	temp = temp ^ B
	temp = temp ^ C
result = result v temp
	temp = Y
result = result v temp	
```

Of course, there may be many *and sequences*, all within one *or sequence*:

```
result = A and B or P and Q or R and S and T or Z

result = {}
	temp = A
	temp = temp ^ B
result = result v temp
	temp = P
	temp = temp ^ Q
result = result v temp
	temp = R
	temp = temp ^ S
	temp = temp ^ T
result = result v temp
	temp = Z
result = result v temp
```

Notice what I did with `Z`, and that I *never union anything to `result` other than `temp`*.
