package visitor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Stack;

import ast.*;
import util.*;

public class InterpretVisitor extends Visitor {
  private static final String MAIN = "main";

  private Stack<HashMap<String, Object>> env;
  private HashMap<String, Function> functions;
  private HashMap<String, List<String>> datas;
  private Stack<Object> operands;
  private boolean returnMode, debug;

  public InterpretVisitor() {
    this.env = new Stack<HashMap<String, Object>>();
    this.env.push(new HashMap<String, Object>());
    this.functions = new HashMap<String, Function>();
    this.operands = new Stack<Object>();
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
      operands.push(SumOperator.execute(left, right));
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
      operands.push(AndOperator.execute(left, right));
    } catch (Exception e) {
      throw new RuntimeException(" (" + exp.getLine() + ", " + exp.getCol() + ") " + e.getMessage());
    }
  }

  @Override
  public void visit(ArrayLValue lvalue) {
    try {
      lvalue.getLValue().accept(this);
      lvalue.getExpression().accept(this);
    } catch (Exception e) {
      throw new RuntimeException(" (" + lvalue.getLine() + ", " + lvalue.getCol() + ") " + e.getMessage() );
    }
  }

  @Override
  public void visit(ArrayType node) { /* Ignora */ }

  @Override
  public void visit(AssignmentCommand cmd) {
    try {
      LValue lvalue = cmd.getLValue();
      Expression exp = cmd.getExpression();
      exp.accept(this);
      Object value = operands.pop();

      String headID = LValueTracker.getHeadID(lvalue);
      Object variable = env.peek().get(headID);
      if (variable == null) {
        throw new RuntimeException(
            " (" + lvalue.getLine() + ", " + lvalue.getCol() + ") Variável não existe" + headID);
      }
      // Object newVariable = LValueTracker.assignValue(lvalue, variable, value);
      // env.peek().put(headID, newVariable);
    } catch (Exception e) {
      throw new RuntimeException(" (" + cmd.getLine() + ", " + cmd.getCol() + ") " + e.getMessage() );
    }
  }

  @Override
  public void visit(BooleanBasicType node) { /* Ignora */}

  @Override
  public void visit(CallCommand node) {
    // TODO Auto-generated method stub
    System.out.println("CallCommand");
  }

  @Override
  public void visit(CallPExpression node) {
    // TODO Auto-generated method stub
    System.out.println("CallPExpression");
  }

  @Override
  public void visit(CharSExpression exp) {
    try {
      operands.push(Character.valueOf(exp.getValue()));
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
      operands.push(DivOperator.execute(left, right));
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
      operands.push(EqualOperator.execute(left, right));
    } catch (Exception e) {
      throw new RuntimeException(" (" + exp.getLine() + ", " + exp.getCol() + ") " + e.getMessage());
    }
  }

  @Override
  public void visit(FalseSExpression exp) {
    try {
      operands.push(Boolean.valueOf(false));
    } catch (Exception e) {
      throw new RuntimeException(" (" + exp.getLine() + ", " + exp.getCol() + ") " + e.getMessage() );
    }
  }

  @Override
  public void visit(FloatBasicType node) { /* Ignora */}

  @Override
  public void visit(FloatSExpression exp) {
    try {
      operands.push(Float.valueOf(exp.getValue()));
    } catch (Exception e) {
      throw new RuntimeException(" (" + exp.getLine() + ", " + exp.getCol() + ") " + e.getMessage() );
    }
  }

  @Override
  public void visit(Function function) {
    HashMap<String, Object> functionEnv = new HashMap<String, Object>();
    List<Parameter> params = function.getParameters();
    for(int i = params.size() - 1; i >= 0; i--) {
      functionEnv.put(params.get(i).getId(), operands.pop());
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
  public void visit(IdentifierLValue lvalue) { /* Ignora */ }

  @Override
  public void visit(IfCommand cmd) {
    try {
      cmd.getExpression().accept(this);
      if ((Boolean) operands.pop()) {
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
      if ((Boolean) operands.pop()) {
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
      operands.push(!EqualOperator.execute(left, right));
    } catch (Exception e) {
      throw new RuntimeException(" (" + exp.getLine() + ", " + exp.getCol() + ") " + e.getMessage());
    }
  }

  @Override
  public void visit(IntegerBasicType node) { /* Ignora */}

  @Override
  public void visit(IntegerSExpression exp) {
    try {
      operands.push(Integer.valueOf(exp.getValue()));
    } catch (Exception e) {
      throw new RuntimeException(" (" + exp.getLine() + ", " + exp.getCol() + ") " + e.getMessage() );
    }
  }

  @Override
  public void visit(IterateCommand cmd) {
    try {
      cmd.getExpression().accept(this);
      while ((Boolean) operands.pop()) {
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
      operands.push(LessOperator.execute(left, right));
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
      operands.push(ModOperator.execute(left, right));
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
      operands.push(MultOperator.execute(left, right));
    } catch (Exception e) {
      throw new RuntimeException(" (" + exp.getLine() + ", " + exp.getCol() + ") " + e.getMessage());
    }
  }

  @Override
  public void visit(NegationSExpression node) {
    // TODO Auto-generated method stub
    System.out.println("NegationSExpression");
  }

  @Override
  public void visit(NegativeSExpression node) {
    // TODO Auto-generated method stub
    System.out.println("NegativeSExpression");
  }

  @Override
  public void visit(NewPExpression node) {
    // TODO Auto-generated method stub
    System.out.println("NewPExpression");
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
    // TODO Auto-generated method stub
    System.out.println("PrintCommand");
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
  public void visit(ReadCommand node) {
    // TODO Auto-generated method stub
    System.out.println("ReadCommand");
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
      operands.push(SubOperator.execute(left, right));
    } catch (Exception e) {
      throw new RuntimeException(" (" + exp.getLine() + ", " + exp.getCol() + ") " + e.getMessage());
    }
  }

  @Override
  public void visit(TrueSExpression exp) {
    try {
      operands.push(Boolean.valueOf(true));
    } catch (Exception e) {
      throw new RuntimeException(" (" + exp.getLine() + ", " + exp.getCol() + ") " + e.getMessage() );
    }
  }
}
