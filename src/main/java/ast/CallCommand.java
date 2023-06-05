package ast;

import java.util.List;

import visitor.Visitable;
import visitor.Visitor;

public class CallCommand extends Command implements Visitable {
  private String id;
  private List<Expression> expressions;
  private List<LValueContext> returnLValues;

  public CallCommand(int line, int col, String id, List<Expression> exps, List<LValueContext> lvalues) {
    super(line, col);
    this.id = id;
    this.expressions = exps;
    this.returnLValues = lvalues;
  }

  public String getId() { return this.id; }
  public List<Expression> getExpressions() { return this.expressions; }
  public List<LValueContext> getReturnLValueContexts() { return this.returnLValues; }

  public void accept(Visitor visitor) {
    visitor.visit(this);
  }
}
