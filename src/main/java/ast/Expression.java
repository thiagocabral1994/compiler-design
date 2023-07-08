package ast;

import util.semantic.SemanticType;

public abstract class Expression extends Node {
  private SemanticType sType;

  public void setSemanticType(SemanticType type) { this.sType = type; }
  public SemanticType getSemanticType() { return this.sType; }

  public Expression(int line, int col) {
    super(line, col);
  }
}
