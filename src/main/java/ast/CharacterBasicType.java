package ast;

import visitor.Visitable;
import visitor.Visitor;

public class CharacterBasicType extends Type implements Visitable {
  public CharacterBasicType(int line, int col) {
    super(line, col, "Char");
  }

  public void accept(Visitor visitor) {
    visitor.visit(this);
  }
}
