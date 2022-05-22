
 /*  Esta seção é copiada antes da declaração da classe do analisador léxico.
  *  É nesta seção que se deve incluir imports e declaração de pacotes.
  *  Neste exemplo não temos nada a incluir nesta seção.
  */
  
%%

%unicode
%line
%column
%class LextTest
%function nextToken
%type Token

%{
    
    /* Código arbitrário pode ser inserido diretamente no analisador dessa forma. 
     * Aqui podemos declarar variáveis e métodos adicionais que julgarmos necessários. 
     */
    private int ntk;
    
    public int readedTokens(){
       return ntk;
    }
    private Token symbol(TOKEN_TYPE t) {
        ntk++;
        return new Token(t,yytext(), yyline+1, yycolumn+1);
        
    }
    private Token symbol(TOKEN_TYPE t, Object value) {
        ntk++;
        return new Token(t, value, yyline+1, yycolumn+1);
    }
%}

%init{
    ntk = 0; // Isto é copiado direto no construtor do lexer. 
%init}

  
  /* Agora vamos definir algumas macros */
  EndOfLine  = \r|\n|\r\n
  Whitespace     = {EndOfLine} | [ \t\f]
  Integer      = [:digit:] [:digit:]*
  Float        = [:digit:] [:digit:]* ['.'] [:digit:]* | ['.'] [:digit:] [:digit:]*
  Char         = \'.\' | \'\\n\' | \'\\t\'  | \'\\b\'  | \'\\r\' | \'\\\\\' | \'\\\'\'
  Boolean     = 'true' | 'false'
  Type        = [A-Z][a-zA-Z_0-9]*
  Identifier = [a-z][a-zA-Z_0-9]*
  LineComment = "//" (.)* {EndOfLine}
  
%state COMMENT

%%

<YYINITIAL>{
    "data"          { return symbol(TOKEN_TYPE.DATA_KEYWORD); }
    "Int"           { return symbol(TOKEN_TYPE.INT_KEYWORD); }
    "Char"          { return symbol(TOKEN_TYPE.CHAR_KEYWORD); }
    "Float"         { return symbol(TOKEN_TYPE.FLOAT_KEYWORD); }
    "Bool"          { return symbol(TOKEN_TYPE.BOOL_KEYWORD); }
    "if"            { return symbol(TOKEN_TYPE.IF_KEYWORD); }
    "else"          { return symbol(TOKEN_TYPE.ELSE_KEYWORD); }
    "iterate"       { return symbol(TOKEN_TYPE.ITERATE_KEYWORD); }
    "read"          { return symbol(TOKEN_TYPE.READ_KEYWORD); }
    "print"         { return symbol(TOKEN_TYPE.PRINT_KEYWORD); }
    "return"        { return symbol(TOKEN_TYPE.RETURN_KEYWORD); }
    "new"           { return symbol(TOKEN_TYPE.NEW_KEYWORD); }
    {Type}          { return symbol(TOKEN_TYPE.TYPE); }
    {Float}         { return symbol(TOKEN_TYPE.FLOAT_LITERAL, Float.parseFloat(yytext())); }
    {Char}          { return symbol(TOKEN_TYPE.CHAR_LITERAL, yytext().replace("\'", "")); }
    {Integer}       { return symbol(TOKEN_TYPE.INT_LITERAL, Integer.parseInt(yytext())); }
    {Boolean}       { return symbol(TOKEN_TYPE.BOOL_LITERAL, Boolean.parseBoolean(yytext())); }
    "null"          { return symbol(TOKEN_TYPE.NULL_LITERAL); }
    {Identifier}    { return symbol(TOKEN_TYPE.IDENTIFIER);   }
    "=="            { return symbol(TOKEN_TYPE.EQUALITY); }
    "!="            { return symbol(TOKEN_TYPE.INEQUALITY); }
    ">="            { return symbol(TOKEN_TYPE.GREATER_THAN_OR_EQUAL); }
    "<="            { return symbol(TOKEN_TYPE.LESS_THAN_OR_EQUAL); }
    ">"             { return symbol(TOKEN_TYPE.GREATER_THAN); }
    "<"             { return symbol(TOKEN_TYPE.LESS_THAN); }
    "="             { return symbol(TOKEN_TYPE.ASSIGNMENT); }
    "+"             { return symbol(TOKEN_TYPE.ADDITION); }
    "-"             { return symbol(TOKEN_TYPE.SUBTRACTION); }
    "*"             { return symbol(TOKEN_TYPE.MULTIPLICATION); }
    "/"             { return symbol(TOKEN_TYPE.DIVISION); }
    "%"             { return symbol(TOKEN_TYPE.MODULUS); }
    "&&"            { return symbol(TOKEN_TYPE.AND); }
    "||"            { return symbol(TOKEN_TYPE.OR); }
    "!"             { return symbol(TOKEN_TYPE.NEGATION); }
    "("             { return symbol(TOKEN_TYPE.OPEN_PARENTHESIS); }
    ")"             { return symbol(TOKEN_TYPE.CLOSE_PARENTHESIS); }
    "{"             { return symbol(TOKEN_TYPE.OPEN_BRACE); }
    "}"             { return symbol(TOKEN_TYPE.CLOSE_BRACE); }
    "["             { return symbol(TOKEN_TYPE.OPEN_BRACKET); }
    "]"             { return symbol(TOKEN_TYPE.CLOSE_BRACKET); }
    ","             { return symbol(TOKEN_TYPE.COMMA); }
    ";"             { return symbol(TOKEN_TYPE.SEMICOLON); }
    "::"            { return symbol(TOKEN_TYPE.TYPE_ASSIGMENT); } 
    ":"             { return symbol(TOKEN_TYPE.COLON); }
    "."             { return symbol(TOKEN_TYPE.DOT); }
    "/*"            { yybegin(COMMENT);               }
    {Whitespace}    { /* Não faz nada  */             }
    {LineComment}   {                       }
}

<COMMENT>{
   "*/"     { yybegin(YYINITIAL); } 
   [^"*/"]* {                     }
}



[^]                 { throw new RuntimeException("Illegal character <"+yytext()+">"); }



