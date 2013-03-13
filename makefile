JFLAGS = -g
JC = javac
.SUFFIXES: .java .class

.java.class:
	$(JC) $(JFLAGS) $*.java

CLASSES = \
		  sketch/controller/Controller.java \
		  sketch/model/Model.java \
		  sketch/view/MouseController.java \
		  sketch/view/Toolbar.java \
		  sketch/view/View.java \
		  sketch/Main.java

default: classes

classes: $(CLASSES:.java=.class)

run: default
	java sketch.Main

clean:
	$(RM) sketch/*.class
	$(RM) sketch/controller/*.class
	$(RM) sketch/model/*.class
	$(RM) sketch/view/*.class
