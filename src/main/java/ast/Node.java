package main.java.ast;

public abstract class Node {
  private int line, col;

  public Node(int line, int col) {
    this.line = line;
    this.col = col;
  }

  public int getLine() { return this.line;}
  public int getCol() { return this.col;}
}