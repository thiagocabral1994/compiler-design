package util.semantic;

public class STypeBool extends SemanticType {
  private static STypeBool type = new STypeBool();
  
  private STypeBool() {}
  
  public static STypeBool create() { return type; }

  public boolean match(SemanticType other) {
    return (other instanceof STypeError) || (other instanceof STypeBool);
  }

  public String toString() { return "Bool"; }
}
