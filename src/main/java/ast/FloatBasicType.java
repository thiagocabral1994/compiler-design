package ast;

import visitor.Visitable;
import visitor.Visitor;

public class FloatBasicType extends BasicType implements Visitable {
  public FloatBasicType(int line, int col) {
    super(line, col);
  }

  public String getTypeName() { return "Float"; }

  public void accept(Visitor visitor) {
    visitor.visit(this);
  }
}
