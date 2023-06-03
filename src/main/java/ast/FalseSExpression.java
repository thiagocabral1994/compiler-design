package ast;

import visitor.Visitable;
import visitor.Visitor;

public class FalseSExpression extends SExpression implements Visitable {
  public FalseSExpression(int line, int col) {
    super(line, col);
  }

  public boolean getValue() { return false; }

  public void accept(Visitor visitor) {
    visitor.visit(this);
  }
}
