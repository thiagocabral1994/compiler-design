package ast;

public abstract class Type extends Node {
  private String typeName;

  public Type(int line, int col, String typeName) {
    super(line, col);
    this.typeName = typeName;
  }

  public String getTypeName() { return this.typeName; }
}
