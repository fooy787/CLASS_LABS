JAVA=java
JAVAC=javac
SOURCES=$(wildcard *.java)
CLASSES=$(SOURCES:.java=.class)
MAIN_CLASS=TextureExample

default: clean $(CLASSES)

run:
	$(JAVA) -cp .:lwjgl/jar/lwjgl.jar -Djava.library.path=lwjgl/native -XstartOnFirstThread $(MAIN_CLASS)

Vector4: Vector4.class
	$(JAVA) Vector4

%.class: %.java
	$(JAVAC) -cp .:lwjgl/jar/lwjgl.jar $^

.PHONY: clean
clean:
	rm -f $(CLASSES)
