#!/usr/bin/env python

# bootstrap by downloading bilder.py if not found
import urllib
import os

if not os.path.exists("bilder.py"):
    print "bootstrapping; downloading bilder.py"
    urllib.urlretrieve(
        "https://raw.githubusercontent.com/parrt/bild/master/src/python/bilder.py",
        "bilder.py")

# assumes bilder.py is in current directory
from bilder import *

def init():
    download("http://search.maven.org/remotecontent?filepath=junit/junit/4.10/junit-4.10.jar", "/tmp")

def compile():
    require(init)
    javac("src", "out", cp="src:/tmp/junit-4.10.jar")
    javac("test", "out", cp="out:/tmp/junit-4.10.jar")

def mkjar():
    require(compile)
    jar("dist/doublekey.jar", srcdir="out", manifest="")

def test():
    require(mkjar)
    junit_runner('cs601.collections.TestMap',
	cp="lib/doublekey-tests.jar:dist/doublekey.jar:/tmp/junit-4.10.jar")

def all():
    require(init)
    test()

processargs(globals())
