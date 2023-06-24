package util.interpret;

public class AndOperator {
  public static Object execute(Object left, Object right) {
    if(left == null || right == null) {
      return Boolean.valueOf(false);
    }

    if(left instanceof Boolean && right instanceof Boolean) {
      return AndOperator.execute((Boolean) left, (Boolean) right);
    }
    throw new RuntimeException("`E` lógico inválido: " + left.toString() + ", " + right.toString());
  }

  public static Boolean execute(Boolean left, Boolean right) {
    return Boolean.valueOf(left.booleanValue() && right.booleanValue());
  }

}