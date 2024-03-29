package ast;

import visitor.Visitable;
import visitor.Visitor;

public class NewPExpression extends PExpression implements Visitable {
  private Type type;
  private Expression exp;
  private int index;

  public NewPExpression(int line, int col, Type type, Expression exp) {
    super(line, col);
    this.type = type;
    this.exp = exp;
  }

  public Type getType() { return this.type; }
  public Expression getExpression() { return this.exp; }
  public int getIndex() { return this.index; }
  public void setIndex(int index) { this.index = index; }

  public void accept(Visitor visitor) {
    visitor.visit(this);
  }
}
