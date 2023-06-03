package ast;

import visitor.Visitable;
import visitor.Visitor;

public class IntegerSExpression extends SExpression implements Visitable {
  private int value;

  public IntegerSExpression(int line, int col, String value) {
    super(line, col);
    this.value = Integer.parseInt(value);
  }

  public int getValue() { return value; }

  public void accept(Visitor visitor) {
    visitor.visit(this);
  }
}
