package util.semantic;

public class STypeChar extends SemanticType {
  private static STypeChar type = new STypeChar();
  
  private STypeChar() {}
  
  public static STypeChar create() { return type; }

  public boolean match(SemanticType other) {
    return (other instanceof STypeError) || (other instanceof STypeChar);
  }

  public String toString() { return "Char"; }
}
