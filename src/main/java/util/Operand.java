package util;

public class Operand {
  private Object value;

  public Operand(Object value) {
    this.value = value;
  }

  public Object getValue() { return value; }

  public void setValue(Object value) { this.value = value; }

  @Override
  public String toString() {
    if (this.getValue() == null) {
      return null;
    }
    return this.getValue().toString();
  }
}
