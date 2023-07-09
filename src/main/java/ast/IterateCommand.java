package ast;

import visitor.Visitable;
import visitor.Visitor;

public class IterateCommand extends Command implements Visitable {
  private Expression exp;
  private Command cmd;

  private int label;


  public IterateCommand(int line, int col, Expression exp, Command cmd) {
    super(line, col);
    this.exp = exp;
    this.cmd = cmd;
  }

  public Expression getExpression() { return this.exp; }
  public Command getCommand() { return this.cmd; }

  public int getLabel() { return this.label;}

  public void setLabel(int label) { this.label = label;}

  public void accept(Visitor visitor) {
    visitor.visit(this);
  }
}
