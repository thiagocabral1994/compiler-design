package ast;

import visitor.Visitable;
import visitor.Visitor;

public class Type extends Node implements Visitable {
  private String typeName;

  public Type(int line, int col, String typeName) {
    super(line, col);
    this.typeName = typeName;
  }

  public String getTypeName() { return this.typeName; }

  public void accept(Visitor visitor) {
    visitor.visit(this);
  }
}
