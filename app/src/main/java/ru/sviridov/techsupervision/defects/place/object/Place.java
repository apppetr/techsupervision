package ru.sviridov.techsupervision.defects.place.object;

import android.os.Parcelable;
import android.support.annotation.NonNull;

public abstract class Place implements Parcelable {
   @NonNull
   private final Type type;

   public Place(@NonNull Type var1) {
      this.type = var1;
   }

   @NonNull
   public Type getType() {
      return this.type;
   }

   public abstract String toStoredValue();
}
