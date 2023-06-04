package util;

public class NegOperator {
  public static Object execute(Object object) {
    
    if(object instanceof Integer) {
      return NegOperator.execute((Integer) object);
    }
    if(object instanceof Character) {
      return NegOperator.execute((Character) object);
    }
    if(object instanceof Float) {
      return NegOperator.execute((Float) object);
    }


    throw new RuntimeException("Subtração inválida: " + object.toString());
  }

  public static Integer execute(Integer object) {
    return Integer.valueOf(- object.intValue());
  }
  public static Integer execute(Character object) {
    return Integer.valueOf(- object.charValue());
  }
  public static Float execute(Float object) {
    return Float.valueOf(- object.floatValue());
  }

  // TODO: adicionar o resto de execuções válidas para soma.
}