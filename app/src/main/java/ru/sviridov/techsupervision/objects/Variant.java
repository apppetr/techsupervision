package ru.sviridov.techsupervision.objects;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.support.annotation.NonNull;


public class Variant extends Meta implements Parcelable {
   public static final Creator CREATOR = new Creator() {
      public Variant createFromParcel(Parcel var1) {
         return new Variant(var1.readInt(), var1.readString());
      }

      public Variant[] newArray(int var1) {
         return new Variant[var1];
      }
   };
   private Integer _id;
   private String name;

   public Variant() {
   }

   public Variant(int var1, String var2) {
      this._id = var1;
      this.name = var2;
   }

   public int describeContents() {
      return 0;
   }

   public int getId() {
      return this._id;
   }

   public String getName() {
      return this.name;
   }

   public void setName(String var1) {
      this.name = var1;
   }

   public String toString() {
      return this.name;
   }

   public void writeToParcel(@NonNull Parcel var1, int var2) {
      var1.writeInt(this._id);
      var1.writeString(this.name);
   }
}
