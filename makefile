all : compile

src/main/java/parser/Parser.java: 
	java -jar antlr-4.8-complete.jar -no-listener -no-visitor src/main/java/parser/Parser.g4 

compile : src/main/java/parser/Parser.java
	find src -name "*.java" > javalist
	javac -cp .:ST-4.3.1.jar:antlr-4.8-complete.jar @javalist
	rm -f javalist
	
run: compile
	java -cp ST-4.3.1.jar:antlr-4.8-complete.jar:src/main/java Teste $(path)

clean: 
	rm -fr src/main/java/parser/.antlr
	rm -f src/main/java/parser/ParserParser.java
	rm -f src/main/java/parser/ParserLexer.java
	rm -f src/main/java/parser/Parser.tokens
	rm -f src/main/java/parser/Parser.interp
	rm -f src/main/java/parser/ParserLexer.interp
	rm -f src/main/java/parser/ParserLexer.tokens
	rm -f ast/*.class
	rm -f src/main/java/parser/*.class
	rm -f *.class
	find . -name "*.class" -delete
	
cleanClass :
	find . -name "*.class" -delete
	
cleanParser: 
	rm -f src/main/java/parser/ParserParser.java
	rm -f src/main/java/parser/ParserLexer.java
	rm -f src/main/java/parser/Parser.tokens
	rm -f src/main/java/parser/Parser.interp
	rm -f src/main/java/parser/ParserLexer.interp
	rm -f src/main/java/parser/ParserLexer.tokens
