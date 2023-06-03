package main.java.ast;

import main.java.visitor.Visitable;
import main.java.visitor.Visitor;

public class ObjectLValue extends LValue implements Visitable {
  private LValue lvalue;
  private String paramId;

  public ObjectLValue(int line, int col, LValue lvalue, String id) {
    super(line, col);
    this.lvalue = lvalue;
    this.paramId = id;
  }

  public LValue getLValue() { return this.lvalue; };
  public String getParamID() { return this.paramId; };

  public void accept(Visitor visitor) {
    visitor.visit(this);
  }
}
