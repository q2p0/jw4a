package org.q2p0.jw4a.abstractDesc.nodes;

import java.util.HashMap;
import java.util.Map;

public enum PrimitiveTypeDesc {
   BYTE     ("byte"),
   SHORT    ("short"),
   INT      ("int"),
   LONG     ("long"),
   FLOAT    ("float"),
   DOUBLE   ("double"),
   CHAR     ("char"),
   BOOLEAN  ("boolean");

   private final String type;
   PrimitiveTypeDesc(final String text) {
      this.type = text;
   }

   @Override public String toString() {
      return type;
   }

   private static final Map<String, PrimitiveTypeDesc> lookup = new HashMap<>();
   static {
      for(PrimitiveTypeDesc p : PrimitiveTypeDesc.values()) lookup.put(p.type, p);
   }
   public static PrimitiveTypeDesc parse(String url) {
      return lookup.get(url);
   }
}
