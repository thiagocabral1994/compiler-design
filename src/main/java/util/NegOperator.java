package util;

public class NegOperator {
  public static Object execute(Object object) {
    // TODO: Adicionar cast de tipos válidos.
    throw new RuntimeException("Subtração inválida: " + object.toString());
  }

  public static Integer execute(Integer object) {
    return Integer.valueOf(- object.intValue());
  }

  // TODO: adicionar o resto de execuções válidas para soma.
}