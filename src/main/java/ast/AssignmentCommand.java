package ast;

import visitor.Visitable;
import visitor.Visitor;

public class AssignmentCommand extends Command implements Visitable {
  private LValue lvalue;
  private Expression exp;

  public AssignmentCommand(int line, int col, LValue lvalue, Expression exp) {
    super(line, col);
    this.lvalue = lvalue;
    this.exp = exp;
  }

  public Expression getExpression() { return this.exp; }
  public LValue getLValue() { return this.lvalue; }

  public void accept(Visitor visitor) {
    visitor.visit(this);
  }
}
