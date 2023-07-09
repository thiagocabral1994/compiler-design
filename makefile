all : compile

src/main/java/parser/Parser.java: 
	java -jar antlr-4.8-complete.jar -no-listener -no-visitor src/main/java/parser/Parser.g4 

compile : src/main/java/parser/Parser.java
	find src -name "*.java" > javalist
	javac -cp .:ST-4.3.1.jar:antlr-4.8-complete.jar @javalist
	rm -f javalist
	
run: compile
	java -cp ST-4.3.1.jar:antlr-4.8-complete.jar:src/main/java Teste $(path) $(mode)

runAnalyzer:
	java -cp ST-4.3.1.jar:antlr-4.8-complete.jar:src/main/java Teste $(path) s
	javac output/_Program.java
	for file in output/*class; do \
		java -jar classfileanalyzer.jar $$file > $$file.teste.j; \
	done

compileJasmin: run
	java -jar jasmin.jar output/*.j
	java output._Program

runJasmin:
	java -jar jasmin.jar output/*.j
	java output._Program

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
	rm -f output/*.class
	rm -f output/*.java
	rm -f output/*.j
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

cleanOutput:
	rm -f output/*.class
	rm -f output/*.java
	rm -f output/*.j