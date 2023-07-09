package visitor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.List;
import java.util.Stack;

import ast.*;
import util.Pair;
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
  private Map<STypeFunctionKey, LocalEnv<Pair<SemanticType, Integer>>> env;
  private LocalEnv<Pair<SemanticType, Integer>> activeScope;
  private int lastIndex;

  private Stack<SemanticType> stack;
  private boolean returnCheck;
  private boolean printCheck;

  private int intCount;

  public TypeCheckVisitor() {
    this.stack = new Stack<>();
    this.env = new HashMap<>();
    this.logError = new ArrayList<>();
    this.dataMap = new HashMap<>();
  }

  public int getNumErrors() {
    return this.logError.size();
  }

  public Map<STypeFunctionKey, LocalEnv<Pair<SemanticType, Integer>>> getEnv() {
    return this.env;
  }

  public Map<String, Map<String, SemanticType>> getDataMap() {
    return this.dataMap;
  }

  public void printErrors() {
    for (String string : this.logError) {
      System.out.println(string);
    }
  }

  private void typeArithmeticBinOp(Expression n, String opName) {
    this.testMaxSize();
    SemanticType left, right;
    right = this.stack.pop();
    left = this.stack.pop();

    if ((right.match(typeInt)) || right.match(typeChar)) {
      if (left.match(typeInt) || left.match(typeFloat) || left.match(typeChar)) {
        this.stack.push(left);
        n.setSemanticType(left);
      } else {
        logError.add(n.getLine() + ", " + n.getCol() + ": Operador" + opName + "não se aplica aos tipos "
            + left.toString() + " e " + right.toString());
        this.stack.push(typeError);
      }
    } else if (right.match(typeFloat)) {
      if (left.match(typeInt) || left.match(typeFloat) || left.match(typeChar)) {
        this.stack.push(right);
        n.setSemanticType(right);
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
    this.testMaxSize();
    SemanticType left, right;
    right = this.stack.pop();
    left = this.stack.pop();

    if (right.match(typeBool) && left.match(typeBool)) {
      this.stack.push(typeBool);
      exp.setSemanticType(typeBool);
    } else {
      logError.add(exp.getLine() + ", " + exp.getCol() + ": Operador && não se aplica aos tipos " + left.toString()
          + " e " + right.toString());
      this.stack.push(typeError);
    }
  }

  @Override
  public void visit(ArrayLValue lvalue) {
    lvalue.getExpression().accept(this);
    this.testMaxSize();
    SemanticType indexType = this.stack.pop();

    if (!indexType.match(this.typeInt)) {
      this.stack.push(this.typeError);
      this.logError.add(lvalue.getLine() + ", " + lvalue.getCol() + ": Índice deve ser tipo Int. "
          + indexType.toString() + " encontrado!");
      return;
    }

    lvalue.getLValue().accept(this);
    this.testMaxSize();
    SemanticType lValueType = this.stack.pop();

    if (!(lValueType instanceof STypeArray)) {
      this.stack.push(this.typeError);
      this.logError
          .add(lvalue.getLine() + ", " + lvalue.getCol() + ": Tipo " + lValueType.toString() + " não é um Array.");
      return;
    }

    SemanticType result = ((STypeArray) lValueType).getType();
    this.stack.push(result);
    lvalue.setSemanticType(result);
  }

  @Override
  public void visit(ArrayType arrayType) {
    arrayType.getType().accept(this);
    this.testMaxSize();
    this.stack.push(STypeArray.create(this.stack.pop()));
  }

  @Override
  public void visit(AssignmentCommand cmd) {
    Expression exp = cmd.getExpression();
    cmd.getExpression().accept(this);
    this.testMaxSize();
    SemanticType expressionType = this.stack.pop();

    // Caso expressionType seja nulo, podemos concluir que análise semântica que o
    // dado
    // Não foi inicializado.
    if (exp instanceof LValueContext && expressionType.match(this.typeNull)) {
      this.logError
          .add(cmd.getLine() + ", " + cmd.getCol() + ": Não é possível acessar valor de " + expressionType.toString());
      this.stack.push(this.typeError);
      return;
    }

    LValueContext lvalueContext = cmd.getLValueContext();
    lvalueContext.accept(this);
    this.testMaxSize();
    SemanticType lValueType = this.stack.pop();

    // Caso LValue seja nulo, podemos concluir que análise semântica que o dado
    // Não foi inicializado.
    if (lValueType.match(this.typeNull)) {
      String headId = lvalueContext.getLValue().getHeadId();
      Pair<SemanticType, Integer> pair = this.activeScope.get(headId);
      this.activeScope.set(headId, new Pair<>(expressionType, pair.getRight()));
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
    this.testMaxSize();
  }

  @Override
  public void visit(CallCommand cmd) {
    String id = cmd.getId();

    List<Expression> expressions = cmd.getExpressions();
    List<SemanticType> argTypes = new ArrayList<>();
    for (int i = 0; i < expressions.size(); i++) {
      expressions.get(i).accept(this);
      this.testMaxSize();
      SemanticType expressionType = this.stack.pop();
      argTypes.add(expressionType);
    }

    STypeFunctionKey functionKey = STypeFunctionKey.create(cmd.getId(), argTypes);
    LocalEnv<Pair<SemanticType, Integer>> localEnv = this.env.get(functionKey);

    if (localEnv == null) {
      this.stack.push(this.typeError);
      this.logError
          .add(cmd.getLine() + ", " + cmd.getCol() + ": Função " + id + "com argumentos fornecidos não existe!");
      return;
    }

    STypeFunction scopeFunctionType = localEnv.getFunctionType();

    int expectedAmountOfReturns = scopeFunctionType.getReturnTypes().size();
    int actualAmountOfReturns = cmd.getReturnLValueContexts().size();
    if (actualAmountOfReturns > expectedAmountOfReturns) {
      this.stack.push(this.typeError);
      this.logError.add(cmd.getLine() + ", " + cmd.getCol() + ": Função espera " + expectedAmountOfReturns
          + " retornos, mas recebeu " + actualAmountOfReturns + " retornos!");
      return;
    }

    List<SemanticType> returnTypes = scopeFunctionType.getReturnTypes();
    List<LValueContext> lvalueCtx = cmd.getReturnLValueContexts();
    for (int i = 0; i < lvalueCtx.size(); i++) {
      lvalueCtx.get(i).accept(this);
      this.testMaxSize();
      SemanticType lValueType = this.stack.pop();

      if (lValueType.match(this.typeNull)) {
        String headId = lvalueCtx.get(i).getLValue().getHeadId();
        Pair<SemanticType, Integer> pair = this.activeScope.get(headId);
        this.activeScope.set(headId, new Pair<>(returnTypes.get(i), pair.getRight()));
        this.stack.push(returnTypes.get(i));
      } else if (!returnTypes.get(i).match(lValueType)) {
        this.logError
            .add(cmd.getLine() + ", " + cmd.getCol() + ": Não é possível atribuir " + returnTypes.get(i).toString()
                + " a " + lValueType.toString());
      }
    }
  }

  @Override
  public void visit(CallPExpression exp) {
    String id = exp.getID();

    List<SemanticType> argTypes = new ArrayList<>();
    for (int i = 0; i < exp.getParamExpressions().size(); i++) {
      exp.getParamExpressions().get(i).accept(this);
      this.testMaxSize();
      SemanticType expressionType = this.stack.pop();
      argTypes.add(expressionType);
    }

    STypeFunctionKey functionKey = STypeFunctionKey.create(id, argTypes);
    LocalEnv<Pair<SemanticType, Integer>> localEnv = env.get(functionKey);

    if (localEnv == null) {
      logError
          .add(exp.getLine() + ", " + exp.getCol() + ": Chamada a função não declarada com argumentos: " + exp.getID());
      this.stack.push(typeError);
      return;
    }

    exp.getBracketExpression().accept(this);
    this.testMaxSize();

    STypeFunction typeFunction = localEnv.getFunctionType();
    if (this.stack.pop().match(typeInt)) {
      int index = this.lastIndex;
      exp.setIndex(index);
      if (index >= 0 && typeFunction.getReturnTypes().size() > index) {
        SemanticType returnIndexType = typeFunction.getReturnTypes().get(index);
        this.stack.push(returnIndexType);
        exp.setSemanticType(returnIndexType);
      } else {
        logError.add(exp.getLine() + ", " + exp.getCol() + ": Posição " + index + " de retorno da função " + exp.getID()
            + " não encontrada. ");
        this.stack.push(typeError);
        return;
      }
    } else {
      logError.add(exp.getLine() + ", " + exp.getCol() + ": Tipo de acesso ao retorno da função deve ser inteiro. ");
      this.stack.push(typeError);
    }

  }

  @Override
  public void visit(CharSExpression exp) {
    this.stack.push(this.typeChar);
    this.testMaxSize();
    exp.setSemanticType(this.typeChar);
  }

  @Override
  public void visit(CharacterBasicType node) {
    this.stack.push(this.typeChar);
    this.testMaxSize();
  }

  @Override
  public void visit(CustomBasicType type) {
    String name = type.getTypeName();
    if (!this.dataMap.containsKey(name)) {
      this.logError.add(type.getLine() + ", " + type.getCol() + ": Tipo " + type.getTypeName() + " não existe!");
      return;
    }
    this.stack.push(STypeCustom.create(name));
    this.testMaxSize();
  }

  @Override
  public void visit(Data data) {
    Map<String, SemanticType> declarationsMap = new HashMap<>();

    if (this.dataMap.containsKey(data.getId())) {
      this.logError.add(data.getLine() + ", " + data.getCol() + ": Tipo " + data.getId() + " duplicado!");
      return;
    }
    this.dataMap.put(data.getId(), declarationsMap);

    for (Parameter declaration : data.getDeclarations()) {
      declaration.accept(this);
      this.testMaxSize();
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
    this.testMaxSize();
    SemanticType left, right;
    right = this.stack.pop();
    left = this.stack.pop();

    if ((right.match(typeInt) || right.match(typeFloat) || right.match(typeChar))
        && (left.match(typeInt) || left.match(typeFloat) || left.match(typeChar))) {
      this.stack.push(typeBool);
      exp.setSemanticType(typeBool);
    } else if (right.match(typeBool) && left.match(typeBool)) {
      this.stack.push(typeBool);
      exp.setSemanticType(typeBool);
    } else {
      logError.add(exp.getLine() + ", " + exp.getCol() + ": Operador == não se aplica aos tipos " + left.toString()
          + " e " + right.toString());
      this.stack.push(typeError);
    }
  }

  @Override
  public void visit(FalseSExpression node) {
    this.stack.push(this.typeBool);
    this.testMaxSize();
    node.setSemanticType(typeBool);
  }

  @Override
  public void visit(FloatBasicType node) {
    this.stack.push(this.typeFloat);
  }

  @Override
  public void visit(FloatSExpression node) {
    this.stack.push(this.typeFloat);
    node.setSemanticType(typeFloat);
  }

  @Override
  public void visit(Function function) {
    this.returnCheck = false;
    List<SemanticType> paramTypes = new ArrayList<>();
    for (Parameter parameter : function.getParameters()) {
      parameter.getType().accept(this);
      this.testMaxSize();
      paramTypes.add(this.stack.pop());
    }
    STypeFunctionKey functionKey = STypeFunctionKey.create(function.getId(), paramTypes);

    this.activeScope = env.get(functionKey);
    this.intCount = 0;
    for (int i = 0; i < function.getParameters().size(); i++) {
      this.activeScope.set(function.getParameters().get(i).getId(), new Pair<>(paramTypes.get(i), this.intCount++));
    }

    for (Command cmd : function.getCommands()) {
      cmd.accept(this);
      this.testMaxSize();
    }

    if (function.getReturnTypes().size() > 0 && !this.returnCheck) {
      this.logError.add(function.getLine() + ", " + function.getCol() + ": Função " + function.getId()
          + " deve retornar algum valor.");
    }

  }

  @Override
  public void visit(IdentifierLValue lvalue) {
    String id = lvalue.getID();
    Pair<SemanticType, Integer> lvalueTypePair = this.activeScope.get(id);

    if (lvalueTypePair == null) {
      this.activeScope.set(id, new Pair<>(this.typeNull, this.intCount++));
      this.stack.push(this.typeNull);
      lvalue.setSemanticType(typeNull);
    } else if (lvalueTypePair.getLeft().match(this.typeNull)) {
      this.activeScope.set(id, new Pair<>(this.typeNull, lvalueTypePair.getRight()));
      this.stack.push(this.typeNull);
      lvalue.setSemanticType(typeNull);
    } else {
      this.activeScope.set(id, new Pair<>(lvalueTypePair.getLeft(), lvalueTypePair.getRight()));
      this.stack.push(lvalueTypePair.getLeft());
      lvalue.setSemanticType(lvalueTypePair.getLeft());
    }
    this.testMaxSize();
  }

  @Override
  public void visit(IfCommand cmd) {
    cmd.getExpression().accept(this);
    SemanticType testType = this.stack.pop();
    if (testType.match(this.typeBool)) {
      cmd.getCommand().accept(this);
      this.returnCheck = false;
      return;
    }

    logError.add(cmd.getLine() + ", " + cmd.getCol() + ": Expressão de teste do IF deve ter tipo Bool");
  }

  @Override
  public void visit(IfElseCommand cmd) {
    boolean ifReturn, elseReturn;
    elseReturn = true;
    cmd.getExpression().accept(this);
    SemanticType testType = this.stack.pop();
    if (testType.match(this.typeBool)) {
      this.returnCheck = false;
      cmd.getIfCommand().accept(this);
      ifReturn = this.returnCheck;
      Command elseCmd = cmd.getElseCommand();
      if (elseCmd != null) {
        this.returnCheck = false;
        elseCmd.accept(this);
        elseReturn = this.returnCheck;
      }

      this.returnCheck = ifReturn && elseReturn;
      return;
    }

    logError.add(cmd.getLine() + ", " + cmd.getCol() + ": Expressão de teste do IF deve ter tipo Bool");
  }

  @Override
  public void visit(InequalityRExpression exp) {
    exp.getLeft().accept(this);
    exp.getRight().accept(this);
    this.testMaxSize();
    SemanticType left, right;
    right = this.stack.pop();
    left = this.stack.pop();

    if ((right.match(typeInt) || right.match(typeFloat) || right.match(typeChar))
        && (left.match(typeInt) || left.match(typeFloat) || left.match(typeChar))) {
      this.stack.push(typeBool);
      exp.setSemanticType(typeBool);
    } else if (right.match(typeBool) && left.match(typeBool)) {
      this.stack.push(typeBool);
      exp.setSemanticType(typeBool);
    } else {
      logError.add(exp.getLine() + ", " + exp.getCol() + ": Operador != não se aplica aos tipos " + left.toString()
          + " e " + right.toString());
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
    this.lastIndex = node.getValue();
    node.setSemanticType(typeInt);
    this.testMaxSize();
  }

  @Override
  public void visit(IterateCommand cmd) {
    cmd.getExpression().accept(this);
    SemanticType testType = this.stack.pop();
    if (testType.match(this.typeInt)) {
      cmd.getCommand().accept(this);
      return;
    }

    logError.add(cmd.getLine() + ", " + cmd.getCol() + ": Expressão de teste do IF deve ter tipo Int");
  }

  @Override
  public void visit(LessRExpression exp) {
    exp.getLeft().accept(this);
    exp.getRight().accept(this);
    this.testMaxSize();
    SemanticType left, right;
    right = this.stack.pop();
    left = this.stack.pop();

    if ((right.match(typeInt) || right.match(typeFloat) || right.match(typeChar))
        && (left.match(typeInt) || left.match(typeFloat) || left.match(typeChar))) {
      this.stack.push(typeBool);
      exp.setSemanticType(typeBool);
    } else {
      logError.add(exp.getLine() + ", " + exp.getCol() + ": Operador < não se aplica aos tipos " + left.toString()
          + " e " + right.toString());
      this.stack.push(typeError);
    }
  }

  @Override
  public void visit(ListCommand node) {
    for (Command cmd : node.getCommands()) {
      cmd.accept(this);
    }
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
    this.testMaxSize();
    SemanticType type = this.stack.pop();

    if (type.match(typeBool)) {
      this.stack.push(typeBool);
      exp.setSemanticType(typeBool);
    } else {
      logError.add(exp.getLine() + ", " + exp.getCol() + ": Operador ! não se aplica ao tipo " + type.toString());
      this.stack.push(typeError);
    }
  }

  @Override
  public void visit(NegativeSExpression exp) {
    exp.getSExpression().accept(this);
    this.testMaxSize();
    SemanticType type = this.stack.pop();
    if (type.match(typeInt) || type.match(typeChar)) {
      this.stack.push(typeInt);
      exp.setSemanticType(typeInt);
    } else if (type.match(typeFloat)) {
      this.stack.push(typeFloat);
      exp.setSemanticType(typeFloat);
    } else {
      logError.add(exp.getLine() + ", " + exp.getCol() + ": Operador ! não se aplica ao tipo " + type.toString());
      this.stack.push(typeError);
    }
  }

  @Override
  public void visit(NewPExpression exp) {
    exp.getType().accept(this);

    if (this.stack.empty()) {
      this.stack.push(this.typeError);
      return;
    }

    SemanticType baseType = this.stack.pop();
    Expression arrayExpression = exp.getExpression();

    if (baseType instanceof STypeError || (!this.dataMap.containsKey(baseType.toString()) && arrayExpression == null)) {
      this.logError.add(exp.getLine() + ", " + exp.getCol() + ": Tipo " + baseType + " de data não existe!");
      this.stack.push(this.typeError);
      return;
    }

    if (arrayExpression == null) {
      this.stack.push(baseType);
      exp.setSemanticType(baseType);
      return;
    }

    arrayExpression.accept(this);
    this.testMaxSize();
    SemanticType indexType = this.stack.pop();

    if (!indexType.match(this.typeInt)) {
      this.stack.push(this.typeError);
      this.logError.add(exp.getLine() + ", " + exp.getCol() + ": Índice deve ser tipo Int. "
          + indexType.toString() + " encontrado!");
      return;
    }

    STypeArray result = STypeArray.create(baseType);
    this.stack.push(result);
    exp.setSemanticType(result);
  }

  @Override
  public void visit(NullSExpression node) {
    this.stack.push(this.typeNull);
    node.setSemanticType(typeNull);
    this.testMaxSize();
  }

  @Override
  public void visit(ObjectLValue lvalue) {
    lvalue.getLValue().accept(this);
    this.testMaxSize();
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
    lvalue.setSemanticType(objectSType);
  }

  @Override
  public void visit(Parameter param) {
    param.getType().accept(this);
  }

  @Override
  public void visit(ParenthesisPExpression node) {
    Expression exp = node.getExpression();
    exp.accept(this);
    this.testMaxSize();
    node.setSemanticType(exp.getSemanticType());
  }

  @Override
  public void visit(PrintCommand cmd) {
    this.printCheck = true;
    cmd.getExpression().accept(this);
    this.printCheck = false;
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

      String id = function.getId();
      STypeFunctionKey functionKey = STypeFunctionKey.create(id, typeArgs);
      STypeFunction typeFunction = STypeFunction.create(typeArgs, typeReturns);
      function.setFunctionType(typeFunction);
      this.env.put(functionKey, new LocalEnv<>(id, typeFunction));

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
  public void visit(ReadCommand cmd) {
    LValueContext lvalueContext = cmd.getLValue();
    lvalueContext.accept(this);
    this.testMaxSize();
    SemanticType lValueType = this.stack.pop();

    if (lValueType.match(this.typeNull)) {
      String headId = lvalueContext.getLValue().getHeadId();
      this.activeScope.set(headId, new Pair<>(this.typeChar, this.intCount++));
      this.stack.push(this.typeChar);
      return;
    }
    
    cmd.setLabel(intCount++);
    this.stack.push(lValueType);
  }

  @Override
  public void visit(ReturnCommand cmd) {
    if (returnCheck) {
      this.logError.add(cmd.getLine() + ", " + cmd.getCol() + " Função já retornou um valor!");
      return;
    }
    this.returnCheck = true;
    cmd.setLabel(this.intCount++);
    STypeFunction funcType = this.activeScope.getFunctionType();
    List<Expression> returnExpressions = cmd.getReturnExpressions();

    int expectedAmountOfReturns = funcType.getReturnTypes().size();
    int actualAmountOfReturns = returnExpressions.size();

    if (actualAmountOfReturns != expectedAmountOfReturns) {
      this.stack.push(this.typeError);
      this.logError.add(cmd.getLine() + ", " + cmd.getCol() + ": Função espera " + expectedAmountOfReturns
          + " retornos, mas retorna " + actualAmountOfReturns + " valores!");
      return;
    }

    List<SemanticType> argTypes = funcType.getReturnTypes();
    for (int i = 0; i < returnExpressions.size(); i++) {
      returnExpressions.get(i).accept(this);
      this.testMaxSize();
      SemanticType expressionType = this.stack.pop();
      if (!argTypes.get(i).match(expressionType)) {
        this.logError.add(cmd.getLine() + ", " + cmd.getCol() + " Retorno " + (i + 1) + " espera "
            + argTypes.get(i).toString() + " mas recebeu " + expressionType);
      }
    }
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
    node.setSemanticType(typeBool);
    this.testMaxSize();
  }

  @Override
  public void visit(LValueContext context) {
    LValue lvalue = context.getLValue();
    lvalue.accept(this);
    this.testMaxSize();
    context.setSemanticType(lvalue.getSemanticType());
  }

  private void testMaxSize() {
    this.testMaxSize(this.stack.size());
  }

  private void testMaxSize(int overwrite) {
    if (this.activeScope == null) {
      return;
    }

    if (this.stack == null) {
      this.activeScope.assertMaxStackSize(0);
      return;
    }

    if (this.printCheck) {
      this.activeScope.assertMaxStackSize(overwrite + 1);
    }
    this.activeScope.assertMaxStackSize(overwrite);
  }

}
