package ast;

import visitor.Visitable;
import visitor.Visitor;

public class CharSExpression extends SExpression implements Visitable {
  private char value;

  public CharSExpression(int line, int col, String value) {
    super(line, col);
    this.value = value.charAt(0);
  }

  public char getValue() { return value; }

  public void accept(Visitor visitor) {
    visitor.visit(this);
  }
}
