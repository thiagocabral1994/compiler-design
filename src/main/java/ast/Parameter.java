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

  @Override
  public boolean equals(Object o) {
    if (this == o)
      return true;
    if (o == null || getClass() != o.getClass())
      return false;
    Function that = (Function) o;
    return !this.getId().equals(that.getId());
  }

  @Override
  public int hashCode() {
    int result = Integer.parseInt(this.getId());
    return result;
  }
}
