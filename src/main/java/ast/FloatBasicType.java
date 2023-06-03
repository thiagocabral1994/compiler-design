package main.java.ast;

import main.java.visitor.Visitable;
import main.java.visitor.Visitor;

public class FloatBasicType extends BasicType implements Visitable {
  public FloatBasicType(int line, int col) {
    super(line, col);
  }

  public String getTypeName() { return "Float"; }

  public void accept(Visitor visitor) {
    visitor.visit(this);
  }
}
