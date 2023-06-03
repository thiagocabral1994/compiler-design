package main.java.ast;

import main.java.visitor.Visitable;
import main.java.visitor.Visitor;

public class IdentifierLValue extends LValue implements Visitable {
  private String id;

  public IdentifierLValue(int line, int col, String id) {
    super(line, col);
    this.id = id;
  }

  public String getID() { return this.id; }

  public void accept(Visitor visitor) {
    visitor.visit(this);
  }
}
