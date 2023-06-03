package ast;

import java.util.List;

import visitor.Visitable;
import visitor.Visitor;

public class Data extends Node implements Visitable {
  private String id;
  private List<Parameter> declarations;

  public Data(int line, int col, String id, List<Parameter> declarations) {
    super(line, col);
    this.id = id;
    this.declarations = declarations;
  }

  public String getId() { return this.id; }
  public List<Parameter> getDeclarations() {return this.declarations; };

  public void accept(Visitor visitor) {
    visitor.visit(this);
  }
}
