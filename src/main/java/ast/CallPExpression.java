package main.java.ast;

import java.util.List;

import main.java.visitor.Visitable;
import main.java.visitor.Visitor;

public class CallPExpression extends PExpression implements Visitable {
  private String id;
  private List<Expression> paramExps;
  private Expression bracketExp;

  public CallPExpression(int line, int col, String id) {
    super(line, col);
    this.id = id;
  }

  public void addParamExpressions(List<Expression> exps) { this.paramExps = exps;}
  public void addBracketExp(Expression exp) { this.bracketExp = exp; }

  public List<Expression> getParamExpressions() { return this.paramExps; }
  public Expression getBracketExpression() { return this.bracketExp; }
  public String getID() { return this.id; }

  public void accept(Visitor visitor) {
    visitor.visit(this);
  }
}
