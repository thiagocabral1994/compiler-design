package util.semantic;

public class STypeArray extends SemanticType {
  private SemanticType type;

  private STypeArray(SemanticType type) {
    this.type = type;
  }
  
  public static STypeArray create(SemanticType type) { 
    return new STypeArray(type); 
  }

  public boolean match(SemanticType other) {
    if (other instanceof STypeError) {
      return true;
    }

    if (other instanceof STypeArray) {
      STypeArray otherArray = (STypeArray) other;
      return otherArray.getType().match(this.type);
    }

    return false;
  }

  public SemanticType getType() { return this.type; }

  public String toString() { return this.type.toString() + "[]"; }
}
