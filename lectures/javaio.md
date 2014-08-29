Java I/O
=====

## Introduction
The java.io.* provides classes that read and write data from/to streams sequentially or in random-access fashion. The data may exist in a file or an array, be piped from another stream, or even come from a port on another computer. The data can be compressed/uncompressed during writing/reading even. The flexibility of this model makes it a powerful abstraction of any required input and output.

Java provides two sets of classes for reading/writing. The OutputStream/InputStream class hierarchy is for bytes, data primitives, and objects, while the Reader/Writer hierarchy is for writing character and text strings. Which classes you use depend upon your specific needs.

Methods throw IOException or a subclass thereof when there is a problem such as "can't find the file": FileNotFoundException.

Files
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

String fileName = "data";
File f = new File(fileName);
System.out.println("file " + fileName + " is " +
      f.length() + " bytes long");
System.out.println("Path: " + f.getCanonicalPath());

For another example, when the File object is associated with a file system directory, you can easily obtain the list of files in that directory:

String fileName = "examples";
File f = new File(fileName);
String dirListing[] = f.list();
In addition to getting information about a file from a File object, you can use one to create an instance of FileInputStream, FileReader, FileOutputStream or FileWriter. The following example writes a single line to a file.

String fileName = "/tmp/more-examples";
File f = new File(fileName);
FileWriter fw = new FileWriter (f);
PrintWriter pw = new PrintWriter (fw);
pw.println("Welcome home");
pw.close();
OS-independent directory naming
The above example uses UNIX file names. We can create similar File objects for Windows files as follows:

File c = new File("c:\\windows\\system\\smurf.gif");
File d = new File("system\\smurf.gif");
Note the double backslashes. Because the backslash is a Java String escape character, you must type two of them to represent a single, "real" backslash.

The above specifications are not very portable. The problem is that the direction of the slashes and the way the "root" of the path is specified is specific for the platform in question. Fortunately, there are several ways to deal with this issue.

First, Java allows either type of slash to be used on any platform, and translates it appropriately. This means that you could type

File e = new File("c:/windows/system/smurf.gif");
and it will find the same file on Windows. However, we still have the "root" of the path as a problem.

The easiest solution to deal with files on multiple platforms is to always use relative path names. A file name like

File f = new File("images/smurf.gif");
will work on any system. That will look for the images directory in the current working directory (the directory where you started the Java application).

If full path names (including drive specifications) are required (there are some methods of obtaining a list of available devices).

Finally, you can create files by specifying two parameters: the name (String or File) of the parent directory and the simple name of the file in that directory. For example:

File g = new File("/windows/system");
File h = new File(g, "smurf.gif");
File i = new File("/windows/system", "smurf.gif");
will create objects for h and i that refer to the same file.

Sequential Files
Writing
Data
If you want to write primitive data types to a file, you would combine DataOutputStream and FileOutputStream in the following manner:

String filename = ...;
FileOutputStream fos = new FileOutputStream (filename);
DataOutputStream dos = new DataOutputStream (fos);
dos.writeInt (42);
dos.writeDouble (Math.PI);
dos.close();
In many cases, you'll want to buffer that output stream for efficiency's sake (i.e., don't write to the physical disk for each "write").

FileOutputStream f = new FileOutputStream("junk");
BufferedOutputStream bf = new BufferedOutputStream(f);
DataOutputStream dos = new DataOutputStream (bf);
dos.writeInt(34);
dos.close();
Java provides two static objects in class System for sending output to standard output (stdout) and standard error (stderr):

static PrintStream out;
static PrintStream err;
Methods print() and println() are used to print (only) ASCII text representations of their arguments. For example,

System.out.println("Hello");
Text
To write ASCII (8-bit non UNICODE) text to a file, use the OutputStream hierarchy:

FileOutputStream out = new FileOutputStream("somefile");
PrintStream pout = new PrintStream(out);
pout.println("first line");
pout.close();
For portable writing of text, use the Writer hierarchy.

FileWriter fw = new FileWriter(...);
BufferedWriter bw = new BufferedWriter(fw);
PrintWriter pw = new PrintWriter(bw);
pw.println("some text");
pw.close();
FileWriter assumes the default character encoding for your Locale. Encodings are used when converting between raw 8-bit bytes and sixteen-bit Unicode characters. For example, the encoding for a US computer is "US-ASCII" also known as "ISO646-US". See below for more about Unicode.

Reading
Data
To read primitive data types from a file, you would combine DataInputStream and FileInputStream in the following manner:

FileInputStream fis = new FileInputStream(filename);
DataInputStream dis = new DataInputStream(fis);
int fortyTwo = dis.readInt();
double pi = dis.readDouble();
dis.close();
Text
To read text from stdin, things are a little weird due to Java I/O libraries being updated and having to maintain backward compatibility. From jGuru How do I read text from standard input?:

InputStreamReader isr = new InputStreamReader(System.in);
BufferedReader reader = new BufferedReader(isr);
String line = reader.readLine();
while ( line!=null ) {
  // Process line
  line = reader.readLine();
}
If you are reading ASCII characters like the digits 0..9 and only want to read character by character, then you can directly use System.in.read().

Unicode
Native character codes versus Unicode
All text held in memory is represented as two-byte UNICODE characters. UNICODE is a standard that allows characters from character sets throughout the world to be represented in two bytes. Characters 0-127 of the UNICODE standard map directly to the ASCII standard. The rest of the character set is composed of "pages" that represent other character sets. The trick of course, is that each platform has its own native character set, which usually has some mapping to the UNICODE standard. Java needs some way to map the native character set to UNICODE.

Java's text input and output classes translate the native characters to and from UNICODE. For each delivered JDK, there is a "default mapping" that is used for most translations.

Stream character encoding of Unicode
If we can assume that the native-Unicode just "works", the only issue you really need to worry about is how 16-bit characters are read or written. If you are storing a 16-bit Unicode character, you must somehow encode those as a sequence of bytes. The most common way is "UTF-8", "unicode to follow", which is much more efficient than just storing the 16 bit characters sequential. UTF-8 is a simple encoding of UNICODE characters and strings that is optimized for the ASCII characters. In each byte of the encoding, the high bit determines if more bytes follow. A high bit of zero means that the byte has enough information to fully represent a character; ASCII characters require only a single byte.

Regardless, the key here is that you read characters the same way they were written. The encoding will become important when you start working with sockets between different computers. The locale on the client and server may be different.

Bottom line. If you are reading text from a file, you should be using the Reader I/O hierarchy, which will sense your "locale" and interpret a text file properly. Be careful that you do not get a file stored in 2-byte unicode from another country and then try to open with a "native ASCII format" computer such as a computer in the USA. It will try to interpret the text as UTF-8 instead of a stream of 16-bit characters!

Random Access Files
You often want to read or write data at random positions within a file, rather than sequentially as you would with a magnetic tape. The RandomAccessFile class behaves like a combined DataOutputStream and DataInputStream. RandomAccessFile implements both the DataOutput and DataInput interfaces. RandomAccessFile objects are created from a String filename or File object like other stream objects, but a mode constructor argument is also required. The mode is either String "r" (read only) or "rw" (read/write), just like fopen() in C or C++. For example:

// open file junk for reading and writing
RandomAccessFile f = new RandomAccessFile("junk", "rw");
// open crud for reading only
RandomAccessFile ff = new RandomAccessFile("crud", "r");
To append information to a RandomAccessFile, seek to the end of the file:

f.seek(f.length());
The beginning of a file is considered position 0. Therefore, to seek to the beginning of a file, use:

f.seek(0);
The following example writes an integer and a floating point number to a file, rewinds the file, and reads them back:

RandomAccessFile f = new RandomAccessFile("junk", "rw");
f.writeInt(34);
f.writeDouble(3.14159);
f.seek(0);
int i = f.readInt();
double d = f.readDouble();
f.close();
Object Serialization
Similar to primitive value input and output, object values can be written to binary files as well. Writing an object value to an output stream is known as "serializing" that object. The Java core API defines interfaces ObjectInput and ObjectOutput. These interfaces define the readObject() and writeObject() methods, the core of Java object serialization. Two classes, ObjectInputStream and ObjectOutputStream implement these interfaces.

Another interface, Serializable, is used to "mark" a class as one that can be handled via writeObject() and readObject(). Note that Serializable defines no methods; it is simply a "tag" to indicate that a class may be serialized.

Properly Closing Files
The WRONG way to close a file:

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
This ensures that no matter what happens, we at least try to close the file.
