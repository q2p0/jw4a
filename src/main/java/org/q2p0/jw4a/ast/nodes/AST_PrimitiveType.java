package org.q2p0.jw4a.ast.nodes;

import java.util.HashMap;
import java.util.Map;

public enum AST_PrimitiveType {
   BYTE     ("byte"),
   SHORT    ("short"),
   INT      ("int"),
   LONG     ("long"),
   FLOAT    ("float"),
   DOUBLE   ("double"),
   CHAR     ("char"),
   BOOLEAN  ("boolean");

   private final String type;
   AST_PrimitiveType(final String text) {
      this.type = text;
   }

   @Override public String toString() {
      return type;
   }

   private static final Map<String, AST_PrimitiveType> lookup = new HashMap<>();
   static {
      for(AST_PrimitiveType p : AST_PrimitiveType.values()) lookup.put(p.type, p);
   }
   public static AST_PrimitiveType parse(String url) {
      return lookup.get(url);
   }
}
