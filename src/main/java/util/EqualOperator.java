package util;

public class EqualOperator {
  public static Boolean execute(Object left, Object right) {
    if(left == null || right == null) {
      return Boolean.valueOf(false);
    }

    if(left instanceof Integer && right instanceof Integer) {
      return EqualOperator.execute((Integer) left, (Integer) right);
    }

    if(left instanceof Character && right instanceof Character) {
      return EqualOperator.execute((Character) left, (Character) right);
    }

    if(left instanceof Float && right instanceof Float) {
      return EqualOperator.execute((Float) left, (Float) right);
    }

    if(left instanceof Boolean && right instanceof Boolean) {
      return EqualOperator.execute((Boolean) left, (Boolean) right);
    }

    throw new RuntimeException("Igualdade inválida: " + left.toString() + ", " + right.toString());
  }

  public static Boolean execute(Integer left, Integer right) {
    return Boolean.valueOf(left.intValue() == right.intValue());
  }

  public static Boolean execute(Character left, Character right) {
    return Boolean.valueOf(left.charValue() == right.charValue());
  }

  public static Boolean execute(Float left, Float right) {
    return Boolean.valueOf(left.floatValue() == right.floatValue());
  }

  public static Boolean execute(Boolean left, Boolean right) {
    return Boolean.valueOf(left.booleanValue() == right.booleanValue());
  }
  

}