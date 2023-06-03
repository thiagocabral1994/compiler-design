package ast;

import java.util.List;

import visitor.Visitable;
import visitor.Visitor;

public class CallCommand extends Command implements Visitable {
  private String id;
  private List<Expression> expressions;
  private List<LValue> returnLValues;

  public CallCommand(int line, int col, String id, List<Expression> exps, List<LValue> lvalues) {
    super(line, col);
    this.id = id;
    this.expressions = exps;
    this.returnLValues = lvalues;
  }

  public String getId() { return this.id; }
  public List<Expression> getExpressions() { return this.expressions; }
  public List<LValue> getReturnLValues() { return this.returnLValues; }

  public void accept(Visitor visitor) {
    visitor.visit(this);
  }
}
