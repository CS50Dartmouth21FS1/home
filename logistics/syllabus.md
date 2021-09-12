
# CS50 Syllabus

In Computer Science 50 you will learn how to design & build large, reliable, maintainable, and understandable software systems.
In the process you will learn to program in C with Unix development tools.

Let's break that down.
The objectives of this course are

* to be capable working at the Unix command line;
* to be capable coding in C;
* to understand pointers and memory management;
* to be capable using core Unix development tools: editors, Makefiles, gdb, and git;
* to be able to design and implement a large, complex program with a structured approach;
* to develop a strong sense of programming style and ‘clean’ coding;
* to develop good documentation practices;
* to develop good testing practices;
* to be able to work effectively in a team.

The first few weeks of this course are a crash course in Unix and C.
After that, you'll build your very own Search Engine application.
Finally, you'll join a team of other students to design, build, document, and test a large software project.

## Structure

There are no lectures and no textbook; instead, course content is a series of [knowledge units](https://github.com/CS50Dartmouth21FS1/home/blob/main/knowledge), many with pre-recorded videos for viewing at your own pace and at a time convenient to you.
The unit index is annotated with 'milestones' marking the set of units you should complete before each classroom activity or lab assignment.
The notes and video are meant to complement (not duplicate) each other, so you should review the lecture notes *and* the video.

For most of the course, each section will meet only once per week.
Why? because I respect the time you are spending outside class reading the units and working on the labs.
On Wednesdays, you will meet with your team to work on a group activity, assisted by Adjunct Professor Charles C Palmer and Learning Fellows.
It is very important for you to attend, because
(1) these activities help to re-inforce the lecture material,
(2) working with your teammates will prepare you for the final project,
(3) it is a chance for real-time interaction with and assistance from the Learning Fellows and Adjunct Professor Charles C Palmer.

The Learning Fellows (LFs) are here to facilitate active learning through peer instruction -- critical to your learning in this course because your reasoning skills will develop most effectively when you are actively engaged with this material.
Research has shown that cooperative learning is one of the best ways to achieve this engagement.

Adjunct Professor Charles C Palmer and the Learning Fellows will circulate through the breakout rooms, promoting useful dialog and providing feedback.
Your questions and feedback will help shape the course based on your questions and ideas.
If you have questions about the Learning Fellow Program in general, please visit the [Dartmouth Learning Fellows site](https://sites.dartmouth.edu/learningfellows/).

## <a id="schedule">Schedule</a>

<!-- @CHANGEME -->
All class times, and the official assignment deadlines, are posted in the [Canvas](https://canvas.dartmouth.edu/courses/49179) calendar; we recommend you subscribe to that calendar using whatever calendar tool you prefer.
Changes will be announced via updates to the Canvas calendar.

### <a id="deadlines">Deadlines</a>

|       deadline*    | deliverable
|               ---: | :---
|    21 Sep 10pm EDT | Lab 1
|    28 Sep 10pm EDT | Lab 2
|     7 Oct 10pm EDT | Lab 3
|    16 Oct 10pm EDT | Lab 4
|    22 Oct 10pm EDT | Lab 5
|    29 Oct 10pm EDT | Lab 6
|    1 Nov 5-8pm EDT | Project design reviews**
|     1 Nov 10pm EDT | Project design spec
|     5 Nov 10pm EDT | Project implementation spec (updated deadline)
|    14 Nov 10pm EDT | Project materials
| 15,17 Nov classtime | Project demos**

*If this schedule and the [Canvas calendar](https://canvas.dartmouth.edu/courses/49179) somehow differ, the Canvas deadline is definitive.

**Design reviews may occur outside of regularly scheduled class hours.
These meetings will be scheduled in consultation with the teams.

---

## Communication

We will be using 
[Canvas](https://canvas.dartmouth.edu/courses/49179) for the Calendar and Grades,
[Panopto](https://canvas.dartmouth.edu/courses/49179/external_tools/14028) for knowledge units and other videos,
and
[Ed Discussion](https://edstem.org/us/courses/13476/discussion/) for class discussion and announcements.


### Contacting the teaching team

**Do _not_ send us email**.  

The majority of questions and problems can be resolved via posts to [Ed Discussion](https://edstem.org/us/courses/13476/discussion/) (see the next section).
By posting to the appropriate channel, anyone on the teaching team, or a fellow student, can answer your question.
**However,** if your message might expose details of your solution or approach to a lab assignment, make it a private post and onlythe staff will see it.

**Office hours are Monday and Wednesday with Prof. Palmer (signup using links on Canvas), and Tuesday and Thursday with Grad TA Tao (Zoom info on [Canvas](https://canvas.dartmouth.edu/courses/49179)).**  

If you need a private conversation with the teaching team, (1) sign up for private office hours with the Professor, or (2) make a private post in [Ed Discussion](https://edstem.org/us/courses/13476/discussion/) and a staff member will contact you.  If a particular staff member is mentioned, that staff member or an alternate will get in touch with you there.

Follow the [Canvas](https://canvas.dartmouth.edu/courses/49179) calendar to be alerted to any changes.

*Note*: If you sign up for an office hours slit with the Professor and later  realize you no longer need the appointment, please be sure to cancel it so others may use the slot.

### <a id="edstem">Ed Discussion</a>

From our [Canvas](https://canvas.dartmouth.edu/courses/49179) page you can join the [Ed Discussion](https://edstem.org/us/courses/13476/discussion/) workspace for our class; [here's how](./systems.md#slack-setup).
This workspace will be the primary location for engagement with one another, our TAs and LFs, and the instructor outside of class.
You can use the space to interact with your team, to ask questions about the assignments or course material, and to share content (and a bit of fun) related to the course.  In addition, the teaching team will make announcements in the `Logistics` category on [Ed Discussion](https://edstem.org/us/courses/13476/discussion/).

Some important points about seeking CS50 help over Ed Discussion:

* Search the help channels on Ed Discussion to see if your question has already been asked and answered.
* Tag your post with the appropriate category.
* LFs are here to support you in-class and may have little or no time to answer questions outside class time; they’ll let you know if/when they can answer questions in Ed Discussion.
* TAs are here to support you outside of class, by providing feedback on your lab submissions; when they have time, they answer questions in Ed Discussion too.
* Every member of the instructional team works part-time, so ask your questions in **public** posts rather than in private ones, so your will be seen by your classmates as well as the teaching staff.
* Exception: use private postss when your question may expose information others should not see, such as code from your lab solution.
* Always be polite: it's easy to forget that when we're discussing things online rather than face-to-face.

---

## Teams

After the first day of class, you will be assigned to a 4-person team; this team will be your Final Project team.  You should sit together as a team, to get to know each other well before the project begins. We will occasionally have in-class activities that you will work on with your team.

> If you have any concerns about your teammates or team assignment, at any time, please contact Adjunct Professor Charles C Palmer privately.

---

## Assignments

All computing work will occur on Linux systems, with code managed and shared in [GitHub](https://github.com/CS50Dartmouth21FS1); see the [Systems](https://github.com/CS50Dartmouth21FS1/home/blob/main/logistics/systems.md) page for details.

* ***Labs:***
	You will work individually on a series of six programming assignments ("labs"), which become more challenging through the term.
	All labs and the project are submitted through GitHub Classroom (see below).

* ***Project:***
	Students will work on a final project with their assigned team.
	After the project is submitted, each member of a group will submit a confidential assessment of themselves and the others in their group.

---

## <a id="grading">Grading</a>

There are no exams in this class -- instead, student assessment is based on a series of programming exercises, a team project, and class engagement.

| Component                           | Percentage |
|:----------------------------------- | ----------:|
| Lab 1 (bash)                        |         8% |
| Labs 2-3 (C)                        |        20% |
| Labs 4-6 (Tiny Search Engine - TSE) |        36% |
| Final project team grade            |        26% |
| Class engagement                    |        10% |

***Lab grades*** depend on whether your submission meets the requirements for that lab, and being submitted on time.
See the [late assignment](#late) policies below.
Remember, the graders test your lab submissions on the CS50 setup on the  `plank.thayer.dartmouth.edu` system.
If it doesn't run correctly there, it doesn't matter whether it runs on your Mac/Windows/Linux laptop.

The ***final project*** is a team grade -- every team member gets the same grade -- **but** I will subtract points from the Project grade for Members who contribute substantially less than their teammates.
At the end of the course there is a mechanism for peer evaluation to address variance in members' contribution.

***Class engagement*** will be assessed on your degree of engagement in class activities, and on Ed Discussion.
*Engagement* requires both *preparation* and *participation* -- not just attendance; you should read ahead, think ahead, and actively participate.

### Homework grading/regrading

Your work is graded according to a rubric that I discuss with the graders.
If you have any grading questions on a homework or quiz, please follow the procedure below.

1. If the grader made an obvious mistake (e.g., totaling error), make a private post in the `Grading` category and we'll make the correction right away.

2. If you feel the grader did not grade your answer accurately, make a private post in the `Grading` category to explain which program(s) need regrading and why.  We will arrange for your answer to be reviewed again.

3. After you hear the outcome on your regrade request, if you still feel the matter is not resolved satisfactorily, then you should see Adjunct Professor Charles C Palmer.

**You must submit your regrade request *within one week* from when the lab assessment was sent to you.**

Please note that any regrade request that comes after the deadline stated above will not be considered, regardless of its merit.

### Programs that crash

Your lab solutions are graded on *correctness*, *style*, *design*, and *documentation*.
Each lab requires you to document your own tests and test results; the graders will also compile and run your program with their own test cases.
If your program crashes when the graders run a particular test, your program will be marked as incorrect for that test.
If your program does not compile, you will lose all correctness points.
The best way to avoid that situation is to ensure that your program compiles and doesn't crash when run on the `plank.thayer.dartmouth.edu` system in the first place.  

Learning how to debug and test your programs is a key part of this course.

### Late assignments

This is a challenging course, so self-discipline and planning will be key to your success. In short, *Start Early*!
Remember, all due dates and times are posted on the [Canvas](https://canvas.dartmouth.edu/courses/49179) calendar.

Late lab assignments will be penalized 10 points for each 24-hour period they are late, up to 72 hours.  Beyond 72 hours late the lab assignment will not be accepted and you will receive zero points for the lab.

For example, if a Lab is due at 10PM EDT on the 12th and it is turned in at 1AM EDT on the 13th the score for that lab will be reduced by 10 points.

**No assignments will be accepted more than 72 hours after the original deadline other than in *extraordinary* cases.**


---

## Student responsibilities

 * Join your team in class when required.
 * Engage in course activities in class and on Ed Discussion.
 * Read/watch the knowledge units, and skim the suggested reading.
 * Pay attention to [Ed Discussion](https://edstem.org/us/courses/13476/discussion/) for all announcements and discussions.
 * Code the Labs and the Project and submit them on time.
 * Engage fully with your final project team.
 * Strive to produce well-designed, well-documented, well-tested, well-styled code.

### Academic Honor Principle

The [Academic Honor Principle](https://students.dartmouth.edu/judicial-affairs/policy/academic-honor-principle) applies to this course, as it does to all Dartmouth courses.
In particular, you are expected to produce all written material yourself, and to explicitly acknowledge anyone (including other students enrolled in the class) from whom you receive assistance.
Furthermore, as with any Dartmouth activity, you are expected to abide by [Dartmouth’s Standards of Conduct](https://student-affairs.dartmouth.edu/policy/standards-conduct).
The integrity of the Dartmouth community depends upon students’ acceptance of individual responsibility and respect for the rights of others.

You may discuss and help each other (help in debugging, sharing knowledge, giving moral support, getting coffee, etc.).
This support is the type of team spirit and joint problem-solving skills that are the essence of the course and necessary to do a great project.

However, you cannot work jointly on coding (i.e., writing) your individual programming assignments. This means _all_ of the lab assignments, except for the project, are to be solely your own work.
You can talk, discuss solutions, even draw snippets of code on the white board (not the computer) to solve a problem but you cannot jointly work on the code development and writing.
Submitted code for the labs has to be yours and yours alone.

The project phase is different.
You will work jointly with your project team on writing code and documentation.
But you cannot take code from anywhere (e.g., the web or any other source).
It has to be the joint product of the team.
No sharing of code between teams.
As above, teams can discuss coding challenges, but not show or share code with other teams.

In either case, you should not read or incorporate solutions for assignments found on the Web (including websites for previous terms, inside or outside of Dartmouth).

#### Specific GitHub warning

We will also be learning how to do software development with a team using the `git` versioning system.
We will use git with [GitHub](https://github.com), which is an open repository of projects (including source code) from programmers around the world.
Some past students of CS50 uploaded their assignment or project solutions to GitHub for reference by potential employers.
While this is may be a good idea for their job search, it is disruptive, distracting, and misleading for you and future students.

Keep in mind:

-   Anyone can upload code to GitHub.
That doesn't mean it's good code, working code, or even code that will compile and run!
-   If you need to upload your work for reference by potential employers, GitHub offers **free private repositories** to students which have an associated secret URL that you may privately share.
-   Any student found to have uploaded any CS50 assignment solutions, including TSE and the final project, to a publicly available GitHub repository, or to a private GitHub repository while sharing its access credentials with other Dartmouth students, will be reported.
-   Anyone caught using the work of prior CS50 students, whether from GitHub or other sources, will be reported.

We can assure you that violations of the Honor Code have been, and will continue to be, treated **seriously**.

We agree with Professor Cormen, who wrote:

> I reserve the right to assign you a failing grade on an entire homework assignment or on an entire exam if I believe that you have violated the Academic Honor Principle, apart from any finding by the COS.
> I will give you every opportunity to convince me that you did not violate the Academic Honor Principle, but I take the Academic Honor Principle very seriously.
> You have read Sources and Citation at Dartmouth College.
> I was co-chair of the committee that wrote this document.
> In fact, I wrote the first draft.
> So I know exactly what it says.
> Cheaters---whether or not they are caught---bring dishonor upon themselves and upon everyone else at Dartmouth.
> To do that, for just a few lousy points in a course, is [insert your favorite strong adjective meaning "stupid" here].
> You cannot fool me into thinking that you did not cheat if, in fact you did.
> So don't cheat.

Please let me know if you have any questions -- better to be safe than sorry!

### Credit your sources

Any ideas you get from other teams or any other source should be carefully cited both in the code and in the documentation.

-   In your assignments, list all your collaborators (e.g., "I discussed this homework with Alice and Bob") and credit any sources (including code) used.  For example, "I got an idea for resolving my git merge conflicts on StackOverflow."
-   You must also credit specific sources that are provided by the instructor.
For example, you must credit code that we give you if it helps you with your work (either by direct use of the code, or by simply enhancing your understanding by reading the code).
-   References for any non-trivial algorithms you employ should be included in the code and documentation to ensure others will know where to learn more about it.

Copying code from StackOverflow or other online sources is strictly forbidden and easy to check. The code you submit must be your own.

For more general information, see [Dartmouth's guidelines for proper citation of sources](http://writing-speech.dartmouth.edu/learning/materials/sources-and-citations-dartmouth),
particularly the section on [Computer programming assignments](http://writing-speech.dartmouth.edu/learning/materials/sources-and-citations-dartmouth#computerprogramming).

---

## You and the Class Environment

### Wellness

The following is standard text provided by Dartmouth [here](https://dcal.dartmouth.edu/resources/course-design-preparation/syllabus-guide).

The academic environment at Dartmouth is challenging, our terms are intensive, and classes are not the only demanding part of your life. There are a number of resources available to you on campus to support your wellness, including your [undergraduate dean](https://students.dartmouth.edu/undergraduate-deans/), [Counseling and Human Development](https://students.dartmouth.edu/health-service/counseling/about), and the [Student Wellness Center](https://students.dartmouth.edu/wellness-center/). I encourage you to use these resources to take care of yourself throughout the term, and to come speak to me if you experience any difficulties.

### Accessibility

The following is standard text provided by Dartmouth [here](https://dcal.dartmouth.edu/resources/course-design-preparation/syllabus-guide).

Students requesting disability-related accommodations and services for this course are encouraged to schedule a Zoom meeting with me as early in the term as possible. This conversation will help to establish what supports are built into my course. In order for accommodations to be authorized, students are required to consult with Student Accessibility Services (SAS: [webpage](https://students.dartmouth.edu/student-accessibility/students/working-sas/getting-started), [email](mailto:student.accessibility.services@dartmouth.edu), or phone +1-603-646-9900) and to request an accommodation email be sent to me. We will then work together with SAS if accommodations need to be modified based on the learning environment. If students have questions about whether they are eligible for accommodations, they should contact the SAS office. All inquiries and discussions will remain confidential.

If you encounter financial challenges related to this class, please let me know. 

### Respect, Diversity, and Inclusion

I aim to create a productive learning environment for all, one that supports a diversity of thoughts, perspectives and experiences, and honors everyone's identities (race, gender, class, sexuality, religion, ability, etc.)
To help accomplish this goal:

-  I ask that we all contribute to the course conversation in a professional manner.
   Specifically, I encourage you to challenge *ideas*, but always to *respect the speaker (or author)*.
   Indeed, it is particularly important that we respect the diversity of our course community, including students on the teaching team, drawing strength from the diversity of backgrounds and perspectives they all bring to the conversation. 

- If you have a preferred name and/or pronouns that differ from
  those that appear in your official college records, please let me know.

- If you feel like your performance in the class is being impacted by your experiences outside of class, or if you encounter financial challenges related to this class, please don’t hesitate to talk with me.
  Remember that you can also submit anonymous feedback (which may lead to me making a general announcement to the class, if necessary to address your concerns).
  If you prefer to speak with someone outside of the course, your [Dean](https://students.dartmouth.edu/undergraduate-deans/about/about-us) can be an excellent resource.

- If at any time you feel uncomfortable about the interactions in our course, I encourage you to contact me privately so I can better understand how I can manage the course. (Again, anonymous feedback is always an option.)


### Title IX

The following is standard text provided by Dartmouth [here](https://dcal.dartmouth.edu/resources/course-design-preparation/syllabus-guide).

At Dartmouth, we value integrity, responsibility, and respect for the rights and interests of others, all central to our Principles of Community. We are dedicated to establishing and maintaining a safe and inclusive campus where all have equal access to the educational and employment opportunities Dartmouth offers. We strive to promote an environment of sexual respect, safety, and well-being. In its policies and standards, Dartmouth demonstrates unequivocally that sexual assault, gender-based harassment, domestic violence, dating violence, and stalking are not tolerated in our community. 

The [Sexual Respect Website](https://sexual-respect.dartmouth.edu) at Dartmouth provides a wealth of information on your rights with regard to sexual respect and resources that are available to all in our community. 

Please note that, as a faculty member, I am obligated to share disclosures regarding conduct under Title IX with Dartmouth's Title IX Coordinator. Confidential resources are also available, and include licensed medical or counseling professionals (e.g., a licensed psychologist), staff members of organizations recognized as rape crisis centers under state law (such as WISE), and ordained clergy; see [resources](https://dartgo.org/titleix_resources).

Should you have any questions, please feel free to contact Dartmouth's Title IX Coordinator or the Deputy Title IX Coordinator for the Guarini School. Their contact information can be found on the sexual respect [website](https://sexual-respect.dartmouth.edu).

### Religious observations

The following is standard text provided by Dartmouth [here](https://dcal.dartmouth.edu/resources/course-design-preparation/syllabus-guide).

Some students may wish to take part in religious observances that occur during this academic term. If you have a religious observance that conflicts with your participation in the course, please meet with me before the end of the second week of the term to discuss appropriate accommodations.

### Inclement weather

On rare occasions, Dartmouth may cancel classes or even close the campus. If this occurs,
general notice will be given in three ways:

- Message via Ed Discussion;
- Local broadcast media;
- Campus-wide BlitzMail messages; and
- A recorded message at a College toll-free Inclement Weather Phone Line: 1-888-566-SNOW (1-888-566-7669).

### Consent to record

The following is standard text provided by Dartmouth [here](https://dcal.dartmouth.edu/resources/course-design-preparation/syllabus-guide).

1. **Consent to recording of course and group office hours.**
By enrolling in this course,

	a. I affirm my understanding that the instructor may record this course and any associated group meetings involving students and the instructor, including but not limited to scheduled and ad hoc office hours and other consultations, within any digital platform, including those used to offer remote instruction for this course.

	b. I further affirm that the instructor owns the copyright to their instructional materials, of which these recordings constitute a part, and my distribution of any of these recordings in whole or in part to any person or entity other than other members of the class without prior written consent of the instructor may be subject to discipline by Dartmouth up to and including separation from Dartmouth.

2. **Requirement of consent to one-on-one recordings.**
By enrolling in this course, I hereby affirm that I will not make a recording in any medium of any one-on-one meeting with the instructor or another member of the class or group of members of the class without obtaining the prior written consent of all those participating, and I understand that if I violate this prohibition, I will be subject to discipline by Dartmouth up to and including separation from Dartmouth, as well as any other civil or criminal penalties under applicable law. I understand that an exception to this consent applies to accommodations approved by SAS for a student’s disability, and that one or more students in a class may record class lectures, discussions, lab sessions, and review sessions and take pictures of essential information, and/or be provided class notes for personal study use only.

---

## Credits

The materials used in this course are derived from those designed by
Professors Balkcom, Campbell, Kotz, Palmer, Zhou, and others.
