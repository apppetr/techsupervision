package ru.lihachev.norm31937.defects.place.object;

import android.support.annotation.NonNull;

public enum Type {
   POINT("point"),
   RECTANGLE("rectangle");

   private final String storedValue;

   private Type(String storedValue2) {
      this.storedValue = storedValue2;
   }

   static Type fromString(@NonNull String value) {
      for (Type type : values()) {
         if (type.storedValue.equals(value)) {
            return type;
         }
      }
      return null;
   }

   public String getStoredValue() {
      return this.storedValue;
   }
}
