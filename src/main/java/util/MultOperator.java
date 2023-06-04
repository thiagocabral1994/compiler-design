package util;

public class MultOperator {
  public static Object execute(Object left, Object right) {
    // TODO: Adicionar cast de tipos válidos.
    throw new RuntimeException("Multiplicação inválida: " + left.toString() + ", " + right.toString());
  }

  public static Integer execute(Integer left, Integer right) {
    return Integer.valueOf(left.intValue() * right.intValue());
  }

  // TODO: adicionar o resto de execuções válidas para soma.
}