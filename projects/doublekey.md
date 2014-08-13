#  Double key hash table

## Discussion

This project tests your programming ability, your ability to code in Java, your knowledge of simple data structures, and how well you can read and follow instructions. This project is a typical undergraduate project, but with a shorter deadline. If this project is not easy for you, this course may overwhelm you. This project is a good way for us to get to know each other. :)

Your  task is to produce a simple Java implementation of a dictionary interface similar to Java's built-in ```Map<Key,Value>``` interface. The interface is called ```DoubleKeyMap<Key1,Key2,Value>``` (attached below) and the implementation is called ```DoubleKeyHashMap<Key1,Key2,Value>```.

You should test your your code well including all boundary conditions as you will not see the test harness I will use to check out your project.

You must implement this project successfully without knowing precisely how I will test it. There are many reasons:

* Part of writing software is thinking about and implementing tests for your own work.
* You must get used to the idea that you do not know how other programmers will use your objects. Programmers communicate by examining the collection of objects and the list of messages they answer.

This project is primarily meant to test your basic Java skills:

* basic class construction and usage
* statements and expressions
* object references
* simple data structure construction
* interfaces
* generics
* compilation and jar'ing

*IMPORTANT*: You are to actually implement a hashtable using an array of ```LinkedList<T>``` containing wrapper objects that wrap the 2 keys and the value using a technique called [separate chaining](http://en.wikipedia.org/wiki/Hash_table#Separate_chaining_with_linked_lists). Do not simply wrap Java's existing ```Map``` interface.  For example, my solution uses a helper class like this:

```
/** An implementation of DoubleKeyMap that uses a hash table. It works
 * just like HashMap in that the keys are used to pick a bucket of
 * elements.
 */
public class DoubleKeyHashMap<K1,K2,V>
    implements DoubleKeyMap<K1,K2,V>
{
    class Entry { K1 key1; K2 key2; V value; ... }
}
```

## Requirements

The exact external behavior of your classes should follow the javadoc comments in the interface definition.
Your hash table should be a fixed size array of "buckets" that stores all key/value pairs with the same hash value.
Order matters for the keys. key1,key2 is different than key2,key1.
For your hash function, you can use:

```
key1.hashCode() << 7 + key2.hashCode()
```

Your implementation class must define a constructor with no arguments.

All of your classes must be in the default package. Do not move DoubleKeyMap into a package.

Do not generate any output from your library. Only test harnesses should generate output. In other words, do not leave any debugging println statements around. Do not print error messages upon invalid keys and so on.

You should test your library extensively (using junit if you know it)

## Submission

You will create a jar file called map.jar containing *.class files and place in a directory called map/dist under your cs680 dir:

https://www/svn/userid/cs680/map/dist/map.jar
Pur your source Java code in map/src:

https://www/svn/userid/cs680/map/src/DoubleKeyHashMap.java
When testing, i'll use my own DoubleKeyMap.

To jar your stuff up, you will "cd" to the directory containing your source code (perhaps map/src) and create the jar in the map dir:

```
cd ~/cs680/map/src
jar cvf ~/cs680/map/dist/map.jar *.class
cd ~/cs680/map/dist
svn add map.jar
svn commit
```

To learn more about submitting your project with svn, see Resources.

You will see something like this:

adding: DoubleKeyHashMap$Entry.class(in = 1045) (out= 569)(deflated 45%) 
adding: DoubleKeyHashMap.class(in = 2532) (out= 1140)(deflated 54%)
adding: DoubleKeyMap.class(in = 651) (out= 297)(deflated 54%)
You should test your project by running as I will run it per the Grading section below.

You are creating a library, NOT a main program.  Your code should not generate any output.  You must submit your source code for credit.

## Grading

I will run your program by pulling your map.jar file from the repository and running like this:

java -cp .:map.jar TerencesTestRig
It will launch my TerencesTestRig.main() method and I will check the results.

Here's an example of how to use the new map interface:

```
DoubleKeyMap<String,Integer,Double> m =
    new DoubleKeyHashMap<String, Integer, Double>();
m.put("hi",32,99.2);
double d = m.get("hi",32);
System.out.println(d); // should be 99.2
System.out.println(m.containsKey("hi",32)); // should be true
System.out.println(m.containsKey("hy",3));  // should be false
```

You may discuss this project in its generality with anybody you want and may look at any code on the internet except for a classmate's code. You should physically code this project completely yourself but can use all the help you find other than cutting-n-pasting or looking at code from a classmate or other Human being.

I will deduct 10% if your program is not executable exactly in the fashion mentioned in the project; that is, class name, methods, lack-of-package, and jar must be exactly right. For you PC folks, note that case is significant for class names and file names on unix! All projects must run properly under linux.  That means it's a good idea to test it there yourself.
