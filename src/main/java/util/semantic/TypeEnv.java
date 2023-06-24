package util.semantic;

import java.util.Map;
import java.util.TreeMap;

public class TypeEnv<A> {
  private Map<String, A> typeEnv;

  public TypeEnv() {
    this.typeEnv = new TreeMap<String, A>();
  }

  public void set(String id, A type) {
    typeEnv.put(id, type);
  }

  public A get(String id) {
    return typeEnv.get(id);
  }

  public boolean contains(String id) {
    return typeEnv.containsKey(id);
  }

  public void printTable() {
    System.out.println(this.toString());
  }

  public String toString() {
    String string = "";
    Object[] obj = typeEnv.keySet().toArray();
    for(int i = 0; i < obj.length; i++) {
      string += ((String) obj[i]) + " : " + typeEnv.get(obj[i]).toString() + "\n";
    }

    return string;
  }
  
}
