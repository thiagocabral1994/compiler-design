package util;

public class EqualOperator {
  public static Boolean execute(Object left, Object right) {
    // TODO: Adicionar cast de tipos válidos.
    throw new RuntimeException("Igualdade inválida: " + left.toString() + ", " + right.toString());
  }

  public static Boolean execute(Integer left, Integer right) {
    return Boolean.valueOf(left.intValue() == right.intValue());
  }

  // TODO: adicionar o resto de execuções válidas para soma.
}