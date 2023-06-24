package util.interpret;

public class NotOperator {
  public static Object execute(Object object) {
    if(object == null) {
      return Boolean.valueOf(true);
    }

    
    if(object instanceof Boolean) {
      return NotOperator.execute((Boolean) object);
    }

    throw new RuntimeException("Negação lógica inválida: " + object.toString());
  }

  public static Boolean execute(Boolean object) {
    return Boolean.valueOf(!object.booleanValue());
  }

}