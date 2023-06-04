package util;

public class ModOperator {
  public static Object execute(Object left, Object right) {
    // TODO: Adicionar cast de tipos válidos.
    throw new RuntimeException("Mod inválido: " + left.toString() + ", " + right.toString());
  }

  public static Integer execute(Integer left, Integer right) {
    return Integer.valueOf(left.intValue() % right.intValue());
  }

  // TODO: adicionar o resto de execuções válidas para soma.
}