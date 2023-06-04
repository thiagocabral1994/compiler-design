package ast;

import visitor.Visitable;
import visitor.Visitor;

public class CustomBasicType extends Type implements Visitable {
  public CustomBasicType(int line, int col, String name) {
    super(line, col, name);
  }

  public void accept(Visitor visitor) {
    visitor.visit(this);
  }
}
