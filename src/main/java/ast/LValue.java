package main.java.ast;

public abstract class LValue extends PExpression {
  public LValue(int line, int col) {
    super(line, col);
  }
}
