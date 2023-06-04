package util;

public class LessOperator {
  public static Object execute(Object left, Object right) {
    // TODO: Adicionar cast de tipos válidos.
    throw new RuntimeException("`<` lógico inválido: " + left.toString() + ", " + right.toString());
  }

  public static Boolean execute(Integer left, Integer right) {
    return Boolean.valueOf(left.intValue() < right.intValue());
  }

  // TODO: adicionar o resto de execuções válidas para soma.
}