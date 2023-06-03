package ast;

import java.util.List;
import java.util.ArrayList;

import visitor.Visitable;
import visitor.Visitor;

public class ListCommand extends Command implements Visitable {
  private List<Command> commands;

  public ListCommand(int line, int col) {
    super(line, col);
    this.commands = new ArrayList<Command>();
  }

  public void pushCommand(Command cmd) { this.commands.add(cmd); }

  public List<Command> getCommands() { return this.commands; }

  public void accept(Visitor visitor) {
    visitor.visit(this);
  }
}
