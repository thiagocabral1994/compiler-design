package ast;

import visitor.Visitable;
import visitor.Visitor;

public class AssignmentCommand extends Command implements Visitable {
  private LValueContext ctx;
  private Expression exp;

  public AssignmentCommand(int line, int col, LValueContext lvalueCtx, Expression exp) {
    super(line, col);
    this.ctx = lvalueCtx;
    this.exp = exp;
  }

  public Expression getExpression() { return this.exp; }
  public LValueContext getLValueContext() { return this.ctx; }

  public void accept(Visitor visitor) {
    visitor.visit(this);
  }
}
