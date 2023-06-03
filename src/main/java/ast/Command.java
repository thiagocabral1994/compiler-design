package ast;

public abstract class Command extends Node {
  public Command(int line, int col) {
    super(line, col);
  }
}
