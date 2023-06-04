package util;

public class NotOperator {
  public static Object execute(Object object) {
    // TODO: Adicionar cast de tipos válidos.
    throw new RuntimeException("Negação lógica inválida: " + object.toString());
  }

  public static Boolean execute(Boolean object) {
    return Boolean.valueOf(!object.booleanValue());
  }

  // TODO: adicionar o resto de execuções válidas para soma.
}