/**
 * Daniel Augusto Machado Baeta - 201965122C
 * Thiago do Vale Cabral - 201965220AC
 */

package src.main.java;

public class Token {
    public int l, c;
    public TOKEN_TYPE t;
    public String lexeme;
    public Object info;
    
    public Token(TOKEN_TYPE t, String lex, Object o ,int l, int c){
          this.t = t;
          lexeme = lex;
          info =  o;
          this.l = l;
          this.c = c;
    }
    
    public Token(TOKEN_TYPE t, String lex,int l, int c){
          this.t = t;
          lexeme = lex;
          info =  null;
          this.l = l;
          this.c = c;
    }
    
    public Token(TOKEN_TYPE t,Object o,int l, int c){
          this.t = t;
          lexeme = "";
          info =  o;
          this.l = l;
          this.c = c;
    }
    
    @Override
    public String toString(){
      switch (t) {
            case DATA_KEYWORD:
                  return "DATA";
            case INT_KEYWORD:
                  return "INT";
            case FLOAT_KEYWORD:
                  return "FLOAT";
            case CHAR_KEYWORD:
                  return "CHAR";
            case BOOL_KEYWORD:
                  return "BOOL";
            case IF_KEYWORD:
                  return "IF";
            case ELSE_KEYWORD:
                  return "ELSE";
            case ITERATE_KEYWORD:
                  return "ITERATE";
            case READ_KEYWORD:
                  return "READ";
            case PRINT_KEYWORD:
                  return "PRINT";
            case RETURN_KEYWORD:
                  return "RETURN";
            case NEW_KEYWORD:
                  return "NEW";
            case IDENTIFIER:
                  return "ID:"+lexeme;
            case TYPE:
                  return "TYPE:"+lexeme;
            case FLOAT_LITERAL:
                  return "FLOAT:"+info.toString();
            case CHAR_LITERAL:
                  return "CHAR:"+info.toString();
            case INT_LITERAL:
                  return "INT:"+info.toString();
            case BOOL_LITERAL:
                  return "BOOL:"+info.toString();
            case NULL_LITERAL:
                  return "NULL";
            default:
                  return lexeme;
      }
      //  return "[("+l+","+ c+ ") \"" + lexeme + "\" : <" + (info == null ? "" : info.toString()) + ">] - " + t.toString();
    }
}

 
