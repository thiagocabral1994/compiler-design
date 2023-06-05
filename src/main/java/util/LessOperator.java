package util;

public class LessOperator {
  public static Object execute(Object left, Object right) {
    if(left == null || right == null) {
      return Boolean.valueOf(false);
    }

    if(left instanceof Integer && right instanceof Integer) {
      return LessOperator.execute((Integer) left, (Integer) right);
    }
    if(left instanceof Integer && right instanceof Character) {
      return LessOperator.execute((Integer) left, (Character) right);
    }
    if(left instanceof Integer && right instanceof Float) {
      return LessOperator.execute((Integer) left, (Float) right);
    }

    if(left instanceof Character && right instanceof Character) {
      return LessOperator.execute((Character) left, (Character) right);
    }
    if(left instanceof Character && right instanceof Integer) {
      return LessOperator.execute((Character) left, (Integer) right);
    }
    if(left instanceof Character && right instanceof Float) {
      return LessOperator.execute((Character) left, (Float) right);
    }

    if(left instanceof Float && right instanceof Float) {
      return LessOperator.execute((Float) left, (Float) right);
    }
    if(left instanceof Float && right instanceof Integer) {
      return LessOperator.execute((Float) left, (Integer) right);
    }
    if(left instanceof Float && right instanceof Character) {
      return LessOperator.execute((Float) left, (Character) right);
    }

    throw new RuntimeException("`<` lógico inválido: " + left.toString() + ", " + right.toString());
  }

  public static Boolean execute(Integer left, Integer right) {
    return Boolean.valueOf(left.intValue() < right.intValue());
  }
  public static Boolean execute(Integer left, Character right) {
    return Boolean.valueOf(left.intValue() < right.charValue());
  }
  public static Boolean execute(Integer left, Float right) {
    return Boolean.valueOf(left.intValue() < right.floatValue());
  }
  
  public static Boolean execute(Character left, Character right) {
    return Boolean.valueOf(left.charValue() < right.charValue());
  }
  public static Boolean execute(Character left, Integer right) {
    return Boolean.valueOf(left.charValue() < right.intValue());
  }
  public static Boolean execute(Character left, Float right) {
    return Boolean.valueOf(left.charValue() < right.floatValue());
  }

  public static Boolean execute(Float left, Float right) {
    return Boolean.valueOf(left.floatValue() < right.floatValue());
  }
  public static Boolean execute(Float left, Integer right) {
    return Boolean.valueOf(left.floatValue() < right.intValue());
  }
  public static Boolean execute(Float left, Character right) {
    return Boolean.valueOf(left.floatValue() < right.charValue());
  }
}