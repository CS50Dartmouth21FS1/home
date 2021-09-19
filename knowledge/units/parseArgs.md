**[:arrow_forward: Video demo of parseArgs example](https://dartmouth.hosted.panopto.com/Panopto/Pages/Viewer.aspx?id=76caa480-f5c3-478d-bd8f-ad0b00f42fb1)**

A demonstration of a method for hiding the messy work of command-line argument parsing & validating, in a utility function I like to call `parseArgs()`.
As your own programs become more complicated, I highly recommend you take a similar approach - it declutters the `main()` function so the reader can focus on the top-level logic of the program, clearly separating the code required to parse the command line from the logic of actually doing what the program is meant to do.

See the completed example code [parseArgs.c](https://github.com/CS50Dartmouth21FS1/examples/blob/fall21s1/parseArgs.c).
