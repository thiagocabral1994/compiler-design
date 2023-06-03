package main.java.visitor;

public interface Visitable {
  public void accept(Visitor visitor);
}
