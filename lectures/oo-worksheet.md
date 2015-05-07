Object-Oriented Principles Worksheet
====
This worksheet explores your understanding of the fundamental principles behind object-oriented programming including dynamic binding, polymorphism, data hiding, and inheritance. Express your answers using Java syntax.

**1. Can I make an instance of an Animal?**
```java
abstract class Animal {
  ...
  public abstract void speak();
}
```
Will the following definition of Tiger compile without error?
```java
class Tiger extends Animal {
  public void scratch() {;}
  public String getName() { return "tigger"; }
}
```

**2. Considering Tiger and the Animal class, which class is the subclass and which is the superclass?**

**3. What is the difference between an instance of Tiger and the class Tiger? Does the following makes sense and compile properly?**

```java
  String n = Tiger.getName();
```

**4. Can I redefine Tiger to make the speak() method private like the following? Why or why not?**
```java
class Tiger extends Animal {
  ...
  private void speak() {
    System.out.println("roar!");
  }
}
```

**5. Assume Dog and Cat are subclasses of Animal. Which of the following assignments will result in compile-time warnings?**
```java
  Animal a = ... ;
  Dog d = ... ;
  Cat c = ... ;

  a = d;
  a = (Animal)d;
  d = a;
  d = (Dog)a;
  c = d;
  d = c;
```

**6. Which method is called in each of the following:**
```java
Bag b = ...;
b.insert("some string");
b.insert(3);
b.insert("3");
given the following:
class Bag {
  ...
  public void insert(String s) {...}
  public void insert(int i) {...}
  public void insert(float f) {...}
}
```

**7. Consider the following class definitions:**
```java
class Human {
	String name;
	...
	public String getName() {
		return name;
	}
	public String getInfo() {
		return getName();
	}
}
class Student extends Human {
	String ID;
	...
	public String getName() {
		return name+":"+ID;
	}
}
...
Student s = new Student("Madonna","111-22-3333");
Human h = s;
```
 * What does s.getName() return?

 * What does h.getName() return?

 * What does s.getInfo() return?

 * What does h.getInfo() return?

**8. Which of the following assignments result in compile-time errors? Will any assignments compile properly, but eventually lead to run-time errors (ClassCastException)?**

```java
Airplane a = ...;
Bird b = ...;
FlyingSquirrel s = ...;

CanFly f;

f = a;
f = b;
f = s;
f = (CanFly)s;
a = b;
```
Assume the following definitions:

```java
interface CanFly {
  public void fly();
}

class Airplane implements CanFly {
  public void fly() {...}
}

class Bird implements CanFly {
  public void fly() {...}
}

class FlyingSquirrel {
  public void fly() {...}
}
```

**9. Can a method of an interface be private? **

**10. In method reset(), what is "x = 0" shorthand for? I.e., what is the fully qualified name for "x"?**

```java
class Point {
  int x,y;
  public void reset() {
    x = 0;
    y = 0;
  }
}
```

**11. Can two individual employee objects refer to two different company names given the following class? Can they each have a different ID?**

```java
class Employee {
    String ID;
    static String companyName;
    static public void setCompanyName(String n) {
        companyName = n;
    }
    public void getID() {
        return ID;
    }
    ...
}
```

 * Could getID() refer to method setCompanyName()?
 * Could setCompanyName() refer to getID()? 

**12. Is the following class method definition equivalent/valid to the above definition?**

```java
  static public void setCompanyName(String n) {
    this.companyName = n;
  }
```