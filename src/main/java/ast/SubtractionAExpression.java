package ast;

import visitor.Visitable;
import visitor.Visitor;

public class SubtractionAExpression extends AExpression implements Visitable {
  private AExpression left;
  private MExpression right;

  public SubtractionAExpression(int line, int col, AExpression left, MExpression right) {
    super(line, col);
    this.left = left;
    this.right = right;
  }

  public AExpression getLeft() { return this.left; }
  public MExpression getRight() { return this.right; }

  public void accept(Visitor visitor) {
    visitor.visit(this);
  }
}
