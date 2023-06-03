package ast;

import java.util.List;

import visitor.Visitable;
import visitor.Visitor;

public class ListCommand extends Command implements Visitable {
  private List<Command> commands;

  public ListCommand(int line, int col, List<Command> cmds) {
    super(line, col);
    this.commands = cmds;
  }

  public List<Command> getCommands() { return this.commands; }

  public void accept(Visitor visitor) {
    visitor.visit(this);
  }
}
