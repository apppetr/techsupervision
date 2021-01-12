package ru.lihachev.norm31937.defects.place.object;

import android.os.Parcel;
import android.os.Parcelable;

public class Point extends Place {
   public static final Parcelable.Creator<Point> CREATOR = new Parcelable.Creator<Point>() {
      public Point createFromParcel(Parcel in) {
         return new Point(in.readInt(), (char) in.readInt());
      }

      public Point[] newArray(int size) {
         return new Point[size];
      }
   };
   private static final String POINT_PATTERN = "{\"type\":\"point\", \"x\": \"%s\", \"y\":\"%s\"}";

   /* renamed from: x */
   private int f75x;

   /* renamed from: y */
   private char f76y;

   public Point() {
      super(Type.POINT);
   }

   public Point(int x, char y) {
      super(Type.POINT);
      this.f75x = x;
      this.f76y = y;
   }

   public int getX() {
      return this.f75x;
   }

   public void setX(int x) {
      this.f75x = x;
   }

   public char getY() {
      return this.f76y;
   }

   public void setY(char y) {
      this.f76y = y;
   }

   public int describeContents() {
      return 0;
   }

   public void writeToParcel(Parcel dest, int flags) {
      dest.writeInt(this.f75x);
      dest.writeInt(this.f76y);
   }

   public String toStoredValue() {
      return String.format(POINT_PATTERN, new Object[]{Integer.valueOf(this.f75x), Character.valueOf(this.f76y)});
   }

   public String toString() {
      return String.format("%1$s/%2$s", new Object[]{Integer.valueOf(this.f75x), Character.valueOf(this.f76y)});
   }
}
