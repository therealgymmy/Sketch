JFLAGS = -g
JC = javac
.SUFFIXES: .java .class

.java.class:
	$(JC) $(JFLAGS) $*.java

CLASSES = \
		  sketch/common/Config.java \
		  sketch/common/Log.java \
		  sketch/controller/Controller.java \
		  sketch/model/IModel.java \
		  sketch/model/LineComponent.java \
		  sketch/model/Model.java \
		  sketch/model/TimeLine.java \
		  sketch/view/IView.java \
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
	$(RM) sketch/common/*.class
	$(RM) sketch/controller/*.class
	$(RM) sketch/model/*.class
	$(RM) sketch/view/*.class
