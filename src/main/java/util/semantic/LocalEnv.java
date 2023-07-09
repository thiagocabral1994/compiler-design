package util.semantic;

public class LocalEnv<A> extends TypeEnv<A> {
  private String id;
  private STypeFunction type;
  private int maxStackSize = 0;

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

  public int getMaxStackSize() {
    return this.maxStackSize;
  }

  public void assertMaxStackSize(int size) {
    this.maxStackSize = size > this.maxStackSize ? size : this.maxStackSize;
  }

  public String toString() {
    String string = "--------------- (" + this.id + "," + this.type.toString() + ") ---------------\n";
    string += super.toString();
    return string;
  }
}
