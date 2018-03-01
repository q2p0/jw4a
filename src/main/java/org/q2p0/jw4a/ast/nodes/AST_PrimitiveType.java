package org.q2p0.jw4a.ast.nodes;

import java.util.HashMap;
import java.util.Map;

public enum AST_PrimitiveType {
   BYTE     ("byte"   , byte.class),
   SHORT    ("short"  , short.class),
   INT      ("int"    , int.class),
   LONG     ("long"   , long.class),
   FLOAT    ("float"  , float.class),
   DOUBLE   ("double" , double.class),
   CHAR     ("char"   , char.class),
   BOOLEAN  ("boolean", boolean.class);

   private final String type;
   private final Class _class;
   AST_PrimitiveType(final String text, final Class c) {
      this.type = text;
      this._class = c;
   }

   @Override public String toString() {
      return type.toLowerCase();
   }
   public Class toClass() { return _class; }

   private static final Map<String, AST_PrimitiveType> lookup = new HashMap<>();
   static {
      for(AST_PrimitiveType p : AST_PrimitiveType.values()) lookup.put(p.type, p);
   }
   public static AST_PrimitiveType parse(String url) {
      return lookup.get(url);
   }
}
