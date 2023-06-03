package ast;

import visitor.Visitable;
import visitor.Visitor;

public class CustomBasicType extends BasicType implements Visitable {
  String name;

  public CustomBasicType(int line, int col, String name) {
    super(line, col);
    this.name = name;
  }

  public String getTypeName() { return name; }

  public void accept(Visitor visitor) {
    visitor.visit(this);
  }
}
