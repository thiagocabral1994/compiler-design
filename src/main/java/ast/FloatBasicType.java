package ast;

import visitor.Visitable;
import visitor.Visitor;

public class FloatBasicType extends Type implements Visitable {
  public FloatBasicType(int line, int col) {
    super(line, col, "Float");
  }

  public void accept(Visitor visitor) {
    visitor.visit(this);
  }
}
