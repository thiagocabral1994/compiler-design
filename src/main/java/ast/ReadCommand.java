package ast;

import visitor.Visitable;
import visitor.Visitor;

public class ReadCommand extends Command implements Visitable {
  private LValueContext lvalue;

  private int label;

  public ReadCommand(int line, int col, LValueContext lvalue) {
    super(line, col);
    this.lvalue = lvalue;
  }

  public LValueContext getLValue() { return this.lvalue;}

  public int getLabel() { return this.label;}

  public void setLabel(int label) { this.label = label;}

  public void accept(Visitor visitor) {
    visitor.visit(this);
  }
}
