package main.java.ast;

import main.java.visitor.Visitable;
import main.java.visitor.Visitor;

public class IfElseCommand extends Command implements Visitable {
  private Expression exp;
  private Command ifCmd;
  private Command elseCmd;

  public IfElseCommand(int line, int col, Expression exp, Command ifCmd, Command elseCmd) {
    super(line, col);
    this.exp = exp;
    this.ifCmd = ifCmd;
    this.elseCmd = elseCmd;
  }

  public Expression getExpression() { return this.exp; }
  public Command getIfCommand() { return this.ifCmd; }
  public Command getElseCommand() { return this.elseCmd; }

  public void accept(Visitor visitor) {
    visitor.visit(this);
  }
}
