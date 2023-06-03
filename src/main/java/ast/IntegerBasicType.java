package ast;

import visitor.Visitable;
import visitor.Visitor;

public class IntegerBasicType extends BasicType implements Visitable {
  public IntegerBasicType(int line, int col) {
    super(line, col);
  }

  public String getTypeName() { return "Int"; }

  public void accept(Visitor visitor) {
    visitor.visit(this);
  }
}
