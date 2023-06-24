package util.semantic;

public class STypeInt extends SemanticType {
  private static STypeInt type = new STypeInt();
  
  private STypeInt() {}
  
  public static STypeInt create() { return type; }

  public boolean match(SemanticType other) {
    return (other instanceof STypeError) || (other instanceof STypeInt);
  }

  public String toString() { return "Int"; }
}
