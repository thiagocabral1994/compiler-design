package ast;

import visitor.Visitable;
import visitor.Visitor;

public class CharacterBasicType extends BasicType implements Visitable {
  public CharacterBasicType(int line, int col) {
    super(line, col);
  }

  public String getTypeName() { return "Char"; }

  public void accept(Visitor visitor) {
    visitor.visit(this);
  }
}
