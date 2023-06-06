package visitor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;
import java.util.Stack;

import ast.*;
import util.*;

public class InterpretVisitor extends Visitor {
  private static final String MAIN = "main";

  private Stack<HashMap<String, Operand>> env;
  private HashMap<String, Function> functions;
  private HashMap<String, List<String>> datas;
  private Stack<Operand> operands;
  private Stack<Stack<String>> lValueTrace;
  private boolean returnMode, debug;

  public InterpretVisitor() {
    this.env = new Stack<HashMap<String, Operand>>();
    this.env.push(new HashMap<String, Operand>());
    this.datas = new HashMap<String, List<String>>();
    this.functions = new HashMap<String, Function>();
    this.operands = new Stack<Operand>();
    this.lValueTrace = new Stack<Stack<String>>();
    this.lValueTrace.push(new Stack<String>());
    this.returnMode = false;
    this.debug = false;
  }

  public InterpretVisitor(boolean debug) {
    this();
    this.debug = debug;
  }

  @Override
  public void visit(AdditionAExpression exp) {
    try {
      exp.getLeft().accept(this);
      exp.getRight().accept(this);
      Operand left, right;
      right = operands.pop();
      left = operands.pop();
      Operand result = new Operand(SumOperator.execute(left.getValue(), right.getValue()));
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
      Operand left, right;
      right = operands.pop();
      left = operands.pop();
      Operand result = new Operand(AndOperator.execute(left.getValue(), right.getValue()));
      operands.push(result);
    } catch (Exception e) {
      throw new RuntimeException(" (" + exp.getLine() + ", " + exp.getCol() + ") " + e.getMessage());
    }
  }

  @Override
  public void visit(ArrayLValue lvalue) {
    try {
      lvalue.getExpression().accept(this);
      lValueTrace.peek().push(this.operands.pop().getValue().toString());
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
      Operand value = operands.pop();

      LValueContext ctx = cmd.getLValueContext();
      ctx.accept(this);
      Operand attrObj = this.operands.pop();
      if (
        (attrObj.getValue() == null && this.env.peek().get(ctx.getLValue().getHeadId()) == null)
        || ctx.getLValue() instanceof IdentifierLValue
        ) {
        this.env.peek().put(ctx.getLValue().getHeadId(), new Operand(value.getValue()));
      } else {
        // Alteramos o valor do Operando, mas sem alterar a referência
        attrObj.setValue(value.getValue());
      }

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
      Function function = this.functions.get(cmd.getId());
      if (function == null) {
        throw new RuntimeException(
            " (" + cmd.getLine() + ", " + cmd.getCol() + ") Função " + cmd.getId() + " não existe!");
      }

      for (Expression exp : cmd.getExpressions()) {
        exp.accept(this);
      }
      function.accept(this);

      Stack<Operand> returnValues = new Stack<Operand>();
      for (int i = 0; i < cmd.getReturnLValueContexts().size(); i++) {
        returnValues.push(this.operands.pop());
      }

      for (LValueContext returnLValue : cmd.getReturnLValueContexts()) {
        returnLValue.accept(this);
        Operand oldValue = this.operands.pop();
        Operand returnValue = returnValues.pop();
        oldValue.setValue(returnValue);
      }
    } catch (Exception e) {
      throw new RuntimeException(" (" + cmd.getLine() + ", " + cmd.getCol() + ") " + e.getMessage() );
    }
  }

  @Override
  public void visit(CallPExpression exp) {
    try {
      Function function = functions.get(exp.getID());
      if (function == null) {
        throw new RuntimeException(
            " (" + exp.getLine() + ", " + exp.getCol() + ") Função " + exp.getID() + " não existe!");
      }

      for (Expression paramExp : exp.getParamExpressions()) {
        paramExp.accept(this);
      }
      function.accept(this);

      exp.getBracketExpression().accept(this);
      Object returnIndex = this.operands.pop().getValue();
      
      if (!(returnIndex instanceof Integer)) {
        throw new RuntimeException(" (" + exp.getLine() + ", " + exp.getCol() + ") Índice de retorno deve ser um inteiro" );
      }

      for (int i = function.getReturnTypes().size() - 1; i > (Integer) returnIndex; i--) {
        /**
         * Retiramos todos os valores indesejados. Ex:
         * [1, 2, 3, 4 ,5].
         * 
         * Se queremos o índice `3`, então fazemos pop de `4` e `5`.
         */
        this.operands.pop();
      }
    } catch (Exception e) {
      throw new RuntimeException(" (" + exp.getLine() + ", " + exp.getCol() + ") " + e.getMessage() );
    }
  }

  @Override
  public void visit(CharSExpression exp) {
    try {
      Operand result = new Operand(Character.valueOf(exp.getValue()));
      operands.push(result);
    } catch (Exception e) {
      throw new RuntimeException(" (" + exp.getLine() + ", " + exp.getCol() + ") " + e.getMessage() );
    }
  }

  @Override
  public void visit(CharacterBasicType node) { /* Ignora */ }

  @Override
  public void visit(CustomBasicType type) {
    if (this.datas.get(type.getTypeName()) == null) {
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
    this.datas.put(id, declarationIds);
  }

  @Override
  public void visit(DivisionMExpression exp) {
    try {
      exp.getLeft().accept(this);
      exp.getRight().accept(this);
      Operand left, right;
      right = operands.pop();
      left = operands.pop();
      Operand result = new Operand(DivOperator.execute(left.getValue(), right.getValue()));
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
      Operand left, right;
      right = operands.pop();
      left = operands.pop();
      Operand result = new Operand(EqualOperator.execute(left.getValue(), right.getValue()));
      operands.push(result);
    } catch (Exception e) {
      throw new RuntimeException(" (" + exp.getLine() + ", " + exp.getCol() + ") " + e.getMessage());
    }
  }

  @Override
  public void visit(FalseSExpression exp) {
    try {
      Operand result = new Operand(Boolean.valueOf(false));
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
      Operand result = new Operand(Float.valueOf(exp.getValue()));
      operands.push(result);
    } catch (Exception e) {
      throw new RuntimeException(" (" + exp.getLine() + ", " + exp.getCol() + ") " + e.getMessage() );
    }
  }

  @Override
  public void visit(Function function) {
    HashMap<String, Operand> functionEnv = new HashMap<String, Operand>();
    List<Parameter> params = function.getParameters();
    for(int i = params.size() - 1; i >= 0; i--) {
      Operand newObject = operands.pop();
      functionEnv.put(params.get(i).getId(), newObject);
    }
    env.push(functionEnv);
    for (Command cmd : function.getCommands()) {
      if (!this.returnMode) {
        cmd.accept(this);
      }
    }

    if(this.debug && function.getId().equals(MAIN)) {
      Object[] x = env.peek().keySet().toArray();
      System.out.println("-------------- Memória ----------------");
      for (int i = 0; i < x.length; i++) {
        System.out.println(((String)x[i]) + " : " + env.peek().get(x[i]).getValue().toString());
      }
    }

    env.pop();
    this.returnMode = false;
  }

  @Override
  public void visit(IdentifierLValue lvalue) {
    try {
      Operand variable = this.env.peek().get(lvalue.getID());
      while (!this.lValueTrace.peek().empty()) {
        String id = this.lValueTrace.peek().pop();
        if (variable != null && variable.getValue() instanceof HashMap) {
          HashMap<?,?> map = (HashMap<?,?>) variable.getValue();
          variable = (Operand) map.get(id);
        } else {
          throw new RuntimeException(
              " (" + lvalue.getLine() + ", " + lvalue.getCol() + ") " + id + " de " + lvalue.getID() + " não é um campo válido");
        }
      }

      if (variable == null) {
        this.operands.push(new Operand(null));
        return;
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
        cmd.getIfCommand().accept(this);
      } else {
        cmd.getElseCommand().accept(this);
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
      Operand left, right;
      right = operands.pop();
      left = operands.pop();
      Operand result = new Operand(!EqualOperator.execute(left.getValue(), right.getValue()));
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
      Operand result = new Operand(Integer.valueOf(exp.getValue()));
      operands.push(result);
    } catch (Exception e) {
      throw new RuntimeException(" (" + exp.getLine() + ", " + exp.getCol() + ") " + e.getMessage() );
    }
  }

  @Override
  public void visit(IterateCommand cmd) {
    try {
      cmd.getExpression().accept(this);
      Object expValue = this.operands.pop().getValue(); 
      for (int i = 0; i < (Integer) expValue; i++) {
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
      Operand left, right;
      right = operands.pop();
      left = operands.pop();
      Operand result = new Operand(LessOperator.execute(left.getValue(), right.getValue()));
      operands.push(result);
    } catch (Exception e) {
      throw new RuntimeException(" (" + exp.getLine() + ", " + exp.getCol() + ") " + e.getMessage());
    }
  }

  @Override
  public void visit(ListCommand node) {
    for (Command cmd : node.getCommands()) {
      if (!this.returnMode) {
        cmd.accept(this);
      }
    }
  }

  @Override
  public void visit(ModulusMExpression exp) {
    try {
      exp.getLeft().accept(this);
      exp.getRight().accept(this);
      Operand left, right;
      right = operands.pop();
      left = operands.pop();
      Operand result = new Operand(ModOperator.execute(left.getValue(), right.getValue()));
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
      Operand left, right;
      right = operands.pop();
      left = operands.pop();
      Operand result = new Operand(MultOperator.execute(left.getValue(), right.getValue()));
      operands.push(result);
    } catch (Exception e) {
      throw new RuntimeException(" (" + exp.getLine() + ", " + exp.getCol() + ") " + e.getMessage());
    }
  }

  @Override
  public void visit(NegationSExpression exp) {
    try {
      exp.getSExpression().accept(this);
      Operand result = new Operand(NotOperator.execute(this.operands.pop().getValue()));
      operands.push(result);
    } catch (Exception e) {
      throw new RuntimeException(" (" + exp.getLine() + ", " + exp.getCol() + ") " + e.getMessage());
    }
  }

  @Override
  public void visit(NegativeSExpression exp) {
    try {
      exp.getSExpression().accept(this);
      Operand result = new Operand(NegOperator.execute(this.operands.pop().getValue()));
      operands.push(result);
    } catch (Exception e) {
      throw new RuntimeException(" (" + exp.getLine() + ", " + exp.getCol() + ") " + e.getMessage());
    }
  }

  @Override
  public void visit(NewPExpression exp) {
    try {
      String typeName = exp.getType().getTypeName();
      Expression arrayIndexExp = exp.getExpression();

      Object newData = null;
      if (arrayIndexExp != null) {
        arrayIndexExp.accept(this);
        Object size = this.operands.pop().getValue();
        if (size instanceof Integer) {
          HashMap<String, Operand> newMap = new HashMap<>();
          for (int i = 0; i < (Integer) size; i++) {
            newMap.put(Integer.valueOf(i).toString(), new Operand(null));
          }
          newData = newMap;
        } else {
          throw new RuntimeException(" (" + exp.getLine() + ", " + exp.getCol() + ") Erro ao criar nova instância de " + typeName );
        }
      } else {
        HashMap<String, Operand> objMap = new HashMap<String, Operand>();
        for (String argName : this.datas.get(typeName)) {
          objMap.put(argName, new Operand(null));
        }
        newData = objMap;
      }
      Operand result = new Operand(newData);
      operands.push(result);
    } catch (Exception e) {
      throw new RuntimeException(" (" + exp.getLine() + ", " + exp.getCol() + ") " + e.getMessage());
    }
  }

  @Override
  public void visit(NullSExpression exp) {
    try {
      operands.push(new Operand(null));
    } catch (Exception e) {
      throw new RuntimeException(" (" + exp.getLine() + ", " + exp.getCol() + ") " + e.getMessage() );
    }
  }

  @Override
  public void visit(ObjectLValue lvalue) {
    try {
      this.lValueTrace.peek().push(lvalue.getParamID());
      lvalue.getLValue().accept(this);
    } catch (Exception e) {
      throw new RuntimeException(" (" + lvalue.getLine() + ", " + lvalue.getCol() + ") " + e.getMessage() );
    }
  }

  @Override
  public void visit(Parameter node) { /* Ignora */ }

  @Override
  public void visit(ParenthesisPExpression exp) {
    exp.getExpression().accept(this);
  }

  @Override
  public void visit(PrintCommand cmd) {
    try {
      cmd.getExpression().accept(this);
      System.out.print(this.operands.pop().getValue());
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
      this.functions.put(function.getId(), function);
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
      LValue lvalue = cmd.getLValue();
      lvalue.accept(this);
      Operand lvalueObj = this.operands.pop();
      Scanner scanner = new Scanner(System.in);
      Object value = null;
      if (lvalueObj.getValue() instanceof Boolean) {
        value = Boolean.valueOf(scanner.nextBoolean());
      } else if (lvalueObj.getValue() instanceof Integer) {
        value = Integer.valueOf(scanner.nextInt());
      } else if (lvalueObj.getValue() instanceof Float) {
        value = Float.valueOf(scanner.nextFloat());
      } else if (lvalueObj.getValue() instanceof Character) {
        value = Character.valueOf(scanner.next().charAt(0));
      } else {
        value = scanner.next();
      }
      scanner.close();

      if ((lvalueObj.getValue() == null && this.env.peek().get(lvalue.getHeadId()) == null)
          || lvalue instanceof IdentifierLValue) {
        this.env.peek().put(lvalue.getHeadId(), new Operand(value));
      } else {
        lvalueObj.setValue(value);
      }
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
    System.out.println("RETURN: "+this.returnMode);
  }

  @Override
  public void visit(SubtractionAExpression exp) {
    try {
      exp.getLeft().accept(this);
      exp.getRight().accept(this);
      Operand left, right;
      right = operands.pop();
      left = operands.pop();
      Operand result = new Operand(SubOperator.execute(left.getValue(), right.getValue()));
      operands.push(result);
    } catch (Exception e) {
      throw new RuntimeException(" (" + exp.getLine() + ", " + exp.getCol() + ") " + e.getMessage());
    }
  }

  @Override
  public void visit(TrueSExpression exp) {
    try {
      Operand result = new Operand(Boolean.valueOf(true));
      operands.push(result);
    } catch (Exception e) {
      throw new RuntimeException(" (" + exp.getLine() + ", " + exp.getCol() + ") " + e.getMessage() );
    }
  }

  @Override
  public void visit(LValueContext node) {
    try {
      this.lValueTrace.push(new Stack<>());
      node.getLValue().accept(this);
      this.lValueTrace.pop();
    } catch (Exception e) {
      throw new RuntimeException(" (" + node.getLine() + ", " + node.getCol() + ") " + e.getMessage() );
    }
  }
}
