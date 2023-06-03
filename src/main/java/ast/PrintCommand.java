package main.java.ast;

import main.java.visitor.Visitable;
import main.java.visitor.Visitor;

public class PrintCommand extends Command implements Visitable {
  private Expression exp;

  public PrintCommand(int line, int col, Expression exp) {
    super(line, col);
    this.exp = exp;
  }

  public Expression getExpression() { return this.exp; }

  public void accept(Visitor visitor) {
    visitor.visit(this);
  }
}
