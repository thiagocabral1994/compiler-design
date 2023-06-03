package ast;

import visitor.Visitable;
import visitor.Visitor;

public class Parameter extends Node implements Visitable {
  private String id;
  private Type type;

  public Parameter(int line, int col, String id, Type type) {
    super(line, col);
    this.id = id;
    this.type = type;
  }

  public String getId() { return this.id; }
  public Type getType() { return this.type; }

  public void accept(Visitor visitor) {
    visitor.visit(this);
  }
}
