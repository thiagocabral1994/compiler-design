package ast;

import visitor.Visitable;
import visitor.Visitor;

public class AndExpression extends Expression implements Visitable {
  private Expression left;
  private Expression right;

  public AndExpression(int line, int col, Expression left, Expression right) {
    super(line, col);
    this.left = left;
    this.right = right;
  }

  public Expression getLeft() { return this.left; }
  public Expression getRight() { return this.right; }

  public void accept(Visitor visitor) {
    visitor.visit(this);
  }
}
