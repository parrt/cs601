Testing and test-driven design
====

# Definitions

*Unit test*: A test typically a method or a fairly shallow method chaining. For a calculator application, this might test that an `add(x,y)` method.

*Functional test*: A test that checks the overall functionality of an application. For calculator, this might check that an input string `10+20` yields `30`.

Another example: testing a method that returns large prime numbers is a unit test but testing the overall encryption algorithm that uses that method is a functional test.

I care more for functional tests not unit tests and most of the time when we say unit test we actually mean functional test, but unit test is easier to say so it's kind of become the generic term.

There some truth in the statement that actual unit tests are really just assertions. For example, you could have a postcondition in `add` instead of a unit test elsewhere:

```java
int add(int x, int y) {
	z = x + y
	assert z == x + y
}
```

Let's get one thing straight before you begin:  **Testing itself doesn't improve quality; it just tells you how screwed you are.**

# Introduction

Unlike mathematics where you can prove that a result is correct, there is no general way to guarantee software is correct.  You just have to try out the software under as many conditions as possible to see if it fails.  Fix the bug(s).  Repeat.

So, we test because Humans have very limited brains and we make lots of mistakes, particularly as the number of lines of code and number of programmers goes up.  The complexity goes up in a highly nonlinear fashion.

Besides increasing the quality of the user experience, less buggy software means you will be awakened less often at 3AM because the server has crashed.  More robust software is less expensive to modify and maintain.  Maintenance is the vast majority of the effort when developing software.  Unlike school projects that you never look at again after the end of the semester, real projects live for years or decades.  Somebody has to deal with your bugs for a long time--possibly you!

From [OSCON](https://twitter.com/andypiper/status/490952891058757632):

<img src=https://pbs.twimg.com/media/BtA3Cr0CAAAVkck.jpg width=300>

Programmers will often avoid writing tests and performing tests because it is a hassle.  jUnit (brought to you by the _extreme programming_ folks that emphasize "testing early, testing often") removes the hassle by providing a very simple set of Java classes that helps you build unit tests.  It provides the framework for specifying tests and includes a nice GUI for running the tests and collecting results. Nowadays, this is all integrated into development environments. E.g.,

<img src=figures/testsets.png width=400>

## Why do we automate testing?

While some applications like GUIs often require human testers, you should automate your tests so that you can run them easily.  If you can run them easily and automatically, they can then be integrated into the build process.  

Test often so that you can find problems right away: this usually tells you precisely what you screwed up.  You want to shrink the time between a change to the code and when you notice a problem because it helps you isolate which change or part of the change has screwed up the software.

A friend at Microsoft told me that they run all tests on a giant server "build farm" at night and if you have submitted something that fails tests or doesn't compile (_shudder_), they can ask you to come in early to fix it so it doesn't hold up the whole team. 

Automated tests, therefore, let you code with confidence; confidence that you will know when/if you break something when adding new functionality or refactoring.  Here is a coding truism: _A confident programmer is a fast coder_!

There is nothing more terrifying than modifying software that has no tests because you don't know what you might be breaking.  You can't clean up the code and you can't extend it.  My parser generator ANTLR v2 is like this.  I literally refuse to alter its grammar analyzer or code generator for fear of breaking the programs of the many ANTLR users around the world.

In contrast, there is nothing quite like the pleasure of seeing all of your unit tests light up green in the jUnit GUI.  It really does give you a good feeling. :)

## What else can testing help?

A bunch of tests often provides better "documentation" of your code's functionality than does your real printed text documentation.  At the very least, the tests will be up-to-date versus your documentation and less likely to be misleading. Programmers tend to update tests more than they update comments or documentation.

## What do you test?

As much as you can given the time constraints and the nature of your software.  Make sure that you test not only good input, but bad input as well.  With bad input make sure your software not only catches it but executes the appropriate cleanup or error code.  Another good idea is to try the boundary conditions. For example, a method that looks at an array, try to get it to hit the last element or first element of the array.  Also try on either side of the boundaries.

Warning: Don't break function / structure just to get access to code for a test. In other words, don't drill a hole in a microchip just so that you can get testing lead on one of the elements.

## How do I integrate testing into my debugging process?

First, build the test that reproduces the bug.  Watch it fail.  *Then* work on the software until that unaltered test passes.  Do not fix the software and then build a test that passes! You must positively absolutely see the test go from failure to passing.

Making a test for each bug is a great idea because errors tend to repeat themselves.  Also the more tests you have, in principle, your software will get more and more robust with time.

Submitting a bug report with the unit test is courteous and increases the likelihood the bug will be fixed and decreases the delay before programmers attempt a fix.

## What does testing cost?

Testing is often more work than the actual application.  Further, it often forces you to refactor your application to create testing hooks so that your testing harness can talk to your application.  In my experience, adding testing hooks leads to better organized and more general code.

Testing can take some work in the near term, but in the long run you are faster and get better, more maintainable code.

Ok, let's dive into testing.

#### The World Before jUnit

Imagine that you have a `Calculator` class and you would like to perform some unit tests on the methods.  You could make a `main` method that does the testing:

```java
public class Calculator {
    public int add(int x, int y) { return x+y; }
    public int mult(int x, int y) { return x*y; }

    public static void main(String[] args) {
        Calculator calc = new Calculator();
        // test add
        if ( calc.add(3,4)!=7 ) { System.err.println("error: add(3,4)"); }
        if ( calc.add(0,4)!=4 ) { System.err.println("error: add(0,4)"); }
        ...
    }
}
```

You can run your test very simply by just running that class:

```bash
$ java Calculator
```

The problem is that your test code will get deployed with the rest of your code.  This is inefficient so you could just cut-n-paste this code into a separate class:

```java
class TestCalculator {
    public static void main(String[] args) {
        Calculator calc = new Calculator();
        // test add
        if ( calc.add(3,4)!=7 ) { System.err.println("error: add(3,4)"); }
        if ( calc.add(0,4)!=4 ) { System.err.println("error: add(0,4)"); }
        ...
    }
}
```

This leaves {Calculator} clean and properly encapsulated.  Now you would run your tests via:

```bash
$ java TestCalculator
```

These tests properly identify which test has failed (if any), but the messages are not great and the IFs make the test code hard to read.  Factoring out the test harness code is a good idea:

```java
class TestCalculator {
    public static void main(String[] args) {
        Calculator calc = new Calculator();
        // test add
        assertEquals(calc.add(3,4),7,"add(3,4)");
        assertEquals(calc.add(0,4),4,"add(0,4)");
        ...
    }

    public static void assertEquals(int result, int expecting, String msg) {
        if ( result!=expecting ) {
            System.err.println("failure: "+msg+"="+result+";
                expecting="+expecting);
        }
    }
}
```

Now, when a test fails, you'll see something like

```bash
failure: add(3,4)=932; expecting=7
```

What if you want multiple tests?  You could just include them in the {main}:

```java
class TestCalculator {
    public static void main(String[] args) {
        Calculator calc = new Calculator();
        // test add
        assertEquals(calc.add(3,4),7,"add(3,4)");
        assertEquals(calc.add(0,4),4,"add(0,4)");
        ...

        // test mult
        assertEquals(calc.mult(0,4),0,"mult(0,4)");
        ...
    }

    public static void assertEquals(int result, int expecting, String msg) {
        ...
    }
}
```

The problem is that you will get a really big main method and you have to comment out code to test a subset of them.  You can extract methods, cleaning up the class a bit also:

```java
class TestCalculator {
  Calculator calc;

  public void testAdd() {
    assertEquals(calc.add(3,4),7,"add(3,4)");
    assertEquals(calc.add(0,4),4,"add(0,4)");
  }

  public void testMult() {
    assertEquals(calc.mult(0,4),0,"mult(0,4)");
  }

  public static void main(String[] args) {
    TestCalculator tester = new TestCalculator();
    tester.setUp();
    tester.testAdd();
    tester.testMult();
    tester.tearDown();
  }

  // support code

  public void assertEquals(int result, int expecting, String msg) {
    ...
  }

  public void setUp() { calc = new Calculator(); }
  public void tearDown() { calc=null; }
}
```

In fact, you can pass in a method name to test as an argument:

```java
class TestCalculator {
  ...

  public static void main(String[] args) {
    TestCalculator tester = new TestCalculator();
    tester.setUp();
    if ( args[0].equals("add") ) {
      testAdd();
    }
    ...
  }
}
```

Runnning a test is done via:

```bash
$ java TestCalculator add
```

Congratulations, you just re-invented what jUnit automates for you.

#### Getting Started

First, make sure that your `CLASSPATH` or your IDE configuration has `junit.jar` and `hamcrest-core.jar` downloaded from:

https://github.com/junit-team/junit/wiki/Download-and-Install.

Then, all you have to do is import a bunch of useful methods and an annotation:

```java
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
public class TestXXX {
	@Test public void testYYY() { }
}
```

Here is the `Calculator` class by itself, which you will be testing with your test suite:

```java
public class Calculator {
    public int add(int x, int y) { return x+y; }
    public int mult(int x, int y) { return x*y; }
}
```

Here is a simple `TestCalculator` that uses jUnit instead of your own testing facility:

```java
import junit.framework.TestCase;

public class TestCalculator extends TestCase {
	private Calculator calc;

	@Before  // before each test
	public void setUp() { calc = new Calculator(); }

	@After
	public void tearDown() { calc = null; }

	@Test public void testAdd() {
		assertEquals("testing add(3,4)", 7, calc.add(3,4));
	}
}
```

To run the test, just compile and invoke the {TestRunner}:

```bash
$ javac TestCalculator.java
$ java org.junit.runner.JUnitCore TestCalculator 
JUnit version 4.8.2
.
Time: 0.007

OK (1 test)
```

#### Failed Tests

What happens when jUnit finds a failed test?  To check that out, change the test to have a bad return value:

```java
    public void testAdd() {
        assertEquals("testing add(3,4)", 8, calc.add(3,4));
    }
```

When you run the test, you will see the failure notice:

```bash
$ java org.junit.runner.JUnitCore TestCalculator 
JUnit version 4.8.2
.E
Time: 0.012
There was 1 failure:
1) testAdd(TestCalculator)
java.lang.AssertionError: testing add(3,4) expected:<8> but was:<7>
	at org.junit.Assert.fail(Assert.java:91)
	at org.junit.Assert.failNotEquals(Assert.java:645)
	at org.junit.Assert.assertEquals(Assert.java:126)
	at org.junit.Assert.assertEquals(Assert.java:470)
	at TestCalculator.testAdd(TestCalculator.java:20)
...
```

#### Running junit from within code

If you have lots of test suites to run and/or you would like to run junit from a java program instead of the command line, just execute the {run} method:

```java
public static void main(String[] args) {
    org.junit.runner.JUnitCore.runClasses(TestCalculator.class);
}
```

# Code coverage

A related topic to unit testing is *code coverage*, which basically indicates how many times each line in a program is exercised during a particular run. If we do code coverage during unit testing it tells us how much of our code is exercised by our unit tests.

Code coverage only says that you have visited that line but remember there could be trillions of possible memory states possible for every given line of program

But, remember to watch out for false precision. Just because you touch 80% of the lines of your program doesn't mean that you have some kind of 80% testing efficiency or have tested 80% of your application. Remember that the vast majority of the time executing the program will be spent in a few hotspots. Those are the areas that you should test the crap out of.

An interesting quote from [James Coplien](http://www.rbcs-us.com/documents/Why-Most-Unit-Testing-is-Waste.pdf):

<blockquote>
I had a client in northern Europe where the developers were required to have 40% code coverage for Level 1 Software Maturity, 60% for Level 2 and 80% for Level 3, while some where aspiring to 100% code coverage.
</blockquote>

Don't chase metrics. can't ever really get 100% anyway. You can only asymptotically approach 100% coverage.


