package ast;

import visitor.Visitable;
import visitor.Visitor;

public class ParenthesisPExpression extends PExpression implements Visitable {
  private Expression exp;

  public ParenthesisPExpression(int line, int col, Expression exp) {
    super(line, col);
    this.exp = exp;
  }

  public Expression getExpression() { return this.exp; }

  public void accept(Visitor visitor) {
    visitor.visit(this);
  }
}
