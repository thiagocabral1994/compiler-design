package main.java.ast;

import java.util.ArrayList;
import java.util.List;

import main.java.visitor.Visitable;
import main.java.visitor.Visitor;

public class ReturnCommand extends Command implements Visitable {
  private List<Expression> expressions;

  public ReturnCommand(int line, int col, Expression exp) {
    super(line, col);
    this.expressions = new ArrayList<Expression>();
    this.pushReturnExpression(exp);
  }

  public void pushReturnExpression(Expression exp) { this.expressions.add(exp); }

  public List<Expression> getReturnExpressions() { return this.expressions; }

  public void accept(Visitor visitor) {
    visitor.visit(this);
  }
}
