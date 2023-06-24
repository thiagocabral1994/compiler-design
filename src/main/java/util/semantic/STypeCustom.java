package util.semantic;

public class STypeCustom extends SemanticType {
  private String name;

  private STypeCustom(String name) {
    this.name = name;
  }
  
  public static STypeCustom create(String name) { 
    return new STypeCustom(name); 
  }

  public boolean match(SemanticType other) {
    if (other instanceof STypeError) {
      return true;
    }

    if (other instanceof STypeCustom) {
      STypeCustom customOther = (STypeCustom) other;
      return customOther.toString().equals(this.toString());
    }

    return false;
  }

  public String toString() { return this.name; }
}
