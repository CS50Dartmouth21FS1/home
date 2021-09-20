<!-- formerly bugs.md -->

## Missile system looking in the wrong place

From the February 1992 GAO report to the House of Representatives' Subcommittee on Investigations and Oversight, Committee on Science, Space, and Technology:

> *On February 25, 1991, a Patriot missile defense system operating at Dhahran, Saudi Arabia, during Operation Desert Storm failed to track and intercept an incoming Scud.
> This Scud subsequently hit an Army barracks, killing 28 Americans. ...
>  The Patriot battery at Dhahran failed to track and intercept the Scud missile because of a software problem in the system's weapons control computer.
> This problem led to an inaccurate tracking calculation that became worse the longer the system operated.
> At the time of the incident, the battery had been operating continuously for over 100 hours.
> By then, the inaccuracy was serious enough to cause the system to look in the wrong place for the incoming Scud. ...
>  The Patriot had never before been used to defend against Scud missiles nor was it expected to operate continuously for long periods of time.
> Two weeks before the incident, Army officials received Israeli data indicating some loss in accuracy after the system had been running for 8 consecutive hours.
> Consequently, Army officials modified the software to improve the system's accuracy.
> However, the modified software did not reach Dhahran until February 26, 1991--the day after the Scud incident.*

The problem was that in order to predict where an incoming missile *would be*, the system needed its velocity and the time expressed as real numbers to do the calculations.
Unfortunately, the time was maintained as an integer representing the number of tenths of a second since the system began operating.
The problem was that the longer it was running, the larger the error would be when converting the integer time into a real number.
Thus, the Patriot was looking "in the wrong place."

## Loss of the Mars Climate Orbiter and Mars Polar Lander

From [Wikipedia](https://en.wikipedia.org/wiki/List_of_software_bugs).

> *NASA Mars Polar Lander was destroyed because its flight software mistook vibrations due to atmospheric turbulence for evidence that the vehicle had landed and shut off the engines 40 meters from the Martian surface.*

> *Its sister spacecraft Mars Climate Orbiter was also destroyed, due to software on the ground generating commands in pound-force (lbf), while the orbiter expected newtons (N).*


## Proven program found to have a bug 20 years later

> adapted from Joshua Bloch's [blog](http://bit.ly/FMKax).

Jon Bentley (Author of *Programming Pearls* and other books) observed that a relatively simple piece of code for a binary sort that he had proven to be correct during a lecture at CMU had a bug in it that wasn't discovered until 20 years later.
Here's the code:

```c
    1:     int binarySearch(int[] a, int length, int key) {
    2:         int low = 0, mid, midVal;
    3:         int high = length - 1;
    4:
    5:         while (low <= high) {
    6:             mid = (low + high) / 2;
    7:             midVal = a[mid];
    8:
    9:             if (midVal < key)
    10:                 low = mid + 1
    11:             else if (midVal > key)
    12:                 high = mid - 1;
    13:             else
    14:                 return mid; // key found
    15:         }
    16:         return -(low + 1);  // key not found.
    17:     }
```

The bug was found to be in line 6:

```c
    6:             mid = (low + high) / 2;
```

This looks correct, since it's purpose is to find the average of `low` and `high`, rounded down to the nearest integer.
However, if the sum of `low` and `high` exceed the maximum signed int value (`2^31-1` on some architectures), the sum goes negative and the resulting value for `mid` is also.

So how would you fix it?
The following would work:

```c
    6:             mid = low + ((high-low) / 2);
```

## More examples

 * *[Historical software bugs with extreme consequences](https://www.pingdom.com/blog/10-historical-software-bugs-with-extreme-consequences/)*, from a blog
 * *[List of software bugs](https://en.wikipedia.org/wiki/List_of_software_bugs)*, Wikipedia
