package ast;

import java.util.List;

import visitor.Visitable;
import visitor.Visitor;

public class Program extends Node implements Visitable {
  private List<Data> datas;
  private List<Function> functions;

  public Program(int line, int col, List<Data> datas, List<Function> functions) {
    super(line, col);
    this.datas = datas;
    this.functions = functions;
  }

  public List<Data> getDatas() {return this.datas; };
  public List<Function> getFunctions() {return this.functions; };

  public void accept(Visitor visitor) {
    visitor.visit(this);
  }
}
