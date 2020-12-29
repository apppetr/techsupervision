package ru.sviridov.techsupervision.defects.place.object;

import android.support.annotation.NonNull;

public enum Type {
   POINT("point"),
   RECTANGLE("rectangle");

   private final String storedValue;

   private Type(String var3) {
      this.storedValue = var3;
   }

   static Type fromString(@NonNull String var0) {
      Type[] var1 = values();
      int var2 = var1.length;
      int var3 = 0;

      Type var5;
      while(true) {
         if (var3 >= var2) {
            var5 = null;
            break;
         }

         Type var4 = var1[var3];
         if (var4.storedValue.equals(var0)) {
            var5 = var4;
            break;
         }

         ++var3;
      }

      return var5;
   }

   public String getStoredValue() {
      return this.storedValue;
   }
}
