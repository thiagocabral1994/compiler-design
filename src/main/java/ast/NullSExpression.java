package ast;

import visitor.Visitable;
import visitor.Visitor;

public class NullSExpression extends SExpression implements Visitable {
  public NullSExpression(int line, int col) {
    super(line, col);
  }

  public void accept(Visitor visitor) {
    visitor.visit(this);
  }
}
