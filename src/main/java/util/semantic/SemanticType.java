package util.semantic;

public abstract class SemanticType {
  public abstract boolean match(SemanticType other);
  public abstract String toString();
}
