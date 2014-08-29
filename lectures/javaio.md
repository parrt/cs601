Java I/O
=====

## Introduction

The java.io.* provides classes that read and write data from/to streams sequentially or in random-access fashion. The data may exist in a file or an array, be piped from another stream, or even come from a port on another computer. The data can be compressed/uncompressed during writing/reading even. The flexibility of this model makes it a powerful abstraction of any required input and output.

Java provides two sets of classes for reading/writing. The OutputStream/InputStream class hierarchy is for bytes, data primitives, and objects, while the Reader/Writer hierarchy is for writing character and text strings. Which classes you use depend upon your specific needs.

Methods throw `IOException` or a subclass thereof when there is a problem such as "can't find the file": `FileNotFoundException`.

## Files

You can use File objects to obtain information about a file or directory. File objects differ from stream objects such as InputStream in that a File object is used to obtain information about a particular file or directory and is not used to read or write data. A File object is a "file handle" or descriptor, not the contents of the file nor a stream that can read/write to the file.

To determine the length of a file and print its full path:

```java
import java.io.File;
import java.io.IOException;

public class DemoFile {
	public static void main(String[] args)  throws IOException {
		String fileName = args[0];
		File f = new File(fileName);
		System.out.println("file " + fileName + " is " +
						   f.length() + " bytes long");
		System.out.println("Path: " + f.getCanonicalPath());
	}
}
```

For another example, when the File object is associated with a file system directory, you can easily obtain the list of files in that directory:

```java
import java.io.File;
import java.io.IOException;
import java.util.Arrays;

public class DemoDir {
	public static void main(String[] args)  throws IOException {
		String fileName = args[0];
		File f = new File(fileName);
		String dirListing[] = f.list();
		System.out.println(Arrays.toString(dirListing));
	}
}
```

In addition to getting information about a file from a File object, you can use one to create an instance of FileInputStream, FileReader, FileOutputStream or FileWriter. The following example writes a single line to a file.

```java
File f = new File(fileName);
FileWriter fw = new FileWriter(f);
```

### OS-independent directory naming

The above example uses UNIX file names. We can create similar File objects for Windows files as follows:

```java
File c = new File("c:\\windows\\system\\smurf.gif");
File d = new File("system\\smurf.gif");
```

Note the double backslashes. Because the backslash is a Java String escape character, you must type two of them to represent a single, "real" backslash.

The above specifications are not very portable. The problem is that the direction of the slashes and the way the "root" of the path is specified is specific for the platform in question. Fortunately, there are several ways to deal with this issue.

First, Java allows either type of slash to be used on any platform, and translates it appropriately. This means that you could type

```java
File e = new File("c:/windows/system/smurf.gif");
```

and it will find the same file on Windows. However, we still have the "root" of the path as a problem.

The easiest solution to deal with files on multiple platforms is to always use relative path names. A file name like

```java
File f = new File("images/smurf.gif");
```

will work on any system. That will look for the images directory in the current working directory (the directory where you started the Java application).

If full path names (including drive specifications) are required (there are some methods of obtaining a list of available devices).

Finally, you can create files by specifying two parameters: the name (String or File) of the parent directory and the simple name of the file in that directory. For example:

```java
File g = new File("/windows/system");
File h = new File(g, "smurf.gif");
File i = new File("/windows/system", "smurf.gif");
```

will create objects for h and i that refer to the same file.

## Sequential Files

### Writing

#### Data

If you want to write primitive data types to a file, you would combine DataOutputStream and FileOutputStream in the following manner:

```java
import java.io.DataOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class WriteData {
	public static void main(String[] args)  throws IOException {
		FileOutputStream fos = new FileOutputStream("/tmp/junk.data");
		DataOutputStream dos = new DataOutputStream(fos);
		dos.writeInt (42);
		dos.writeDouble (Math.PI);
		dos.close();
	}
}
```

In many cases, you'll want to buffer that output stream for efficiency's sake (i.e., don't write to the physical disk for each "write").

```java
import java.io.BufferedOutputStream;
import java.io.DataOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class BufferedWriteData {
	public static void main(String[] args)  throws IOException {
		FileOutputStream f = new FileOutputStream("/tmp/junk.data");
		BufferedOutputStream bf = new BufferedOutputStream(f);
		DataOutputStream dos = new DataOutputStream(bf);
		dos.writeInt(34);
		dos.close();
	}
}
```

Java provides two static objects in class System for sending output to standard output (stdout) and standard error (stderr):

```java
static PrintStream out;
static PrintStream err;
```

Methods `print()` and `println()` are used to print (only) ASCII text representations of their arguments. For example,

```java
System.out.println("Hello");
```

If you are using a UNIX machine, you can look at the binary data within a file using `od`:

```bash
$ od -x /tmp/junk.data     # hex 32-bit words
0000000      0000    2a00    0940    fb21    4454    182d
0000014
$ od -t x1 /tmp/junk.data  # hex bytes
0000000    00  00  00  2a  40  09  21  fb  54  44  2d  18
0000014
$ od -t d1 /tmp/junk.data  # decimal bytes
0000000     0   0   0  42  64   9  33  -5  84  68  45  24
0000014
$
```

#### Text

To write ASCII (8-bit not using an encoding) text to a file, use the OutputStream hierarchy:

```java
FileOutputStream out = new FileOutputStream("somefile");
PrintStream pout = new PrintStream(out);
pout.println("first line");
pout.close();
```
For portable writing of text, use the Writer hierarchy.

```java
FileWriter fw = new FileWriter(...);
BufferedWriter bw = new BufferedWriter(fw);
PrintWriter pw = new PrintWriter(bw);
pw.println("some text");
pw.close();
```

FileWriter assumes the default character encoding for your Locale. Encodings are used when converting between raw 8-bit bytes and 16-bit Unicode characters. For example, the default file character encoding for a US computer is `UTF-8`. On a Japanese machine, the encoding might be `euc-jp`. See below for more about Unicode and encodings.

### Reading

#### Data

To read primitive data types from a file, you would combine DataInputStream and FileInputStream in the following manner:

```java
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.IOException;

public class ReadData {
	public static void main(String[] args)  throws IOException {
		FileInputStream fis = new FileInputStream("/tmp/junk.data");
		DataInputStream dis = new DataInputStream(fis);
		int fortyTwo = dis.readInt();
		double pi = dis.readDouble();
		dis.close();
		System.out.printf("read %d and %1.5f\n", fortyTwo, pi);
	}
}
```


#### Text

To read text from stdin, things are a little weird due to Java I/O libraries being updated and having to maintain backward compatibility:

```java
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class ReadText {
	public static void main(String[] args) throws IOException {
		String fileName = args[0];
		InputStreamReader isr = new InputStreamReader(System.in);
		BufferedReader reader = new BufferedReader(isr);
		String line = reader.readLine();
		while (line != null) {
			// Process line
			line = reader.readLine();
		}
	}
}
```

If you are reading ASCII characters like the digits 0..9 and only want to read character by character, then you can directly use `System.in.read()`.

### Unicode

#### Native character codes versus Unicode

All text held in memory is represented as two-byte UNICODE characters. UNICODE is a standard that allows characters from character sets throughout the world to be represented in two bytes. Characters 0-127 of the UNICODE standard map directly to the ASCII standard. The rest of the character set is composed of "pages" that represent other character sets. The trick of course, is that each platform has its own native character set, which usually has some mapping to the UNICODE standard. Java needs some way to map the native character set to UNICODE.

Java's text input and output classes translate the native characters to and from UNICODE. For each delivered JDK, there is a "default mapping" that is used for most translations. The default file character encoding for a US computer is `UTF-8`. Ona Japanese machine, the encoding might be `euc-jp`.

#### Stream character encoding of Unicode

If we can assume that the native-Unicode just "works", the only issue you really need to worry about is how 16-bit characters are read or written, whether it's to a file, the terminal, or a socket. If you are storing a 16-bit Unicode character, you must somehow encode it as a sequence of bytes.

The most common file encoding is [UTF-8](http://www.fileformat.info/info/unicode/utf8.htm), "unicode to follow", which is much more efficient than just storing the 16 bit characters sequential. UTF-8 is a simple encoding of UNICODE characters and strings that is optimized for the ASCII characters. In each byte of the encoding, the high bit determines if more bytes follow. A high bit of zero means that the byte has enough information to fully represent a character; ASCII characters require only a single byte. From [UTF-8](http://www.fileformat.info/info/unicode/utf8.htm):


1st Byte |   2nd Byte  |  3rd Byte  |  4th Byte  |  Number of Free Bits | Maximum Expressible Unicode Value
---------|---------|---------|---------|---------|---------
0xxxxxxx |  |||              7 |  007F |hex (127)
110xxxxx |   10xxxxxx |     |     | (5+6)=11  |  07FF hex (2047)
1110xxxx |   10xxxxxx  |  10xxxxxx   |     |(4+6+6)=16|  FFFF hex (65535)
11110xxx  |  10xxxxxx   | 10xxxxxx  |  10xxxxxx   | (3+6+6+6)=21 |   10FFFF hex (1,114,111)

Regardless, the key here is that you read characters the same way they were written. The encoding will become important when you start working with sockets between different computers. The locale on the client and server may be different.

As we will see when discussing the HTTP protocol, servers can send back headers that are essentially properties. One of the properties that browsers look for describes the encoding of the data coming back from the server. Our computer science Web server, for example, response to page fetches with header (among other things):

```
content-type=text/html; charset=UTF-8
```

Bottom line. If you are reading text from a file, you should be using the Reader I/O hierarchy, which will sense your "locale" and interpret a text file properly. Be careful that you do not get a file encoded and stored from another country and then try to open with a "native ASCII format" computer such as a computer in the USA. It will try to interpret the text as UTF-8 instead of the original encoding. Even if that encoding is only eight bits per character, it might have different mapping from characters to integer character values.

```java
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;

public class FileEncodingRead {
	public static void main(String[] args) throws IOException {
		String encoding = args[0];
		String fileName = args[1];
		FileInputStream isr = new FileInputStream(fileName);
		Reader r = new InputStreamReader(isr, encoding);
		BufferedReader br = new BufferedReader(r);
		String data = readFully(br);
		System.out.println(data);
	}

	public static String readFully(BufferedReader r) throws IOException {
		StringBuilder buf = new StringBuilder();
		String line = r.readLine();
		while (line != null) {
			buf.append(line);
			line = r.readLine();
		}
		return buf.toString();
	}
}
```

## Random Access Files

You often want to read or write data at random positions within a file, rather than sequentially as you would with a magnetic tape. The `RandomAccessFile` class behaves like a combined `DataOutputStream` and `DataInputStream`. RandomAccessFile implements both the DataOutput and DataInput interfaces. RandomAccessFile objects are created from a String filename or File object like other stream objects, but a mode constructor argument is also required. The mode is either String "r" (read only) or "rw" (read/write), just like fopen() in C or C++. For example:

```java
// open file junk for reading and writing
RandomAccessFile f = new RandomAccessFile("junk", "rw");
// open crud for reading only
RandomAccessFile ff = new RandomAccessFile("crud", "r");
```

To append information to a RandomAccessFile, seek to the end of the file:

```java
f.seek(f.length());
```

The beginning of a file is considered position 0. Therefore, to seek to the beginning of a file, use:

```java
f.seek(0);
```

The following example writes an integer and a floating point number to a file, rewinds the file, and reads them back:

```java
import java.io.IOException;
import java.io.RandomAccessFile;

public class RandomAccessData {
	public static void main(String[] args)  throws IOException {
		RandomAccessFile f = new RandomAccessFile("/tmp/junk.data", "rw");
		f.writeInt(34);
		f.writeDouble(3.14159);
		f.seek(0);  // rewind and read again
		int i = f.readInt();
		double d = f.readDouble();
		f.close();
		System.out.printf("read %d and %1.5f\n", i, d);
	}
}
```

RandomAccessFile f = new RandomAccessFile("junk", "rw");
f.writeInt(34);
f.writeDouble(3.14159);
f.seek(0);
int i = f.readInt();
double d = f.readDouble();
f.close();

## Object Serialization

Similar to primitive value input and output, object values can be written to binary files as well. Writing an object value to an output stream is known as "serializing" that object. The Java core API defines interfaces ObjectInput and ObjectOutput. These interfaces define the readObject() and writeObject() methods, the core of Java object serialization. Two classes, ObjectInputStream and ObjectOutputStream implement these interfaces.

Another interface, Serializable, is used to "mark" a class as one that can be handled via writeObject() and readObject(). Note that Serializable defines no methods; it is simply a "tag" to indicate that a class may be serialized.

## Properly Closing Files

The WRONG way to close a file:

```java
try {
  FileReader r = new FileReader("somefile.txt");
  // read some text from the file
  r.close();
}
catch(IOException e) {
  // display a message about the error
}
Any exception before the close() will skip the file close!

Here is the RIGHT way:

FileReader r = null;
try {
  r = new FileReader("somefile.txt");
  // read some text from the file
  // NOTE: No close() here!
}
catch(IOException e) {
  // display a message about the error
}
finally {
  if (r != null) {
    try {
      r.close();
    }
    // tried to close but couldn't anyway!
    // should inform the user if the data was important... 
    catch(Exception ignoreMe) {}
  }
}
```

This ensures that no matter what happens, we at least try to close the file.