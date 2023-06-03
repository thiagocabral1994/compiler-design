package main.java.ast;

import main.java.visitor.Visitable;
import main.java.visitor.Visitor;

public class FloatSExpression extends SExpression implements Visitable {
  private float value;

  public FloatSExpression(int line, int col, String value) {
    super(line, col);
    this.value = Float.parseFloat(value);
  }

  public float getValue() { return value; }

  public void accept(Visitor visitor) {
    visitor.visit(this);
  }
}
