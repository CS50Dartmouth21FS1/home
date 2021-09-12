In this unit we discuss some high-level design principles.

* *routines* - types and names
* *cohesion* - and why we want strong cohesion
* *coupling* - and why we want weak coupling

Many of these concepts come from chapter 5 of *Code Complete* by Steve McConnell. [Microsoft Press, 1993.]

## Routines

We often use the word *routine* as a general concept, to refer to  a *function* or a *procedure*.
We typically use the word *procedure* to refer to a function with no return value.
In other languages, routines might be called *subroutines* or *methods*.

There are many reasons to break a program into *routines*:

* Reduce complexity
* Avoid duplicate code
* Limit effects of changes (narrow scope of change to a routine)
* Hide sequences (a form of information hiding)
* Improve performance (optimize in one place)
* Centralize control (e.g., controlling devices, files, data structures... more information hiding)
* Hide data structures (classes, or abstract data types)
* Hide global data (access routines centralize control of globals)
* Hide pointer operations (makes it easier to read)
* Promote code reuse (easier to reuse code in a routine)
* Plan for a family of programs (isolate parts that may change in a few routines)
* Improve readability (named routine is more readable; avoid deep nesting, etc.)
* Improve portability (isolate nonportable features)
* Isolate complex operations (algorithm, protocols, etc.)
* Isolate use of nonstandard language functions (isolate nonportable features)
* Simplify complicated boolean tests (inlines are great here)

Routines should be well named:

* a *procedure* name should be a strong verb followed by object (like *printCalendar()*).
* a *function* name should describe its return value (like *numberOfNonzeros()*).
* a *boolean function* name should sound like a question (like *isInternalURL()*).

A good routine name

* avoids nondescriptive verbs (like *do*, *perform*)
* describes everything the routine does
* is as long as necessary
* follows naming conventions!

A routine's body should not be **too long**.
If you find your routine approaching 200 lines, you should break it up - or have a darn good reason you should not break it up.

### Cohesion

> "Cohesion refers to how closely [or strongly] the operations in a routine are related." -- McConnell

One study found that 50% of high-cohesion routines were fault-free, while 18% of low-cohesion routines were fault-free.

Good routines have ***strong cohesion***.

**Acceptable cohesion**:

* **Functional cohesion** (strongest and best kind): performs one and only one operation.
* **Sequential cohesion**: contains operations that must be performed in a sequential order.
* **Communicational cohesion**: contains operations that make use of the same data, but are not otherwise related.
* **Temporal cohesion**: contains operations that do several things, because all are done at the same time.

**Unacceptable cohesion**:

* **Procedural cohesion**: contains operations that must be performed in a sequential order, but don't share the same data.
* **Logical cohesion**: several things in a routine, only one executed, depending on a flag parameter.
(Exception - it can be  ok if using a `switch` statement to call one of many other (cohesive) functions.)
* **Coincidental cohesion**: no apparent reason for things to be together in a routine!

### Coupling

> "The degree of coupling refers to the strength of a connection between two routines. Coupling is a complement to cohesion." -- McConnell

Good code has ***loose coupling*** among routines.

> "Make the coupling of routines as simple as possible."

**Criteria** for evaluating coupling between routines:

* **size** (number of connections)
* **intimacy** (directness of connection; better to use direct parameter passing than indirect global variables)
* **visibility** (best if connection is obvious)
* **flexibility** (how easily you can change connections)

Go for minimal interconnectedness, and make what interconnections you have simple and obvious.
"If a program were a piece of wood, you'd try to split it with the grain."


#### Kinds of coupling

* **Simple-data coupling**: the only data passed from one routine to another is through parameters, and is nonstructured.
* **Data-structure coupling**: one routine passes a data structure to another; best if it really needs the whole data structure.
* **Control coupling**: one routine tells the other what to do.
* **Global-data coupling**: two routines use the same global data; may be tolerable if read-only.
* **Pathological coupling**: one routine uses the data inside the other routine. (Somewhat hard to do in C and C++.)

> "Try to create routines that depend little on other routines."


#### Defensive programming

Best practices:

* Use assert() or other tests to insert sanity checks into your code.
* Be particularly suspicious of parameters and input values that come from another module (including the user!), i.e., data that crosses a module interface.

#### Routine parameters

Best practices:

* Put parameters in input-modify-output order; put "status" or "error" variables last.
* Use all the parameters.
* Use a consistent order among similar routines.
* Document assumptions about parameters: *Preconditions* are assumptions about the parameters (or internal data-structure state) before the routine executes, and *postconditions* are assumptions about the parameters, data structure, and return value after the routine exits.

#### Macros

You can write *macros* in C with #define - these are handled by the C preprocessor.
Macros can be handy, on occasion, but it is usually better to use `inline` functions or `const` variables for these purposes.
You get better syntax, type checking, and better debugger support.

## Modules

A *module* is a collection of related routines that, together, encapsulate a data structure, subsystem, or task.
In object-oriented programming, a module is typically called a *class*.

Why modules?

* It is sometimes necessary to have strong coupling between routines.
* In that case you should group those routines into a *module*; the module has strong cohesion.
* Strive for strong intra-module cohesion and loose inter-module coupling.
* A well-designed module (in C) or class (in C++ or Java) encourages *information hiding*.

#### Information hiding

A good module serves to "hide" implementation details inside the module, providing a clean abstraction and clear interface to other modules.

Each module programmer makes many implementation decisions, which should largely be unknown to code outside the module.
Thus the module is

* less coupled to others,
* more maintainable,
* more self-testable,
* more replacable,
* more debuggable (can insert debugging code easily),
* more reliable (can insert checks in a few places),
* more understandable (hides complexity).

#### Collaborative work

Modules (classes) are an excellent mechanism for dividing work among programmers, because module boundaries and interfaces are clear, and there is otherwise little coupling.
Thus, the module programmer has wide flexibility and authority on the implementation details of her module, as long as she sticks to the agreed-upon interface for the module and clearly documents the interface.

## Examples

Study this [example code](https://github.com/cs50dartmouth21FS1/examples/blob/main/cohesion.c);
for each function, what kinds of cohesion and coupling do you see?
What function could perhaps be better named?
When finished, take a look at our [annotated version](https://github.com/cs50dartmouth21FS1/examples/blob/main/cohesion-annotated.c) for our take on those questions.
