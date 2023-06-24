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

  @Override
  public void visit(AdditionAExpression node) {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'visit'");
  }

  @Override
  public void visit(AndExpression node) {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'visit'");
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
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'visit'");
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
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'visit'");
  }

  @Override
  public void visit(CustomBasicType node) {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'visit'");
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
  public void visit(DivisionMExpression node) {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'visit'");
  }

  @Override
  public void visit(EqualityRExpression node) {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'visit'");
  }

  @Override
  public void visit(FalseSExpression node) {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'visit'");
  }

  @Override
  public void visit(FloatBasicType node) {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'visit'");
  }

  @Override
  public void visit(FloatSExpression node) {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'visit'");
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
  public void visit(InequalityRExpression node) {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'visit'");
  }

  @Override
  public void visit(IntegerBasicType node) {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'visit'");
  }

  @Override
  public void visit(IntegerSExpression node) {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'visit'");
  }

  @Override
  public void visit(IterateCommand node) {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'visit'");
  }

  @Override
  public void visit(LessRExpression node) {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'visit'");
  }

  @Override
  public void visit(ListCommand node) {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'visit'");
  }

  @Override
  public void visit(ModulusMExpression node) {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'visit'");
  }

  @Override
  public void visit(MultiplicationMExpression node) {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'visit'");
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
  public void visit(Parameter node) {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'visit'");
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
    }

    // TODO: Validar a main:
    // 1) Se ela existe.
    // 2) Se ela é void.

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
  public void visit(SubtractionAExpression node) {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'visit'");
  }

  @Override
  public void visit(TrueSExpression node) {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'visit'");
  }

  @Override
  public void visit(LValueContext node) {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'visit'");
  }

}
