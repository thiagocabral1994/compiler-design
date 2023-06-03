package ast;

public abstract class BasicType extends Node {
  public BasicType(int line, int col) {
    super(line, col);
  }

  public abstract String getTypeName();
}
