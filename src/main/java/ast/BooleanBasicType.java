package ast;

import visitor.Visitable;
import visitor.Visitor;

public class BooleanBasicType extends BasicType implements Visitable {
  public BooleanBasicType(int line, int col) {
    super(line, col);
  }

  public String getTypeName() { return "Bool"; }

  public void accept(Visitor visitor) {
    visitor.visit(this);
  }
}
