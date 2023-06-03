package main.java.ast;

import main.java.visitor.Visitable;
import main.java.visitor.Visitor;

public class CharacterBasicType extends BasicType implements Visitable {
  public CharacterBasicType(int line, int col) {
    super(line, col);
  }

  public String getTypeName() { return "Char"; }

  public void accept(Visitor visitor) {
    visitor.visit(this);
  }
}
