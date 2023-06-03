package ast;

import visitor.Visitable;
import visitor.Visitor;

public class IfCommand extends Command implements Visitable {
  private Expression exp;
  private Command cmd;

  public IfCommand(int line, int col, Expression exp, Command cmd) {
    super(line, col);
    this.exp = exp;
    this.cmd = cmd;
  }

  public Expression getExpression() { return this.exp; }
  public Command getCommand() { return this.cmd; }

  public void accept(Visitor visitor) {
    visitor.visit(this);
  }
}
