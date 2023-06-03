package main.java.ast;

import main.java.visitor.Visitable;
import main.java.visitor.Visitor;

public class InequalityRExpression extends RExpression implements Visitable {
  private RExpression left;
  private AExpression right;

  public InequalityRExpression(int line, int col, RExpression left, AExpression right) {
    super(line, col);
    this.left = left;
    this.right = right;
  }

  public RExpression getLeft() { return this.left; }
  public AExpression getRight() { return this.right; }

  public void accept(Visitor visitor) {
    visitor.visit(this);
  }
}
