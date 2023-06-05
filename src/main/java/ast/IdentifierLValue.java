package ast;

import visitor.Visitable;
import visitor.Visitor;

public class IdentifierLValue extends LValue implements Visitable {
  private String id;

  public IdentifierLValue(int line, int col, String id) {
    super(line, col);
    this.id = id;
  }

  public String getID() { return this.id; }
  public String getHeadId() { return this.getID(); }

  public void accept(Visitor visitor) {
    visitor.visit(this);
  }
}
