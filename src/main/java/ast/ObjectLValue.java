package ast;

import visitor.Visitable;
import visitor.Visitor;

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
  public String getHeadId() { return this.lvalue.getHeadId(); };

  public void accept(Visitor visitor) {
    visitor.visit(this);
  }
}
