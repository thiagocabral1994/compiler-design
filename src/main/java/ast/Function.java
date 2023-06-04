package ast;

import java.util.List;

import visitor.Visitable;
import visitor.Visitor;

public class Function extends Node implements Visitable {
  private String id;
  private List<Parameter> parameters;
  private List<Command> commands;
  private List<Type> returnTypes;

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

  public void accept(Visitor visitor) {
    visitor.visit(this);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o)
      return true;
    if (o == null || getClass() != o.getClass())
      return false;
    Function that = (Function) o;
    if (!this.getId().equals(that.getId())) {
      return false;
    }

    return this.getParameters().equals(that.getParameters());
  }

  @Override
  public int hashCode() {
    int result = Integer.parseInt(this.getId());
    for (Parameter param : this.parameters) {
      result += Integer.parseInt(param.getId());
    }
    return result;
  }
}
