package ast;

import java.util.List;

import visitor.Visitable;
import visitor.Visitor;

public class CallCommand extends Command implements Visitable {
  private String id;
  private List<Expression> expressions;
  private List<LValue> returnLValues;

  public CallCommand(int line, int col, String id) {
    super(line, col);
    this.id = id;
  }

  public void pushExpressions(List<Expression> expressions) { this.expressions = expressions; }
  public void pushReturnValue(LValue lvalue) { this.returnLValues.add(lvalue); }

  public String getId() { return this.id; }
  public List<Expression> getExpressions() { return this.expressions; }
  public List<LValue> getReturnLValues() { return this.returnLValues; }

  public void accept(Visitor visitor) {
    visitor.visit(this);
  }
}
