package util;

import ast.*;

public class LValueParser {
  public static String getHead(LValue lvalue) {
    if (lvalue instanceof ArrayLValue) {
      return LValueParser.getHead(((ArrayLValue) lvalue).getLValue());
    }

    if (lvalue instanceof ObjectLValue) {
      return LValueParser.getHead(((ObjectLValue) lvalue).getLValue());
    }

    return ((IdentifierLValue) lvalue).getID();
  }
}
