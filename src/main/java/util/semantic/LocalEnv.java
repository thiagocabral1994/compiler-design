package util.semantic;

public class LocalEnv<A> extends TypeEnv<A> {
  private String id;
  private A type;

  public LocalEnv(String id, A type) {
    this.type = type;
    this.id = id;
  }

  public String getFunctionID() {
    return this.id;
  }

  public A getFunctionType() {
    return this.type;
  }

  public String toString() {
    String string = "--------------- (" + this.id + "," + this.type.toString() + ") ---------------\n";
    string += super.toString();
    return string;
  }
}
