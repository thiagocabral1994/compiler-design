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

  public int getNumErrors() { return this.logError.size(); }

  public void printErrors() {
    for (String string : this.logError) {
      System.out.println(string);
    }
  }


  private void typeArithmeticBinOp(Node n, String opName){
    SemanticType left, right;
    right = this.stack.pop();
    left = this.stack.pop();


    if( (right.match(typeInt) ) ){
        if(left.match(typeInt) || left.match(typeFloat) || left.match(typeChar)){
          this.stack.push(left);
        }else{
          logError.add( n.getLine() + ", " + n.getCol() + ": Operador" + opName +"não se aplica aos tipos " + left.toString() + " e " + right.toString() );
          this.stack.push(typeError);
        }
       
    }else if(right.match(typeFloat)){
      if(left.match(typeInt) || left.match(typeFloat) || left.match(typeChar)){
          this.stack.push(left);
      }else{
          logError.add( n.getLine() + ", " + n.getCol() + ": Operador " + opName +" não se aplica aos tipos " + left.toString() + " e " + right.toString() );
          this.stack.push(typeError);
      }
    }else if(right.match(typeChar)){
      if(left.match(typeInt) || left.match(typeFloat) || left.match(typeChar)){
          this.stack.push(left);
      }else{
          logError.add( n.getLine() + ", " + n.getCol() + ": Operador " + opName +" não se aplica aos tipos " + left.toString() + " e " + right.toString() );
          this.stack.push(typeError);
      }
    }else{
        logError.add( n.getLine() + ", " + n.getCol() + ": Operador " + opName +" não se aplica aos tipos " + left.toString() + " e " + right.toString() );
        this.stack.push(typeError);
    }
  }



  @Override
  public void visit(AdditionAExpression exp) {
    exp.getLeft().accept(this);
    exp.getRight().accept(this);
    typeArithmeticBinOp(exp,"+");
  }

  @Override
  public void visit(AndExpression exp) {
    exp.getLeft().accept(this);
    exp.getRight().accept(this);
    SemanticType left, right;
    right = this.stack.pop();
    left = this.stack.pop();


    if(right.match(typeBool) && left.match(typeBool)) {
      this.stack.push(typeBool);
    }
    else {
      logError.add( exp.getLine() + ", " + exp.getCol() + ": Operador & não se aplica aos tipos " + left.toString() + " e " + right.toString() );
      this.stack.push(typeError);
    }


  }


  @Override
  public void visit(ArrayLValue node) {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'visit'");
  }

  @Override
  public void visit(ArrayType node) {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'visit'");
  }

  @Override
  public void visit(AssignmentCommand node) {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'visit'");
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

    for (Parameter declaration : data.getDeclarations()) {
      declaration.accept(this);
      declarationsMap.put(declaration.getId(), this.stack.pop());
    }

    this.dataMap.put(data.getId(), declarationsMap);
  }

  @Override
  public void visit(DivisionMExpression exp) {
    exp.getLeft().accept(this);
    exp.getRight().accept(this);
    typeArithmeticBinOp(exp,"/");    
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
      logError.add( exp.getLine() + ", " + exp.getCol() + ": Operador & não se aplica aos tipos " + left.toString() + " e " + right.toString() );
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
      this.logError.add(function.getLine() + ", " + function.getCol() + ": Função " + function.getId() + " deve retornar algum valor.");
    }
  }

  @Override
  public void visit(IdentifierLValue node) {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'visit'");
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
      logError.add( exp.getLine() + ", " + exp.getCol() + ": Operador & não se aplica aos tipos " + left.toString() + " e " + right.toString() );
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
    } else if (right.match(typeBool) && left.match(typeBool)) {
      this.stack.push(typeBool);
    }
    else {
      logError.add( exp.getLine() + ", " + exp.getCol() + ": Operador & não se aplica aos tipos " + left.toString() + " e " + right.toString() );
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
    typeArithmeticBinOp(exp,"%");
  }

  @Override
  public void visit(MultiplicationMExpression exp) {
    exp.getLeft().accept(this);
    exp.getRight().accept(this);
    typeArithmeticBinOp(exp,"*");
  }

  @Override
  public void visit(NegationSExpression node) {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'visit'");
  }

  @Override
  public void visit(NegativeSExpression node) {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'visit'");
  }

  @Override
  public void visit(NewPExpression node) {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'visit'");
  }

  @Override
  public void visit(NullSExpression node) {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'visit'");
  }

  @Override
  public void visit(ObjectLValue node) {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'visit'");
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
          this.logError.add(function.getLine() + ", " + function.getCol() + " Função main não deve ter argumentos ou tipo de retorno.");
        }
      }
    }

    if (main == null) {
      this.logError.add(program.getLine() + ", " + program.getCol() + " Programa deve ter uma função main.");
    }

    for(Function function : program.getFunctions()) {
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
    typeArithmeticBinOp(exp,"-");
  }

  @Override
  public void visit(TrueSExpression node) {
    this.stack.push(this.typeBool);
  }

  @Override
  public void visit(LValueContext node) {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'visit'");
  }

}
