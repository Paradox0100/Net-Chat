build:
	javac -cp src/json-simple-1.1.jar src/*.java
run:
	java -cp .:src/json-simple-1.1.jar src/NetChat
clean:
	rm -f src/*.class
