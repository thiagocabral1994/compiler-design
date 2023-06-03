package ast;

import visitor.Visitor;

public class ArrayType extends Type {
  public ArrayType(int line, int col, String typeName) {
    super(line, col, typeName);
  }

  public void accept(Visitor visitor) {
    visitor.visit(this);
  }
}
