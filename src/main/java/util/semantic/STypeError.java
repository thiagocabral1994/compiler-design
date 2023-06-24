package util.semantic;

public class STypeError extends SemanticType {
  private static STypeError type = new STypeError();
  
  private STypeError() {}
  
  public static STypeError create() { return type; }

  public boolean match(SemanticType other) {
    return true;
  }

  public String toString() { return "Error"; }
}
