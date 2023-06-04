package util;

public class AndOperator {
  public static Object execute(Object left, Object right) {
    // TODO: Adicionar cast de tipos válidos.
    throw new RuntimeException("`E` lógico inválido: " + left.toString() + ", " + right.toString());
  }

  public static Boolean execute(Boolean left, Boolean right) {
    return Boolean.valueOf(left.booleanValue() && right.booleanValue());
  }

  // TODO: adicionar o resto de execuções válidas para soma.
}