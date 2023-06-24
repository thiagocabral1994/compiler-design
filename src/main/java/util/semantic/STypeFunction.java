package util.semantic;

import java.util.List;

public class STypeFunction extends SemanticType {
  private List<SemanticType> params;
  private List<SemanticType> returnTypes;

  private STypeFunction(List<SemanticType> params, List<SemanticType> returnTypes) {
    this.params = params;
    this.returnTypes = returnTypes;
  }
  
  public static STypeFunction create(List<SemanticType> params, List<SemanticType> returnTypes) { 
    return new STypeFunction(params, returnTypes); 
  }

  public boolean match(SemanticType other) {
    if (other instanceof STypeError) {
      return true;
    }

    if (!(other instanceof STypeFunction)) {
      return false;
    }

    STypeFunction otherFunction = (STypeFunction) other;
    
    List<SemanticType> otherReturnTypes = otherFunction.getReturnTypes();
    if (otherReturnTypes.size() != this.returnTypes.size()) {
      return false;
    }

    for (int i = 0; i < this.returnTypes.size(); i++) {
      if (!this.returnTypes.get(i).match(otherReturnTypes.get(i))) {
        return false;
      }
    }


    List<SemanticType> otherParams = otherFunction.getParams();
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

  public List<SemanticType> getParams() { return this.params; }
  public List<SemanticType> getReturnTypes() { return this.returnTypes; }

  public String toString() {
    String string = "";

    if (this.params.size() > 0) {
      string = this.params.get(0).toString();
      for (int i = 1; i < this.params.size(); i++) {
        string = "->" + this.params.get(i).toString();
      }
    }

    if (this.returnTypes.size() > 0) {
      string = this.params.size() > 0 ? "->" + this.returnTypes.get(0).toString() : this.returnTypes.get(0).toString();
      for (int i = 1; i < this.returnTypes.size(); i++) {
        string = "->" + this.returnTypes.get(i).toString();
      }
    }

    return string;
  }
}
