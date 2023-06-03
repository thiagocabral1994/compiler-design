package ast;

import visitor.Visitable;
import visitor.Visitor;

public class ReadCommand extends Command implements Visitable {
  private LValue lvalue;

  public ReadCommand(int line, int col, LValue lvalue) {
    super(line, col);
    this.lvalue = lvalue;
  }

  public LValue getLValue() { return this.lvalue; }

  public void accept(Visitor visitor) {
    visitor.visit(this);
  }
}
