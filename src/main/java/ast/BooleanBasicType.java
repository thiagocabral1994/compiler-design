package main.java.ast;

import main.java.visitor.Visitable;
import main.java.visitor.Visitor;

public class BooleanBasicType extends BasicType implements Visitable {
  public BooleanBasicType(int line, int col) {
    super(line, col);
  }

  public String getTypeName() { return "Bool"; }

  public void accept(Visitor visitor) {
    visitor.visit(this);
  }
}
