package util;

import java.util.Stack;

import ast.ArrayLValue;
import ast.IdentifierLValue;
import ast.LValue;
import ast.ObjectLValue;

public class LValueTracker {
  public static String getHeadID(LValue lvalue) {
    if (lvalue instanceof ArrayLValue) {
      return LValueTracker.getHeadID(((ArrayLValue) lvalue).getLValue());
    }

    if (lvalue instanceof ObjectLValue) {
      return LValueTracker.getHeadID(((ObjectLValue) lvalue).getLValue());
    }

    return ((IdentifierLValue) lvalue).getID();
  }

  public static Object assignVariable(LValue lvalue, Object variable, Object value) {
    Stack<String> lvalueTrace = new Stack<String>();
    return null;
  }

  private static Stack<String> traceStack(LValue lvalue, Stack<Object> operands) {
    Stack<String> lvalueTrace = new Stack<String>();
    if (lvalue instanceof ArrayLValue) {
      ArrayLValue arrayLValue = (ArrayLValue) lvalue;
      arrayLValue.getExpression().accept(null);
    }

    return lvalueTrace;
  }
}
