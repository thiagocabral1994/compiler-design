package visitor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.List;
import java.util.Stack;

import ast.*;
import util.semantic.*;

public class TypeCheckVisitor extends Visitor {
  private static final String MAIN = "main";

  private STypeInt typeInt = STypeInt.create();
  private STypeFloat typeFloat = STypeFloat.create();
  private STypeChar typeChar = STypeChar.create();
  private STypeBool typeBool = STypeBool.create();
  private STypeError typeError = STypeError.create();
  private STypeNull typeNull = STypeNull.create();

  private List<String> logError;

  private Map<String, Map<String, SemanticType>> dataMap;
  private TypeEnv<LocalEnv<SemanticType>> env;
  private LocalEnv<SemanticType> activeScope;

  private Stack<SemanticType> stack;
  private boolean returnCheck;

  public TypeCheckVisitor() {
    this.stack = new Stack<>();
    this.env = new TypeEnv<>();
    this.logError = new ArrayList<>();
    this.dataMap = new HashMap<>();
  }

  public int getNumErrors() {
    return this.logError.size();
  }

  public void printErrors() {
    for (String string : this.logError) {
      System.out.println(string);
    }
  }

  private void typeArithmeticBinOp(Node n, String opName) {
    SemanticType left, right;
    right = this.stack.pop();
    left = this.stack.pop();

    if( (right.match(typeInt) ) || right.match(typeChar)){
        if(left.match(typeInt) || left.match(typeFloat) || left.match(typeChar)){
          this.stack.push(left);
        }else{
          logError.add( n.getLine() + ", " + n.getCol() + ": Operador" + opName +"não se aplica aos tipos " + left.toString() + " e " + right.toString() );
          this.stack.push(typeError);
        }
       
    }else if(right.match(typeFloat)){
      if(left.match(typeInt) || left.match(typeFloat) || left.match(typeChar)){
          this.stack.push(right);
      }else{
          logError.add( n.getLine() + ", " + n.getCol() + ": Operador " + opName +" não se aplica aos tipos " + left.toString() + " e " + right.toString() );
          this.stack.push(typeError);
      }
    } else if (right.match(typeChar)) {
      if (left.match(typeInt) || left.match(typeFloat) || left.match(typeChar)) {
        this.stack.push(left);
      } else {
        logError.add(n.getLine() + ", " + n.getCol() + ": Operador " + opName + " não se aplica aos tipos "
            + left.toString() + " e " + right.toString());
        this.stack.push(typeError);
      }
    } else {
      logError.add(n.getLine() + ", " + n.getCol() + ": Operador " + opName + " não se aplica aos tipos "
          + left.toString() + " e " + right.toString());
      this.stack.push(typeError);
    }
  }

  @Override
  public void visit(AdditionAExpression exp) {
    exp.getLeft().accept(this);
    exp.getRight().accept(this);
    typeArithmeticBinOp(exp, "+");
  }

  @Override
  public void visit(AndExpression exp) {
    exp.getLeft().accept(this);
    exp.getRight().accept(this);
    SemanticType left, right;
    right = this.stack.pop();
    left = this.stack.pop();

    if (right.match(typeBool) && left.match(typeBool)) {
      this.stack.push(typeBool);
    }
    else {
      logError.add( exp.getLine() + ", " + exp.getCol() + ": Operador && não se aplica aos tipos " + left.toString() + " e " + right.toString() );
      this.stack.push(typeError);
    }
  }

  @Override
  public void visit(ArrayLValue lvalue) {
    lvalue.getExpression().accept(this);
    SemanticType indexType = this.stack.pop();

    if (!indexType.match(this.typeInt)) {
      this.stack.push(this.typeError);
      this.logError.add(lvalue.getLine() + ", " + lvalue.getCol() + ": Índice deve ser tipo Int. "
          + indexType.toString() + " encontrado!");
      return;
    }

    lvalue.getLValue().accept(this);
    SemanticType lValueType = this.stack.pop();

    if (!(lValueType instanceof STypeArray)) {
      this.stack.push(this.typeError);
      this.logError
          .add(lvalue.getLine() + ", " + lvalue.getCol() + ": Tipo " + lValueType.toString() + " não é um Array.");
      return;
    }

    this.stack.push(((STypeArray) lValueType).getType());
  }

  @Override
  public void visit(ArrayType arrayType) {
    arrayType.getType().accept(this);
    this.stack.push(STypeArray.create(this.stack.pop()));
  }

  @Override
  public void visit(AssignmentCommand cmd) {
    cmd.getExpression().accept(this);
    SemanticType expressionType = this.stack.pop();

    LValueContext lvalueContext = cmd.getLValueContext();
    lvalueContext.accept(this);
    SemanticType lValueType = this.stack.pop();

    // Caso LValue seja nulo, podemos concluir que análise semântica que o dado
    // Não foi inicializado.
    if (lValueType.match(this.typeNull)) {
      String headId = lvalueContext.getLValue().getHeadId();
      this.activeScope.set(headId, expressionType);
      this.stack.push(expressionType);
      return;
    }

    // Caso LValue seja definido, a atribuição gera erro se os tipos forem
    // diferentes
    if (!lValueType.match(expressionType)) {
      this.logError.add(cmd.getLine() + ", " + cmd.getCol() + ": Não é possível atribuir " + expressionType.toString()
          + " a " + lValueType.toString());
      this.stack.push(this.typeError);
      return;
    }

    // Atribuição bem sucedida. Passamos adiante o tipo de lvalue.
    this.stack.push(lValueType);
  }

  @Override
  public void visit(BooleanBasicType node) {
    this.stack.push(this.typeBool);
  }

  @Override
  public void visit(CallCommand node) {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'visit'");
  }

  @Override
  public void visit(CallPExpression node) {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'visit'");
  }

  @Override
  public void visit(CharSExpression exp) {
    this.stack.push(this.typeChar);
  }

  @Override
  public void visit(CharacterBasicType node) {
    this.stack.push(this.typeChar);
  }

  @Override
  public void visit(CustomBasicType type) {
    String name = type.getTypeName();

    if (!this.dataMap.containsKey(name)) {
      this.logError.add(type.getLine() + ", " + type.getCol() + ": Tipo " + type.getTypeName() + " não existe!");
      return;
    }
    this.stack.push(STypeCustom.create(name));
  }

  @Override
  public void visit(Data data) {
    Map<String, SemanticType> declarationsMap = new HashMap<>();

    this.dataMap.put(data.getId(), declarationsMap);

    for (Parameter declaration : data.getDeclarations()) {
      declaration.accept(this);
      declarationsMap.put(declaration.getId(), this.stack.pop());
    }
  }

  @Override
  public void visit(DivisionMExpression exp) {
    exp.getLeft().accept(this);
    exp.getRight().accept(this);
    typeArithmeticBinOp(exp, "/");
  }

  @Override
  public void visit(EqualityRExpression exp) {
    exp.getLeft().accept(this);
    exp.getRight().accept(this);
    SemanticType left, right;
    right = this.stack.pop();
    left = this.stack.pop();

    if((right.match(typeInt) || right.match(typeFloat) || right.match(typeChar)) && (left.match(typeInt) || left.match(typeFloat) || left.match(typeChar))) {
      this.stack.push(typeBool);
    } else if (right.match(typeBool) && left.match(typeBool)) {
      this.stack.push(typeBool);
    }
    else {
      logError.add( exp.getLine() + ", " + exp.getCol() + ": Operador == não se aplica aos tipos " + left.toString() + " e " + right.toString() );
      this.stack.push(typeError);
    }
  }

  @Override
  public void visit(FalseSExpression node) {
    this.stack.push(this.typeBool);
  }

  @Override
  public void visit(FloatBasicType node) {
    this.stack.push(this.typeFloat);
  }

  @Override
  public void visit(FloatSExpression node) {
    this.stack.push(this.typeFloat);
  }

  @Override
  public void visit(Function function) {
    this.returnCheck = false;
    this.activeScope = env.get(function.getId());
    for (Parameter parameter : function.getParameters()) {
      parameter.getType().accept(this);
      this.activeScope.set(parameter.getId(), this.stack.pop());
    }

    for (Command cmd : function.getCommands()) {
      cmd.accept(this);
    }

    if (function.getReturnTypes().size() > 0 && !this.returnCheck) {
      this.logError.add(function.getLine() + ", " + function.getCol() + ": Função " + function.getId()
          + " deve retornar algum valor.");
    }
  }

  @Override
  public void visit(IdentifierLValue lvalue) {
    String id = lvalue.getID();
    SemanticType lvalueType = this.activeScope.get(id);

    if (lvalueType == null || lvalueType.match(this.typeNull)) {
      this.activeScope.set(id, this.typeNull);
      this.stack.push(this.typeNull);
    } else {
      this.activeScope.set(id, lvalueType);
      this.stack.push(lvalueType);
    }
  }

  @Override
  public void visit(IfCommand node) {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'visit'");
  }

  @Override
  public void visit(IfElseCommand node) {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'visit'");
  }

  @Override
  public void visit(InequalityRExpression exp) {
    exp.getLeft().accept(this);
    exp.getRight().accept(this);
    SemanticType left, right;
    right = this.stack.pop();
    left = this.stack.pop();

    if((right.match(typeInt) || right.match(typeFloat) || right.match(typeChar)) && (left.match(typeInt) || left.match(typeFloat) || left.match(typeChar))) {
      this.stack.push(typeBool);
    } else if (right.match(typeBool) && left.match(typeBool)) {
      this.stack.push(typeBool);
    }
    else {
      logError.add( exp.getLine() + ", " + exp.getCol() + ": Operador != não se aplica aos tipos " + left.toString() + " e " + right.toString() );
      this.stack.push(typeError);
    }
  }

  @Override
  public void visit(IntegerBasicType node) {
    this.stack.push(this.typeInt);
  }

  @Override
  public void visit(IntegerSExpression node) {
    this.stack.push(this.typeInt);
  }

  @Override
  public void visit(IterateCommand node) {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'visit'");
  }

  @Override
  public void visit(LessRExpression exp) {
    exp.getLeft().accept(this);
    exp.getRight().accept(this);
    SemanticType left, right;
    right = this.stack.pop();
    left = this.stack.pop();

    if((right.match(typeInt) || right.match(typeFloat) || right.match(typeChar)) && (left.match(typeInt) || left.match(typeFloat) || left.match(typeChar))) {
      this.stack.push(typeBool);
    }
    else {
      logError.add( exp.getLine() + ", " + exp.getCol() + ": Operador < não se aplica aos tipos " + left.toString() + " e " + right.toString() );
      this.stack.push(typeError);
    }
  }

  @Override
  public void visit(ListCommand node) {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'visit'");
  }

  @Override
  public void visit(ModulusMExpression exp) {
    exp.getLeft().accept(this);
    exp.getRight().accept(this);
    typeArithmeticBinOp(exp, "%");
  }

  @Override
  public void visit(MultiplicationMExpression exp) {
    exp.getLeft().accept(this);
    exp.getRight().accept(this);
    typeArithmeticBinOp(exp, "*");
  }

  @Override
  public void visit(NegationSExpression exp) {
    exp.getSExpression().accept(this);
    SemanticType type = this.stack.pop();

    if(type.match(typeBool)) {
      this.stack.push(typeBool);
    }
    else {
      logError.add( exp.getLine() + ", " + exp.getCol() + ": Operador ! não se aplica ao tipo " + type.toString());
      this.stack.push(typeError);
    }
  }

  @Override
  public void visit(NegativeSExpression exp) {
    exp.getSExpression().accept(this);
    SemanticType type = this.stack.pop();
    if(type.match(typeInt) || type.match(typeChar)) {
      this.stack.push(typeInt);
    }
    else if(type.match(typeFloat)) {
      this.stack.push(typeFloat);
    }
    else {
      logError.add( exp.getLine() + ", " + exp.getCol() + ": Operador ! não se aplica ao tipo " + type.toString());
      this.stack.push(typeError);
    }
  }

  @Override
  public void visit(NewPExpression exp) {
    exp.getType().accept(this);
    SemanticType baseType = this.stack.pop();

    if (baseType instanceof STypeError) {
      this.stack.push(this.typeError);
      return;
    }

    Expression arrayExpression = exp.getExpression();

    if (arrayExpression == null) {
      this.stack.push(baseType);
      return;
    }

    arrayExpression.accept(this);
    SemanticType indexType = this.stack.pop();

    if (!indexType.match(this.typeInt)) {
      this.stack.push(this.typeError);
      this.logError.add(exp.getLine() + ", " + exp.getCol() + ": Índice deve ser tipo Int. "
          + indexType.toString() + " encontrado!");
      return;
    }

    this.stack.push(STypeArray.create(baseType));
  }

  @Override
  public void visit(NullSExpression node) {
    this.stack.push(this.typeNull);
  }

  @Override
  public void visit(ObjectLValue lvalue) {
    lvalue.getLValue().accept(this);
    SemanticType lvalueType = this.stack.pop();

    STypeCustom customSType = null;
    if (lvalueType instanceof STypeCustom) {
      customSType = (STypeCustom) lvalueType;
    }

    if (customSType == null) {
      this.logError.add(lvalue.getLine() + ", " + lvalue.getCol() + " Não é possível acessar " + lvalue.getParamID()
          + " de " + lvalueType.toString());
      this.stack.push(this.typeError);
      return;
    }

    Map<String, SemanticType> data = this.dataMap.get(customSType.toString());

    if (data == null) {
      this.logError
          .add(lvalue.getLine() + ", " + lvalue.getCol() + ": Tipo " + customSType.toString() + " não existe!");
      this.stack.push(this.typeError);
      return;
    }

    String paramId = lvalue.getParamID();
    SemanticType objectSType = data.get(paramId);

    if (objectSType == null) {
      this.logError.add(lvalue.getLine() + ", " + lvalue.getCol() + ": Tipo " + customSType.toString()
          + " não possui parâmetro " + paramId);
      this.stack.push(this.typeError);
      return;
    }

    this.stack.push(objectSType);
  }

  @Override
  public void visit(Parameter param) {
    param.getType().accept(this);
  }

  @Override
  public void visit(ParenthesisPExpression node) {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'visit'");
  }

  @Override
  public void visit(PrintCommand cmd) {
    cmd.getExpression().accept(this);
    this.stack.pop();
  }

  @Override
  public void visit(Program program) {
    for (Data data : program.getDatas()) {
      data.accept(this);
    }

    Function main = null;
    for (Function function : program.getFunctions()) {
      List<SemanticType> typeArgs = new ArrayList<>();
      for (Parameter parameter : function.getParameters()) {
        parameter.accept(this);
        typeArgs.add(this.stack.pop());
      }

      List<SemanticType> typeReturns = new ArrayList<>();
      for (Type returnType : function.getReturnTypes()) {
        returnType.accept(this);
        typeReturns.add(this.stack.pop());
      }

      STypeFunction typeFunction = STypeFunction.create(typeArgs, typeReturns);
      String id = function.getId();
      env.set(id, new LocalEnv<>(id, typeFunction));

      if (function.getId().equals(MAIN)) {
        main = function;

        // Criamos uma typeMain sem argumentos ou tipos de retorno (void).
        STypeFunction typeMain = STypeFunction.create();

        if (!typeMain.match(typeFunction)) {
          this.logError.add(function.getLine() + ", " + function.getCol()
              + " Função main não deve ter argumentos ou tipo de retorno.");
        }
      }
    }

    if (main == null) {
      this.logError.add(program.getLine() + ", " + program.getCol() + " Programa deve ter uma função main.");
    }

    for (Function function : program.getFunctions()) {
      function.accept(this);
    }
  }

  @Override
  public void visit(ReadCommand node) {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'visit'");
  }

  @Override
  public void visit(ReturnCommand node) {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'visit'");
  }

  @Override
  public void visit(SubtractionAExpression exp) {
    exp.getLeft().accept(this);
    exp.getRight().accept(this);
    typeArithmeticBinOp(exp, "-");
  }

  @Override
  public void visit(TrueSExpression node) {
    this.stack.push(this.typeBool);
  }

  @Override
  public void visit(LValueContext context) {
    LValue lvalue = context.getLValue();
    lvalue.accept(this);
  }

}
