package util.semantic;

public class STypeFloat extends SemanticType {
  private static STypeFloat type = new STypeFloat();
  
  private STypeFloat() {}
  
  public static STypeFloat create() { return type; }

  public boolean match(SemanticType other) {
    return (other instanceof STypeError) || (other instanceof STypeFloat);
  }

  public String toString() { return "Float"; }
}
