COMPILER=javac

PACKAGER=jar
P_FLAGS=cvf

TARGET=DatabaseManager
VERSION=1.0.0

SOURCES=src/NickSifniotis/DatabaseManager/*
CLASSFILES=bin

all:
	mkdir $(CLASSFILES)
	$(COMPILER) -verbose -d $(CLASSFILES) $(SOURCES)
	$(PACKAGER) $(P_FLAGS) $(TARGET)$(VERSION).jar -C $(CLASSFILES) .
	rm -rf $(CLASSFILES)
