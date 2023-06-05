package visitor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Stack;

import ast.*;
import util.*;

public class InterpretVisitor extends Visitor {
  private static final String MAIN = "main";

  private Stack<HashMap<String, LValueObject>> env;
  private HashMap<String, Function> functions;
  private HashMap<String, List<String>> datas;
  private Stack<LValueObject> operands;
  private Stack<String> lValueTrace;
  private boolean returnMode, debug;

  public InterpretVisitor() {
    this.env = new Stack<HashMap<String, LValueObject>>();
    this.env.push(new HashMap<String, LValueObject>());
    this.functions = new HashMap<String, Function>();
    this.operands = new Stack<LValueObject>();
    this.lValueTrace = new Stack<String>();
    this.returnMode = false;
    this.debug = false;
  }

  public InterpretVisitor(boolean debug) {
    super();
    this.debug = debug;
  }

  @Override
  public void visit(AdditionAExpression exp) {
    try {
      exp.getLeft().accept(this);
      exp.getRight().accept(this);
      Object left, right;
      left = operands.pop();
      right = operands.pop();
      LValueObject result = new LValueObject(SumOperator.execute(left, right), this.env.peek());
      operands.push(result);
    } catch (Exception e) {
      throw new RuntimeException(" (" + exp.getLine() + ", " + exp.getCol() + ") " + e.getMessage());
    }
  }

  @Override
  public void visit(AndExpression exp) {
    try {
      exp.getLeft().accept(this);
      exp.getRight().accept(this);
      Object left, right;
      left = operands.pop();
      right = operands.pop();
      LValueObject result = new LValueObject(AndOperator.execute(left, right), this.env.peek());
      operands.push(result);
    } catch (Exception e) {
      throw new RuntimeException(" (" + exp.getLine() + ", " + exp.getCol() + ") " + e.getMessage());
    }
  }

  @Override
  public void visit(ArrayLValue lvalue) {
    try {
      lvalue.getExpression().accept(this);
      lValueTrace.push(this.operands.pop().toString());
      lvalue.getLValue().accept(this);
    } catch (Exception e) {
      throw new RuntimeException(" (" + lvalue.getLine() + ", " + lvalue.getCol() + ") " + e.getMessage() );
    }
  }

  @Override
  public void visit(ArrayType node) { /* Ignora */ }

  @Override
  public void visit(AssignmentCommand cmd) {
    try {
      cmd.getExpression().accept(this);
      Object value = operands.pop().getValue();

      cmd.getLValue().accept(this);
      LValueObject attrObj = this.operands.pop();
      attrObj.setValue(value);

      // Adicionando o resultado da atribuição
      this.operands.push(attrObj);
    } catch (Exception e) {
      throw new RuntimeException(" (" + cmd.getLine() + ", " + cmd.getCol() + ") " + e.getMessage() );
    }
  }

  @Override
  public void visit(BooleanBasicType node) { /* Ignora */}

  @Override
  public void visit(CallCommand cmd) {
    try {
      Function function = functions.get(cmd.getId());
      if (function == null) {
        throw new RuntimeException(
            " (" + cmd.getLine() + ", " + cmd.getCol() + ") Função " + cmd.getId() + " não existe!");
      }

      for (Expression exp : cmd.getExpressions()) {
        exp.accept(this);
      }
      function.accept(this);

      Stack<LValueObject> returnValues = new Stack<LValueObject>();
      for (int i = 0; i < cmd.getReturnLValues().size(); i++) {
        this.operands.pop();
        returnValues.push(this.operands.pop());
      }

      for (LValue returnLValue : cmd.getReturnLValues()) {
        returnLValue.accept(this);
        LValueObject oldValue = this.operands.pop();
        LValueObject returnValue = returnValues.pop();
        oldValue.setValue(returnValue);
      }
    } catch (Exception e) {
      throw new RuntimeException(" (" + cmd.getLine() + ", " + cmd.getCol() + ") " + e.getMessage() );
    }
  }

  @Override
  public void visit(CallPExpression node) {
    // TODO Auto-generated method stub
    System.out.println("CallPExpression");
  }

  @Override
  public void visit(CharSExpression exp) {
    try {
      LValueObject result = new LValueObject(Character.valueOf(exp.getValue()), this.env.peek());
      operands.push(result);
    } catch (Exception e) {
      throw new RuntimeException(" (" + exp.getLine() + ", " + exp.getCol() + ") " + e.getMessage() );
    }
  }

  @Override
  public void visit(CharacterBasicType node) { /* Ignora */ }

  @Override
  public void visit(CustomBasicType type) {
    if (datas.get(type.getTypeName()) == null) {
      throw new RuntimeException(" (" + type.getLine() + ", " + type.getCol() + ") " + type.getTypeName() + " não existe!" );
    }
  }

  @Override
  public void visit(Data data) {
    String id = data.getId();
    List<Parameter> declarations = data.getDeclarations();
    List<String> declarationIds = new ArrayList<String>();
    for (Parameter declaration : declarations) {
      declarationIds.add(declaration.getId());
    }
    datas.put(id, declarationIds);
  }

  @Override
  public void visit(DivisionMExpression exp) {
    try {
      exp.getLeft().accept(this);
      exp.getRight().accept(this);
      Object left, right;
      left = operands.pop();
      right = operands.pop();
      LValueObject result = new LValueObject(DivOperator.execute(left, right), this.env.peek());
      operands.push(result);
    } catch (Exception e) {
      throw new RuntimeException(" (" + exp.getLine() + ", " + exp.getCol() + ") " + e.getMessage());
    }
  }

  @Override
  public void visit(EqualityRExpression exp) {
    try {
      exp.getLeft().accept(this);
      exp.getRight().accept(this);
      Object left, right;
      left = operands.pop();
      right = operands.pop();
      LValueObject result = new LValueObject(EqualOperator.execute(left, right), this.env.peek());
      operands.push(result);
    } catch (Exception e) {
      throw new RuntimeException(" (" + exp.getLine() + ", " + exp.getCol() + ") " + e.getMessage());
    }
  }

  @Override
  public void visit(FalseSExpression exp) {
    try {
      LValueObject result = new LValueObject(Boolean.valueOf(false), this.env.peek());
      operands.push(result);
    } catch (Exception e) {
      throw new RuntimeException(" (" + exp.getLine() + ", " + exp.getCol() + ") " + e.getMessage() );
    }
  }

  @Override
  public void visit(FloatBasicType node) { /* Ignora */}

  @Override
  public void visit(FloatSExpression exp) {
    try {
      LValueObject result = new LValueObject(Float.valueOf(exp.getValue()), this.env.peek());
      operands.push(result);
    } catch (Exception e) {
      throw new RuntimeException(" (" + exp.getLine() + ", " + exp.getCol() + ") " + e.getMessage() );
    }
  }

  @Override
  public void visit(Function function) {
    HashMap<String, LValueObject> functionEnv = new HashMap<String, LValueObject>();
    List<Parameter> params = function.getParameters();
    for(int i = params.size() - 1; i >= 0; i--) {
      LValueObject newObject = new LValueObject(operands.pop(), this.env.peek());
      functionEnv.put(params.get(i).getId(), newObject);
    }
    env.push(functionEnv);
    for (Command cmd : function.getCommands()) {
      cmd.accept(this);
    }

    if(this.debug && function.getId().equals(MAIN)) {
      Object[] x = env.peek().keySet().toArray();
      System.out.println("-------------- Memória ----------------");
      for (int i = 0; i < x.length; i++) {
        System.out.println(((String)x[i]) + " : " + env.peek().get(x[i]).toString());
      }
    }

    env.pop();
    this.returnMode = false;
  }

  @Override
  public void visit(IdentifierLValue lvalue) {
    try {
      LValueObject variable = this.env.peek().get(lvalue.getID());
      while (!this.lValueTrace.empty()) {
        String id = this.lValueTrace.pop();
        // Caso 1: Objeto é um mapa para outros objetos.
        if (variable.getValue() instanceof HashMap) {
          HashMap<?,?> map = (HashMap<?,?>) variable.getValue();
          variable = (LValueObject) map.get(id);
        } else {
          throw new RuntimeException(
              " (" + lvalue.getLine() + ", " + lvalue.getCol() + ") " + id + " de " + lvalue.getID() + " não é um campo válido");
        }
      }

      if (!(variable.getValue() instanceof HashMap)) {
          throw new RuntimeException(
              " (" + lvalue.getLine() + ", " + lvalue.getCol() + ") " + lvalue.getID() + " não é um campo válido");
      }

      this.operands.push(variable);
    } catch (Exception e) {
      throw new RuntimeException(" (" + lvalue.getLine() + ", " + lvalue.getCol() + ") " + e.getMessage());
    }
  }

  @Override
  public void visit(IfCommand cmd) {
    try {
      cmd.getExpression().accept(this);
      if ((Boolean) operands.pop().getValue()) {
        cmd.getCommand().accept(this);
      }
    } catch (Exception e) {
      throw new RuntimeException(" (" + cmd.getLine() + ", " + cmd.getCol() + ") " + e.getMessage());
    }
  }

  @Override
  public void visit(IfElseCommand cmd) {
    try {
      cmd.getExpression().accept(this);
      if ((Boolean) operands.pop().getValue()) {
        cmd.getIfCommand();
      } else {
        cmd.getElseCommand();
      }
    } catch (Exception e) {
      throw new RuntimeException(" (" + cmd.getLine() + ", " + cmd.getCol() + ") " + e.getMessage());
    }
  }

  @Override
  public void visit(InequalityRExpression exp) {
    try {
      exp.getLeft().accept(this);
      exp.getRight().accept(this);
      Object left, right;
      left = operands.pop();
      right = operands.pop();
      LValueObject result = new LValueObject(!EqualOperator.execute(left, right), this.env.peek());
      operands.push(result);
    } catch (Exception e) {
      throw new RuntimeException(" (" + exp.getLine() + ", " + exp.getCol() + ") " + e.getMessage());
    }
  }

  @Override
  public void visit(IntegerBasicType node) { /* Ignora */}

  @Override
  public void visit(IntegerSExpression exp) {
    try {
      LValueObject result = new LValueObject(Integer.valueOf(exp.getValue()), this.env.peek());
      operands.push(result);
    } catch (Exception e) {
      throw new RuntimeException(" (" + exp.getLine() + ", " + exp.getCol() + ") " + e.getMessage() );
    }
  }

  @Override
  public void visit(IterateCommand cmd) {
    try {
      cmd.getExpression().accept(this);
      while ((Boolean) operands.pop().getValue()) {
        cmd.getCommand().accept(this);
        cmd.getExpression().accept(this);
      }
    } catch (Exception e) {
      throw new RuntimeException(" (" + cmd.getLine() + ", " + cmd.getCol() + ") " + e.getMessage());
    }
  }

  @Override
  public void visit(LessRExpression exp) {
    try {
      exp.getLeft().accept(this);
      exp.getRight().accept(this);
      Object left, right;
      left = operands.pop();
      right = operands.pop();
      LValueObject result = new LValueObject(LessOperator.execute(left, right), this.env.peek());
      operands.push(result);
    } catch (Exception e) {
      throw new RuntimeException(" (" + exp.getLine() + ", " + exp.getCol() + ") " + e.getMessage());
    }
  }

  @Override
  public void visit(ListCommand node) {
    for (Command cmd : node.getCommands()) {
      if (this.returnMode) {
        return;
      }
      cmd.accept(this);
    }
  }

  @Override
  public void visit(ModulusMExpression exp) {
    try {
      exp.getLeft().accept(this);
      exp.getRight().accept(this);
      Object left, right;
      left = operands.pop();
      right = operands.pop();
      LValueObject result = new LValueObject(ModOperator.execute(left, right), this.env.peek());
      operands.push(result);
    } catch (Exception e) {
      throw new RuntimeException(" (" + exp.getLine() + ", " + exp.getCol() + ") " + e.getMessage());
    }
  }

  @Override
  public void visit(MultiplicationMExpression exp) {
    try {
      exp.getLeft().accept(this);
      exp.getRight().accept(this);
      Object left, right;
      left = operands.pop();
      right = operands.pop();
      LValueObject result = new LValueObject(MultOperator.execute(left, right), this.env.peek());
      operands.push(result);
    } catch (Exception e) {
      throw new RuntimeException(" (" + exp.getLine() + ", " + exp.getCol() + ") " + e.getMessage());
    }
  }

  @Override
  public void visit(NegationSExpression exp) {
    try {
      exp.getSExpression().accept(this);
      LValueObject result = new LValueObject(NotOperator.execute(this.operands.pop().getValue()), this.env.peek());
      operands.push(result);
    } catch (Exception e) {
      throw new RuntimeException(" (" + exp.getLine() + ", " + exp.getCol() + ") " + e.getMessage());
    }
  }

  @Override
  public void visit(NegativeSExpression exp) {
    try {
      exp.getSExpression().accept(this);
      LValueObject result = new LValueObject(NegOperator.execute(this.operands.pop().getValue()), this.env.peek());
      operands.push(result);
    } catch (Exception e) {
      throw new RuntimeException(" (" + exp.getLine() + ", " + exp.getCol() + ") " + e.getMessage());
    }
  }

  @Override
  public void visit(NewPExpression exp) {
    try {
      String typeName = exp.getType().getTypeName();
      Expression arrayExp = exp.getExpression();

      Object newData = null;
      if (arrayExp != null) {
        arrayExp.accept(this);
        Object size = this.operands.pop();
        if (size instanceof Integer) {
          HashMap<String, LValueObject> newMap = new HashMap<>();
          for (int i = 0; i < (Integer) size; i++) {
            newMap.put(Integer.valueOf(i).toString(), null);
          }
          newData = newMap;
        } else {
          throw new RuntimeException(" (" + exp.getLine() + ", " + exp.getCol() + ") Erro ao criar nova instância de " + typeName );
        }
      } else {
        newData = new Object();
      }
      LValueObject result = new LValueObject(newData, this.env.peek());
      operands.push(result);
    } catch (Exception e) {
      throw new RuntimeException(" (" + exp.getLine() + ", " + exp.getCol() + ") " + e.getMessage());
    }
  }

  @Override
  public void visit(NullSExpression exp) {
    try {
      operands.push(null);
    } catch (Exception e) {
      throw new RuntimeException(" (" + exp.getLine() + ", " + exp.getCol() + ") " + e.getMessage() );
    }
  }

  @Override
  public void visit(ObjectLValue lvalue) {
    try {
      this.lValueTrace.push(lvalue.getParamID());
      lvalue.getLValue().accept(this);
    } catch (Exception e) {
      throw new RuntimeException(" (" + lvalue.getLine() + ", " + lvalue.getCol() + ") " + e.getMessage() );
    }
  }

  @Override
  public void visit(Parameter node) { /* Ignora */ }

  @Override
  public void visit(ParenthesisPExpression exp) {
    exp.accept(this);
  }

  @Override
  public void visit(PrintCommand cmd) {
    try {
      cmd.getExpression().accept(this);
      System.out.println(this.operands.pop());
    } catch (Exception e) {
      throw new RuntimeException(" (" + cmd.getLine() + ", " + cmd.getCol() + ") " + e.getMessage());
    }
  }

  @Override
  public void visit(Program program) {
    Function main = null;
    for(Data data : program.getDatas()) {
      data.accept(this);
    }

    for(Function function : program.getFunctions()) {
      functions.put(function.getId(), function);
      if (function.getId().equals(MAIN)) {
        main = function; 
      }
    }
    
    if (main == null) {
      throw new RuntimeException("Não há uma função chamada `main`!");
    }
    main.accept(this);
  }

  @Override
  public void visit(ReadCommand cmd) {
    try {
      cmd.getLValue().accept(this);
      System.out.print(this.operands.pop());
      //TODO
    } catch (Exception e) {
      throw new RuntimeException(" (" + cmd.getLine() + ", " + cmd.getCol() + ") " + e.getMessage());
    }
  }

  @Override
  public void visit(ReturnCommand cmd) {
    for (Expression exp : cmd.getReturnExpressions()) {
      exp.accept(this);
    }
    this.returnMode = true;
  }

  @Override
  public void visit(SubtractionAExpression exp) {
    try {
      exp.getLeft().accept(this);
      exp.getRight().accept(this);
      Object left, right;
      left = operands.pop();
      right = operands.pop();
      LValueObject result = new LValueObject(SubOperator.execute(left, right), this.env.peek());
      operands.push(result);
    } catch (Exception e) {
      throw new RuntimeException(" (" + exp.getLine() + ", " + exp.getCol() + ") " + e.getMessage());
    }
  }

  @Override
  public void visit(TrueSExpression exp) {
    try {
      LValueObject result = new LValueObject(Boolean.valueOf(true), this.env.peek());
      operands.push(result);
    } catch (Exception e) {
      throw new RuntimeException(" (" + exp.getLine() + ", " + exp.getCol() + ") " + e.getMessage() );
    }
  }
}
