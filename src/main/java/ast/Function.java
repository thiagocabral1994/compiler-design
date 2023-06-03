package ast;

import java.util.ArrayList;
import java.util.List;

import visitor.Visitable;
import visitor.Visitor;

public class Function extends Node implements Visitable {
  private String id;
  private List<Parameter> parameters;
  private List<Command> commands;
  private List<Type> returnTypes;

  public Function(int line, int col, String id) {
    super(line, col);
    this.id = id;
    this.parameters = new ArrayList<Parameter>();
    this.returnTypes = new ArrayList<Type>();
  }

  public void setParameters(List<Parameter> parameters) {this.parameters = parameters;}
  public boolean pushReturnType(Type returnType) { return this.returnTypes.add(returnType); }
  public boolean pushCommand(Command command) { return this.commands.add(command); }

  public String getId() { return this.id; }
  public List<Type> getReturnTypes() {return this.returnTypes; };
  public List<Parameter> getParameters() {return this.parameters; };
  public List<Command> getCommands() {return this.commands; };

  public void accept(Visitor visitor) {
    visitor.visit(this);
  }
}
