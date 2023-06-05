package util;

import java.util.HashMap;

public class LValueObject {
  private Object value;
  private HashMap<String, LValueObject> parent;

  public LValueObject(Object value, HashMap<String, LValueObject> parent) {
    this.value = value;
    this.parent = parent;
  }

  public HashMap<String, LValueObject> getParent() { return parent; }
  public Object getValue() { return value; }

  public void setValue(Object value) { this.value = value; }
  public void setParent(HashMap<String, LValueObject> parent) { this.parent = parent; }
}
