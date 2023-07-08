package util.semantic;

public class LocalEnv<A> extends TypeEnv<A> {
  private String id;
  private STypeFunction type;

  public LocalEnv(String id, STypeFunction type) {
    this.type = type;
    this.id = id;
  }

  public String getFunctionID() {
    return this.id;
  }

  public STypeFunction getFunctionType() {
    return this.type;
  }

  public String toString() {
    String string = "--------------- (" + this.id + "," + this.type.toString() + ") ---------------\n";
    string += super.toString();
    return string;
  }
}
