package main.java.ast;

import main.java.visitor.Visitable;
import main.java.visitor.Visitor;

public class IterateCommand extends Command implements Visitable {
  private Expression exp;
  private Command cmd;

  public IterateCommand(int line, int col, Expression exp, Command cmd) {
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
