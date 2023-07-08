package util;

public class Pair<Left, Right> {
  private Left left;
  private Right right;

  public Pair(Left left, Right right) {
    this.left = left;
    this.right = right;
  }

  public Left getLeft() { return this.left; }
  public Right getRight() { return this.right; }
}
