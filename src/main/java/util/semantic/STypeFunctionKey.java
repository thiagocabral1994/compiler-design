package util.semantic;

import java.util.ArrayList;
import java.util.List;

public class STypeFunctionKey {
  private String id;
  private List<SemanticType> params;

  private STypeFunctionKey(String id, List<SemanticType> params) {
    this.id = id;
    this.params = params;
  }
  
  public static STypeFunctionKey createEmpty(String id) { 
    return new STypeFunctionKey(id, new ArrayList<>()); 
  }

  public static STypeFunctionKey create(String id, List<SemanticType> params) { 
    return new STypeFunctionKey(id, params); 
  }

  public List<SemanticType> getParams() { return this.params; }
  public String getID() { return this.id; }

  @Override
  public boolean equals(Object that) {
    if (that instanceof STypeFunctionKey) {
      STypeFunctionKey thatFunction = (STypeFunctionKey) that;
      return this.id.equals(thatFunction.getID()) && this.matchParams(thatFunction);
    }
    return false;
  }

  @Override
  public int hashCode() {
    String string = this.id;
    for (SemanticType semanticType : this.params) {
     string += "-> " + semanticType.toString(); 
    }
    return string.hashCode();
  }

  private boolean matchParams(STypeFunctionKey other) {
    List<SemanticType> otherParams = other.getParams();
    if (otherParams.size() != this.params.size()) {
      return false;
    }

    for (int i = 0; i < this.params.size(); i++) {
      if (!this.params.get(i).match(otherParams.get(i))) {
        return false;
      }
    }

    return true;
  }

}
