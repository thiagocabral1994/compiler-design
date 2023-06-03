package ast;

import visitor.Visitable;
import visitor.Visitor;

public class EqualityRExpression extends RExpression implements Visitable {
  private RExpression left;
  private AExpression right;

  public EqualityRExpression(int line, int col, RExpression left, AExpression right) {
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
