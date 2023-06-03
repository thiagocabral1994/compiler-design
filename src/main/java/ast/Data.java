package ast;

import java.util.ArrayList;
import java.util.List;

import visitor.Visitable;
import visitor.Visitor;

public class Data extends Node implements Visitable {
  private String id;
  private List<Parameter> declarations;

  public Data(int line, int col, String id) {
    super(line, col);
    this.id = id;
    this.declarations = new ArrayList<Parameter>();
  }

  public boolean pushDeclaration(Parameter declaration) { return this.declarations.add(declaration); }

  public String getId() { return this.id; }
  public List<Parameter> getDeclarations() {return this.declarations; };

  public void accept(Visitor visitor) {
    visitor.visit(this);
  }
}
