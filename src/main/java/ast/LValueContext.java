package ast;

import visitor.Visitable;
import visitor.Visitor;

public class LValueContext extends PExpression implements Visitable {
  private LValue lvalue;
  public LValueContext(LValue lvalue) {
    super(lvalue.getLine(), lvalue.getCol());
    this.lvalue = lvalue;
  }

  public LValue getLValue() {
    return this.lvalue;
  }

  public void accept(Visitor visitor) {
    visitor.visit(this);
  }
}
