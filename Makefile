all: 
	javac -cp src/ -d bin/ src/chess/Chess.java
run: 
	java -cp bin/ chess.Chess
