package ast;

import visitor.Visitable;
import visitor.Visitor;

public class TrueSExpression extends SExpression implements Visitable {
  public TrueSExpression(int line, int col) {
    super(line, col);
  }

  public boolean getValue() { return true; }

  public void accept(Visitor visitor) {
    visitor.visit(this);
  }
}
