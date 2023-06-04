package ast;

import visitor.Visitable;
import visitor.Visitor;

public class BooleanBasicType extends Type implements Visitable {
  public BooleanBasicType(int line, int col) {
    super(line, col, "Bool");
  }

  public void accept(Visitor visitor) {
    visitor.visit(this);
  }
}
