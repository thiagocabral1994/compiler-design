package main.java.ast;

import main.java.visitor.Visitable;
import main.java.visitor.Visitor;

public class NewPExpression extends PExpression implements Visitable {
  private Type type;
  private Expression exp;

  public NewPExpression(int line, int col, Type type) {
    super(line, col);
    this.type = type;
  }

  public void addExpression (Expression exp) { this.exp = exp; }

  public Type getType() { return this.type; }
  public Expression getExpression() { return this.exp; }

  public void accept(Visitor visitor) {
    visitor.visit(this);
  }
}
