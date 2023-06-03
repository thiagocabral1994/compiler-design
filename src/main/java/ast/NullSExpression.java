package main.java.ast;

import main.java.visitor.Visitable;
import main.java.visitor.Visitor;

public class NullSExpression extends SExpression implements Visitable {
  public NullSExpression(int line, int col) {
    super(line, col);
  }

  public void accept(Visitor visitor) {
    visitor.visit(this);
  }
}
