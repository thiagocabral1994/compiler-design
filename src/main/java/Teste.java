/**
 * Daniel Augusto Machado Baeta - 201965122C
 * Thiago do Vale Cabral - 201965220AC
 */

import parser.*;
import ast.*;
import visitor.*;

import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.*;

import java.io.IOException;

public class Teste{
     public static void main(String args[]) throws IOException{
          CharStream stream = CharStreams.fromFileName(args[0]);
          // create a lexer that feeds off of stream
          ParserLexer lex = new ParserLexer(stream);
          // create a buffer of tokens pulled from the lexer
          CommonTokenStream tokens = new CommonTokenStream(lex);
          // create a parser that feeds off the tokens buffer
          ParserParser parser = new ParserParser(tokens);

          Program tree = parser.prog().ast;
          tree.accept(new ParserVisitor());
     }
}
