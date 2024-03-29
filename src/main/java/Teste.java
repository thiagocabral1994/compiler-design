/**
 * Daniel Augusto Machado Baeta - 201965122C
 * Thiago do Vale Cabral - 201965220AC
 */

import parser.*;
import util.Pair;
import ast.*;
import visitor.*;

import org.antlr.v4.runtime.*;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class Teste {
     public static final boolean DEBUG = true;
     public static final String OUTPUT_NAME = "Program";

     public static void main(String args[]) throws IOException {
          CharStream stream = CharStreams.fromFileName(args[0]);
          CommonTokenStream tokens;
          try {
               // create a lexer that feeds off of stream
               ParserLexer lex = new ParserLexer(stream);
               // create a buffer of tokens pulled from the lexer
               tokens = new CommonTokenStream(lex);
          } catch (Exception e) {
               System.out.println("Erro na análise léxica");
               throw e;
          }

          Program tree;
          try {
               // create a parser that feeds off the tokens buffer
               ParserParser parser = new ParserParser(tokens);
               tree = parser.prog().ast;
          } catch (Exception e) {
               System.out.println("Erro na análise sintática");
               throw e;
          }

          TypeCheckVisitor typeChecker = new TypeCheckVisitor();
          tree.accept(typeChecker);
          if (typeChecker.getNumErrors() > 0) {
               typeChecker.printErrors();
               throw new IOException("Erro na análise semântica");
          }

          switch (args[1]) {
               case "i":
                    InterpretVisitor interpretVisitor = new InterpretVisitor();
                    tree.accept(interpretVisitor);
                    break;
               case "j":
                    JasminVisitor jasminVisitor = new JasminVisitor(OUTPUT_NAME, typeChecker.getEnv(),
                              typeChecker.getDataMap());
                    tree.accept(jasminVisitor);
                    String programCode = jasminVisitor.getProgramTemplate();
                    writeProgram(programCode, "_" + OUTPUT_NAME + ".j");
                    List<Pair<String, String>> generatedCodes = jasminVisitor.getDataTemplates();
                    for (Pair<String, String> generatedCode : generatedCodes) {
                         writeProgram(generatedCode.getRight(), "_" + OUTPUT_NAME + "&_" + generatedCode.getLeft() + ".j");
                    }
                    break;
               case "s":
                    JavaVisitor javaVisitor = new JavaVisitor(OUTPUT_NAME, typeChecker.getEnv(),
                              typeChecker.getDataMap());
                    tree.accept(javaVisitor);
                    String generatedCode = javaVisitor.getProgramTemplate();
                    writeProgram(generatedCode, "_" + OUTPUT_NAME + ".java");
                    break;
               default:
                    throw new IOException("É necessário escolher uma forma de execução: -i, -s ou -j.");
          }
     }

     private static void writeProgram(String content, String fileName) {
          try {
            File file = new File("output/"+fileName);
            FileWriter fileWriter = new FileWriter(file);
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
            bufferedWriter.write(content);
            bufferedWriter.close();
            fileWriter.close();
            System.out.println("Arquivo criado com sucesso.");
        } catch (IOException e) {
            e.printStackTrace();
        }
     }

}
