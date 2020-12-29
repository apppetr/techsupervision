package ru.sviridov.techsupervision.defects.place.object;

import android.os.Parcel;
import android.os.Parcelable.Creator;

public class Point extends Place {
   public static final Creator CREATOR = new Creator() {
      public Point createFromParcel(Parcel var1) {
         return new Point(var1.readInt(), (char)var1.readInt());
      }

      public Point[] newArray(int var1) {
         return new Point[var1];
      }
   };
   private static final String POINT_PATTERN = "{\"type\":\"point\", \"x\": \"%s\", \"y\":\"%s\"}";
   private int x;
   private char y;

   public Point() {
      super(Type.POINT);
   }

   public Point(int var1, char var2) {
      super(Type.POINT);
      this.x = var1;
      this.y = (char)var2;
   }

   public int describeContents() {
      return 0;
   }

   public int getX() {
      return this.x;
   }

   public char getY() {
      return this.y;
   }

   public void setX(int var1) {
      this.x = var1;
   }

   public void setY(char var1) {
      this.y = (char)var1;
   }

   public String toStoredValue() {
      return String.format("{\"type\":\"point\", \"x\": \"%s\", \"y\":\"%s\"}", this.x, this.y);
   }

   public String toString() {
      return String.format("%1$s/%2$s", this.x, this.y);
   }

   public void writeToParcel(Parcel var1, int var2) {
      var1.writeInt(this.x);
      var1.writeInt(this.y);
   }
}
