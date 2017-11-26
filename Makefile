# ADD JAR FILES TO THE FOLLOWING LINE, SEPARATED BY ':' (NO WHITESPACE)
JARS := gson-2.6.2.jar:junit-4.10.jar

# DO NOT EDIT BELOW HERE
SUBMIT  := $(wildcard src/*.java test/*.java files/* *.jar Makefile README.txt)


JAVAC	:= javac

DEPENDS := $(wildcard src/*.java)

HWNAME := hw09
ts := $(shell /bin/date "+%Y-%m-%d-%H:%M:%S")

ZIPNAME := $(HWNAME)-submit($(ts)).zip

.PHONY: all game run clean zip

all:	

bin:
	mkdir bin

bin/Game.class : $(DEPENDS) bin
	$(JAVAC) -cp .:$(JARS) -d bin $(DEPENDS)

run : bin/Game.class
	java -cp $(JARS):./bin Game

zip:	$(SUBMIT)
	zip '$(ZIPNAME)' $(SUBMIT)

clean:
	rm -f src/*.class bin/* test/*.class
	rm -rf *.zip
