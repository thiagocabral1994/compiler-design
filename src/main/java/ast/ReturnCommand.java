package ast;

import java.util.List;

import visitor.Visitable;
import visitor.Visitor;

public class ReturnCommand extends Command implements Visitable {
  private List<Expression> expressions;

  public ReturnCommand(int line, int col, List<Expression> exps) {
    super(line, col);
    this.expressions = exps;
  }

  public List<Expression> getReturnExpressions() { return this.expressions; }

  public void accept(Visitor visitor) {
    visitor.visit(this);
  }
}
