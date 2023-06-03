package main.java.ast;

import main.java.visitor.Visitable;
import main.java.visitor.Visitor;

public class NegationSExpression extends SExpression implements Visitable {
  private SExpression sexp;

  public NegationSExpression(int line, int col, SExpression sexp) {
    super(line, col);
    this.sexp = sexp;
  }

  public SExpression getSExpression() { return this.sexp; }

  public void accept(Visitor visitor) {
    visitor.visit(this);
  }
}
