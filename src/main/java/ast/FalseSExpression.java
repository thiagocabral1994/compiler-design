package main.java.ast;

import main.java.visitor.Visitable;
import main.java.visitor.Visitor;

public class FalseSExpression extends SExpression implements Visitable {
  public FalseSExpression(int line, int col) {
    super(line, col);
  }

  public boolean getValue() { return false; }

  public void accept(Visitor visitor) {
    visitor.visit(this);
  }
}
