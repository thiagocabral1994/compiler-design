package ast;

import visitor.Visitable;
import visitor.Visitor;

public class IntegerBasicType extends Type implements Visitable {
  public IntegerBasicType(int line, int col) {
    super(line, col, "Int");
  }

  public void accept(Visitor visitor) {
    visitor.visit(this);
  }
}
