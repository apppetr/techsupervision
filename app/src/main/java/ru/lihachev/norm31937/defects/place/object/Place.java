package ru.lihachev.norm31937.defects.place.object;

import android.os.Parcelable;
import android.support.annotation.NonNull;

public abstract class Place implements Parcelable {
   @NonNull
   private final Type type;

   public abstract String toStoredValue();

   public Place(@NonNull Type type2) {
      this.type = type2;
   }

   @NonNull
   public Type getType() {
      return this.type;
   }
}