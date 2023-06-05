grammar Parser;

@parser::header
{
    package parser;

    import ast.*;

    import java.util.List;
    import java.util.ArrayList;
}

@lexer::header
{
    package parser;
}

/* Regras da gramática */

prog returns [Program ast]:
    {List<Data> datas = new ArrayList<Data>();}
    {List<Function> functions = new ArrayList<Function>();}
    (data1=data {datas.add($data1.ast);})*
    (func1=func {functions.add($func1.ast);})*
    {$ast = new Program(0, 0, datas, functions);}
;

data returns [Data ast]:
    {List<Parameter> declarations = new ArrayList<Parameter>();}
    data1=DATA_KEYWORD
    id=TYPE_KEYWORD
    OPEN_BRACE
    (decl1=decl {declarations.add($decl1.ast);})*
    CLOSE_BRACE
    {$ast = new Data($data1.line, $data1.pos, $id.text, declarations);}
;

decl returns [Parameter ast]:
    id=IDENTIFIER 
    TYPE_ASSIGNMENT 
    type 
    {$ast = new Parameter($id.line, $id.pos, $id.text, $type.ast);} 
    SEMICOLON
;

func returns [Function ast]:
    {List<Parameter> params = new ArrayList<Parameter>();}
    {List<Command> cmds = new ArrayList<Command>();}
    {List<Type> returnTypes = new ArrayList<Type>();}
    id=IDENTIFIER 
    OPEN_PARENTHESIS 
    (params {params = $params.ast;})? 
    CLOSE_PARENTHESIS 
    (
        COLON returnType1=type {returnTypes.add($returnType1.ast);} 
        (COMMA returnType2=type {returnTypes.add($returnType2.ast);})*
    )?
    OPEN_BRACE
    (cmd {cmds.add($cmd.ast);})*
    CLOSE_BRACE
    {$ast = new Function($id.line, $id.pos, $id.text, params, cmds, returnTypes);}
;

params returns [List<Parameter> ast]:
    {$ast = new ArrayList<Parameter>();} 
    id1=IDENTIFIER TYPE_ASSIGNMENT type1=type {$ast.add(new Parameter($id1.line, $id1.pos, $id1.text, $type1.ast));} 
    (COMMA id2=IDENTIFIER TYPE_ASSIGNMENT type2=type {$ast.add(new Parameter($id2.line, $id2.pos, $id2.text, $type2.ast));})* 
;

type returns [Type ast]:
    type1=type OPEN_BRACKET CLOSE_BRACKET {$ast = new ArrayType($type1.ast.getLine(), $type1.ast.getCol(), $type1.ast.getTypeName());}
|
    btype1=btype {$ast = $btype1.ast;}
;

btype returns [Type ast]:
    INT_KEYWORD {$ast = new IntegerBasicType($INT_KEYWORD.line, $INT_KEYWORD.pos);}
|
    CHAR_KEYWORD {$ast = new CharacterBasicType($CHAR_KEYWORD.line, $CHAR_KEYWORD.pos);}
|
    BOOL_KEYWORD {$ast = new BooleanBasicType($BOOL_KEYWORD.line, $BOOL_KEYWORD.pos);}
|
    FLOAT_KEYWORD {$ast = new FloatBasicType($FLOAT_KEYWORD.line, $FLOAT_KEYWORD.pos);}
|
    TYPE_KEYWORD  {$ast = new CustomBasicType($TYPE_KEYWORD.line, $TYPE_KEYWORD.pos, $TYPE_KEYWORD.text);}
;

cmd returns [Command ast]:
    {List<Command> cmds = new ArrayList<Command>();}
    OPEN_BRACE
    (cmd1=cmd {cmds.add($cmd1.ast);})*
    CLOSE_BRACE
    {$ast = new ListCommand($OPEN_BRACE.line, $OPEN_BRACE.pos, cmds);}
|
    IF_KEYWORD
    OPEN_PARENTHESIS
    exp1=exp
    CLOSE_PARENTHESIS
    cmd2=cmd
    {$ast = new IfCommand($IF_KEYWORD.line, $IF_KEYWORD.pos, $exp1.ast, $cmd2.ast);}
|
    IF_KEYWORD
    OPEN_PARENTHESIS
    exp2=exp
    CLOSE_PARENTHESIS
    cmd3=cmd
    ELSE_KEYWORD
    cmd4=cmd
    {$ast = new IfElseCommand($IF_KEYWORD.line, $IF_KEYWORD.pos, $exp2.ast, $cmd3.ast, $cmd4.ast);}
|
    ITERATE_KEYWORD
    OPEN_PARENTHESIS
    exp3=exp
    CLOSE_PARENTHESIS
    cmd5=cmd
    {$ast = new IterateCommand($ITERATE_KEYWORD.line, $ITERATE_KEYWORD.pos, $exp3.ast, $cmd5.ast);}
|
    READ_KEYWORD
    lvalue1=lvalue
    SEMICOLON
    {$ast = new ReadCommand($READ_KEYWORD.line, $READ_KEYWORD.pos, $lvalue1.ast);}
|
    PRINT_KEYWORD
    exp4=exp
    SEMICOLON
    {$ast = new PrintCommand($PRINT_KEYWORD.line, $PRINT_KEYWORD.pos, $exp4.ast);}
|
    {List<Expression> exps = new ArrayList();}
    RETURN_KEYWORD
    exp5=exp {exps.add($exp5.ast);}
    (COMMA exp6=exp {exps.add($exp6.ast);})*
    SEMICOLON
    {$ast = new ReturnCommand($RETURN_KEYWORD.line, $RETURN_KEYWORD.pos, exps);}
|
    lvalue2=lvalue
    ASSIGNMENT
    exp7=exp
    SEMICOLON
    {$ast = new AssignmentCommand($lvalue2.ast.getLine(), $lvalue2.ast.getCol(), $lvalue2.ast, $exp7.ast);}
|
    {List<Expression> exps = new ArrayList<Expression>();}
    {List<LValue> lvalues = new ArrayList<LValue>();}
    IDENTIFIER
    OPEN_PARENTHESIS
    (exps {exps = $exps.ast;})?
    CLOSE_PARENTHESIS
    (
        LESS_THAN
        lvalue3=lvalue {lvalues.add($lvalue3.ast);}
        (COMMA lvalue4=lvalue {lvalues.add($lvalue4.ast);})*
        GREATER_THAN
    )?
    SEMICOLON
    {$ast = new CallCommand($IDENTIFIER.line, $IDENTIFIER.pos, $IDENTIFIER.text, exps, lvalues);}
;

exp returns [Expression ast]:
    exp1=exp
    AND
    exp2=exp
    {$ast = new AndExpression($exp1.ast.getLine(), $exp1.ast.getCol(), $exp1.ast, $exp2.ast);}
|
    rexp
    {$ast = $rexp.ast;}
;

rexp returns [RExpression ast]:
    aexp1=aexp
    LESS_THAN
    aexp2=aexp
    {$ast = new LessRExpression($aexp1.ast.getLine(), $aexp1.ast.getCol(), $aexp1.ast, $aexp2.ast);}
|
    rexp1=rexp
    EQUALITY
    aexp3=aexp
    {$ast = new EqualityRExpression($rexp1.ast.getLine(), $rexp1.ast.getCol(), $rexp1.ast, $aexp3.ast);}
|
    rexp2=rexp
    INEQUALITY
    aexp4=aexp
    {$ast = new EqualityRExpression($rexp2.ast.getLine(), $rexp2.ast.getCol(), $rexp2.ast, $aexp4.ast);}
|
    aexp5=aexp
    {$ast = $aexp5.ast;}
;

aexp returns [AExpression ast]:
    aexp1=aexp
    ADDITION
    mexp1=mexp
    {$ast = new AdditionAExpression($aexp1.ast.getLine(), $aexp1.ast.getCol(), $aexp1.ast, $mexp1.ast);}
|
    aexp2=aexp
    SUBTRACTION
    mexp2=mexp
    {$ast = new SubtractionAExpression($aexp2.ast.getLine(), $aexp2.ast.getCol(), $aexp2.ast, $mexp2.ast);}
|
    mexp3=mexp
    {$ast = $mexp3.ast;}
;

mexp returns [MExpression ast]:
    mexp1=mexp
    MULTIPLICATION
    sexp1=sexp
    {$ast = new MultiplicationMExpression($mexp1.ast.getLine(), $mexp1.ast.getCol(), $mexp1.ast, $sexp1.ast);}
|
    mexp2=mexp
    DIVISION
    sexp2=sexp
    {$ast = new DivisionMExpression($mexp2.ast.getLine(), $mexp2.ast.getCol(), $mexp2.ast, $sexp2.ast);}
|
    mexp3=mexp
    MODULUS
    sexp3=sexp
    {$ast = new ModulusMExpression($mexp3.ast.getLine(), $mexp3.ast.getCol(), $mexp3.ast, $sexp3.ast);}
|
    sexp4=sexp {$ast = $sexp4.ast;}
;

sexp returns [SExpression ast]:
    NEGATION
    sexp1=sexp
    {$ast = new NegationSExpression($NEGATION.line, $NEGATION.pos, $sexp1.ast);}
|
    SUBTRACTION
    sexp2=sexp
    {$ast = new NegativeSExpression($SUBTRACTION.line, $SUBTRACTION.pos, $sexp2.ast);}
|
    TRUE_LITERAL
    {$ast = new TrueSExpression($TRUE_LITERAL.line, $TRUE_LITERAL.pos);}
|
    FALSE_LITERAL
    {$ast = new FalseSExpression($FALSE_LITERAL.line, $FALSE_LITERAL.pos);}
|
    NULL_LITERAL
    {$ast = new NullSExpression($NULL_LITERAL.line, $NULL_LITERAL.pos);}
|
    INTEGER_LITERAL
    {$ast = new IntegerSExpression($INTEGER_LITERAL.line, $INTEGER_LITERAL.pos, $INTEGER_LITERAL.text);}
|
    FLOAT_LITERAL
    {$ast = new FloatSExpression($FLOAT_LITERAL.line, $FLOAT_LITERAL.pos, $FLOAT_LITERAL.text);}
|
    CHAR_LITERAL
    {$ast = new CharSExpression($CHAR_LITERAL.line, $CHAR_LITERAL.pos, $CHAR_LITERAL.text);}
|
    pexp1=pexp {$ast = $pexp1.ast;}
;

pexp returns [PExpression ast]:
    lvalue1=lvalue
    {$ast = $lvalue1.ast;}
|
    OPEN_PARENTHESIS
    exp1=exp
    CLOSE_PARENTHESIS
    {$ast = new ParenthesisPExpression($OPEN_PARENTHESIS.line, $OPEN_PARENTHESIS.pos, $exp1.ast);}
|
    {Expression exp = null;}
    NEW_KEYWORD
    type1=type
    (
        OPEN_BRACKET
        exp2=exp
        CLOSE_BRACKET
        {exp = $exp2.ast;}
    )?
    {$ast = new NewPExpression($NEW_KEYWORD.line, $NEW_KEYWORD.pos, $type1.ast, exp);}
|
    {List<Expression> paramExps = new ArrayList<Expression>();}
    IDENTIFIER
    OPEN_PARENTHESIS
    (exps1=exps {paramExps = $exps1.ast;})?
    CLOSE_PARENTHESIS
    OPEN_BRACKET
    exp3=exp
    CLOSE_BRACKET
    {$ast = new CallPExpression($IDENTIFIER.line, $IDENTIFIER.pos, $IDENTIFIER.text, paramExps, $exp3.ast);}
;

lvalue returns [LValue ast]:
    id1=IDENTIFIER
    {$ast = new IdentifierLValue($id1.line, $id1.pos, $id1.text);}
|
    lvalue1=lvalue
    OPEN_BRACKET
    exp1=exp
    CLOSE_BRACKET
    {$ast = new ArrayLValue($lvalue1.ast.getLine(), $lvalue1.ast.getCol(), $lvalue1.ast, $exp1.ast);}
|
    lvalue2=lvalue
    DOT
    id2=IDENTIFIER
    {$ast = new ObjectLValue($lvalue2.ast.getLine(), $lvalue2.ast.getCol(), $lvalue2.ast, $id2.text);}
;

exps returns [List<Expression> ast]:
    {$ast = new ArrayList<Expression>();}
    exp1=exp
    {$ast.add($exp1.ast);}
    (
        COMMA exp2=exp
        {$ast.add($exp2.ast);}
    )*
;

/* Regras léxicas */
TRUE_LITERAL: 'true';
FALSE_LITERAL: 'false';
NULL_LITERAL: 'null';
DATA_KEYWORD: 'data';
INT_KEYWORD: 'Int';
CHAR_KEYWORD: 'Char';
FLOAT_KEYWORD: 'Float';
BOOL_KEYWORD: 'Bool';
IF_KEYWORD: 'if';
ELSE_KEYWORD: 'else';
ITERATE_KEYWORD: 'iterate';
READ_KEYWORD: 'read';
PRINT_KEYWORD: 'print';
RETURN_KEYWORD: 'return';
NEW_KEYWORD: 'new';
EQUALITY: '==';
INEQUALITY: '!=';
GREATER_THAN_OR_EQUAL: '>=';
LESS_THAN_OR_EQUAL: '<=';
GREATER_THAN: '>';
LESS_THAN: '<';
ASSIGNMENT: '=';
ADDITION: '+';
SUBTRACTION: '-';
MULTIPLICATION: '*';
DIVISION: '/';
MODULUS: '%';
AND: '&&';
OR: '||';
NEGATION: '!';
OPEN_PARENTHESIS: '(';
CLOSE_PARENTHESIS: ')';
OPEN_BRACE: '{';
CLOSE_BRACE: '}';
OPEN_BRACKET: '[';
CLOSE_BRACKET: ']';
COMMA: ',';
SEMICOLON: ';';
TYPE_ASSIGNMENT: '::';
COLON: ':';
DOT: '.';
INTEGER_LITERAL: [0-9] [0-9]*;
FLOAT_LITERAL: [0-9] [0-9]* '.' [0-9]* | '.' [0-9] [0-9]*;
CHAR_LITERAL : '\'' CHAR '\'';
IDENTIFIER: [a-z][a-zA-Z_0-9]*;

TYPE_KEYWORD: [A-Z][a-zA-Z_0-9]*;

NEWLINE: '\r'? '\n' -> skip;
WS : [ \t]+ -> skip;
LINE_COMMENT : '--' ~('\r' | '\n')* NEWLINE -> skip;
COMMENT: '{-' .*?  '-}' -> skip;

CHAR : ~[\r\n\t\\] | '\\' [btnr'"\\];