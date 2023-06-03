package main.java.ast;

import main.java.visitor.Visitable;
import main.java.visitor.Visitor;

public class Type extends Node implements Visitable {
  private String typeName;
  private boolean _isArray;

  public Type(int line, int col, String typeName, boolean isArray) {
    super(line, col);
    this.typeName = typeName;
    this._isArray = isArray;
  }

  public boolean isArray() {return this._isArray; };
  public String getTypeName() { return this.typeName; }

  public void accept(Visitor visitor) {
    visitor.visit(this);
  }
}
