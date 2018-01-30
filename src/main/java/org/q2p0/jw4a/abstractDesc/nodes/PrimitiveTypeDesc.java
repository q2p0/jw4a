package org.q2p0.jw4a.abstractDesc.nodes;

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
   private PrimitiveTypeDesc(final String text) {
      this.type = text;
   }

   @Override public String toString() {
      return type;
   }
}
