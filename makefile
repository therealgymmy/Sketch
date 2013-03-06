JFLAGS = -g
JC = javac
.SUFFIXES: .java .class

.java.class:
	$(JC) $(JFLAGS) $*.java

CLASSES = \
		  sketch/Main.java

default: classes

classes: $(CLASSES:.java=.class)

run: default
	java sketch.Main

clean:
	$(RM) sketch/*.class
