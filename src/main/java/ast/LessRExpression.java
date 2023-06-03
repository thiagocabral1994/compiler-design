package ast;

import visitor.Visitable;
import visitor.Visitor;

public class LessRExpression extends RExpression implements Visitable {
  private AExpression left;
  private AExpression right;

  public LessRExpression(int line, int col, AExpression left, AExpression right) {
    super(line, col);
    this.left = left;
    this.right = right;
  }

  public AExpression getLeft() { return this.left; }
  public AExpression getRight() { return this.right; }

  public void accept(Visitor visitor) {
    visitor.visit(this);
  }
}
