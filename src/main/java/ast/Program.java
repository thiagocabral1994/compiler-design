package ast;

import java.util.ArrayList;
import java.util.List;

import visitor.Visitable;
import visitor.Visitor;

public class Program extends Node implements Visitable {
  private List<Data> datas;
  private List<Function> functions;

  public Program(int line, int col) {
    super(line, col);
    this.datas = new ArrayList<Data>();
    this.functions = new ArrayList<Function>();
  }

  public boolean pushData(Data data) { return this.datas.add(data); }
  public boolean pushFunction(Function function) { return this.functions.add(function); }

  public List<Data> getDatas() {return this.datas; };
  public List<Function> getFunctions() {return this.functions; };

  public void accept(Visitor visitor) {
    visitor.visit(this);
  }
}
