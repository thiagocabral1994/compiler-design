package util.semantic;

public class LocalEnv<A> extends TypeEnv<A> {
  private String id;
  private SemanticType type;

  public LocalEnv(String id, SemanticType type) {
    this.type = type;
    this.id = id;
  }

  public String getFunctionID() {
    return this.id;
  }

  public SemanticType getFunctionType() {
    return this.type;
  }

  public String toString() {
    String string = "--------------- (" + this.id + "," + this.type.toString() + ") ---------------\n";
    string += super.toString();
    return string;
  }
}
