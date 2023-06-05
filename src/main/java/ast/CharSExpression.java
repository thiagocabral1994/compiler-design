package ast;

import visitor.Visitable;
import visitor.Visitor;

public class CharSExpression extends SExpression implements Visitable {
  private char value;

  public CharSExpression(int line, int col, String value) {
    super(line, col);
    String trimmedValue = value.replace("'", "");
    if (trimmedValue.equals("\\n")) {
      this.value = '\n';
    } else if (trimmedValue.equals("\\r")) {
      this.value = '\r';
    } else if (trimmedValue.equals("\\t")) {
      this.value = '\t';
    } else if (trimmedValue.equals("\\b")) {
      this.value = '\b';
    } else {
      this.value = trimmedValue.charAt(0);
    }
  }

  public char getValue() { return value; }

  public void accept(Visitor visitor) {
    visitor.visit(this);
  }
}
