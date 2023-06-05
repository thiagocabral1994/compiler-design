package ast;

import visitor.Visitable;
import visitor.Visitor;

public class ArrayLValue extends LValue implements Visitable {
  private LValue lvalue;
  private Expression exp;

  public ArrayLValue(int line, int col, LValue lvalue, Expression exp) {
    super(line, col);
    this.lvalue = lvalue;
    this.exp = exp;
  }

  public LValue getLValue() { return this.lvalue; };
  public Expression getExpression() { return this.exp; };
  public String getHeadId() { return this.lvalue.getHeadId(); };

  public void accept(Visitor visitor) {
    visitor.visit(this);
  }
}
