package ast;

import java.util.List;

import visitor.Visitable;
import visitor.Visitor;

public class ReturnCommand extends Command implements Visitable {
  private List<Expression> expressions;
  private int label;

  public ReturnCommand(int line, int col, List<Expression> exps) {
    super(line, col);
    this.expressions = exps;
  }

  public List<Expression> getReturnExpressions() { return this.expressions; }
  public int getLabel() { return this.label; }
  public void setLabel(int label) { this.label = label; }

  public void accept(Visitor visitor) {
    visitor.visit(this);
  }
}
