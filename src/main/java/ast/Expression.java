package ast;

public abstract class Expression extends Node {
  public Expression(int line, int col) {
    super(line, col);
  }
}
