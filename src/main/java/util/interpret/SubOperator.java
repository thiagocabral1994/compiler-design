package util.interpret;

public class SubOperator {
  public static Object execute(Object left, Object right) {
    
    if(left instanceof Integer && right instanceof Integer) {
      return SubOperator.execute((Integer) left, (Integer) right);
    }
    if(left instanceof Integer && right instanceof Character) {
      return SubOperator.execute((Integer) left, (Character) right);
    }
    if(left instanceof Integer && right instanceof Float) {
      return SubOperator.execute((Integer) left, (Float) right);
    }

    if(left instanceof Character && right instanceof Character) {
      return SubOperator.execute((Character) left, (Character) right);
    }
    if(left instanceof Character && right instanceof Integer) {
      return SubOperator.execute((Character) left, (Integer) right);
    }
    if(left instanceof Character && right instanceof Float) {
      return SubOperator.execute((Character) left, (Float) right);
    }

    if(left instanceof Float && right instanceof Float) {
      return SubOperator.execute((Float) left, (Float) right);
    }
    if(left instanceof Float && right instanceof Integer) {
      return SubOperator.execute((Float) left, (Integer) right);
    }
    if(left instanceof Float && right instanceof Character) {
      return SubOperator.execute((Float) left, (Character) right);
    }

    throw new RuntimeException("Subtração inválida: " + left.toString() + ", " + right.toString());
  }

  public static Integer execute(Integer left, Integer right) {
    return Integer.valueOf(left.intValue() - right.intValue());
  }
  public static Integer execute(Integer left, Character right) {
    return Integer.valueOf(left.intValue() - right.charValue());
  }
  public static Float execute(Integer left, Float right) {
    return Float.valueOf(left.intValue() - right.floatValue());
  }
  
  public static Integer execute(Character left, Character right) {
    return Integer.valueOf(left.charValue() - right.charValue());
  }
  public static Integer execute(Character left, Integer right) {
    return Integer.valueOf(left.charValue() - right.intValue());
  }
  public static Float execute(Character left, Float right) {
    return Float.valueOf(left.charValue() - right.floatValue());
  }

  public static Float execute(Float left, Float right) {
    return Float.valueOf(left.floatValue() - right.floatValue());
  }
  public static Float execute(Float left, Integer right) {
    return Float.valueOf(left.floatValue() - right.intValue());
  }
  public static Float execute(Float left, Character right) {
    return Float.valueOf(left.floatValue() - right.charValue());
  }
}