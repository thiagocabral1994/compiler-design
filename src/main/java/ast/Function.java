package ast;

import java.util.List;

import util.semantic.STypeFunction;
import visitor.Visitable;
import visitor.Visitor;

public class Function extends Node implements Visitable {
  private String id;
  private List<Parameter> parameters;
  private List<Command> commands;
  private List<Type> returnTypes;
  private STypeFunction type;

  public Function(int line, int col, String id, List<Parameter> params, List<Command> cmds, List<Type> returnTypes) {
    super(line, col);
    this.id = id;
    this.parameters = params;
    this.commands = cmds;
    this.returnTypes = returnTypes;
  }

  public String getId() { return this.id; }
  public List<Type> getReturnTypes() {return this.returnTypes; };
  public List<Parameter> getParameters() {return this.parameters; };
  public List<Command> getCommands() {return this.commands; };
  public STypeFunction getFunctionType() {return this.type; }
  public void setFunctionType(STypeFunction type) { this.type = type; }

  public void accept(Visitor visitor) {
    visitor.visit(this);
  }
}
