package util.semantic;

public class STypeNull extends SemanticType {
  private static STypeNull type = new STypeNull();
  
  private STypeNull() {}
  
  public static STypeNull create() { return type; }

  public boolean match(SemanticType other) {
    return (other instanceof STypeError) || (other instanceof STypeNull);
  }

  public String toString() { return "null"; }
}
