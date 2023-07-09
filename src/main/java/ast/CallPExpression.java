package ast;

import java.util.List;

import visitor.Visitable;
import visitor.Visitor;

public class CallPExpression extends PExpression implements Visitable {
  private String id;
  private List<Expression> paramExps;
  private Expression bracketExp;
  private int index;

  public CallPExpression(int line, int col, String id, List<Expression> paramsExps, Expression bracketExp) {
    super(line, col);
    this.id = id;
    this.paramExps = paramsExps;
    this.bracketExp = bracketExp;
  }

  public List<Expression> getParamExpressions() { return this.paramExps; }
  public Expression getBracketExpression() { return this.bracketExp; }
  public String getID() { return this.id; }
  public int getIndex() { return this.index; }
  public void setIndex(int index) { this.index = index; }

  public void accept(Visitor visitor) {
    visitor.visit(this);
  }
}
