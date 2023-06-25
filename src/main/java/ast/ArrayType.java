package ast;

import visitor.Visitor;

public class ArrayType extends Type {
  private Type type;

  public ArrayType(int line, int col, Type type) {
    super(line, col, type.getTypeName());
    this.type = type;
  }

  public Type getType() {
    return this.type;
  }

  public void accept(Visitor visitor) {
    visitor.visit(this);
  }
}
