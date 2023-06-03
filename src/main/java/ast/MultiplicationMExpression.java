package main.java.ast;

import main.java.visitor.Visitable;
import main.java.visitor.Visitor;

public class MultiplicationMExpression extends MExpression implements Visitable {
  private MExpression left;
  private SExpression right;

  public MultiplicationMExpression(int line, int col, MExpression left, SExpression right) {
    super(line, col);
    this.left = left;
    this.right = right;
  }

  public MExpression getLeft() { return this.left; }
  public SExpression getRight() { return this.right; }

  public void accept(Visitor visitor) {
    visitor.visit(this);
  }
}
