package ru.sviridov.techsupervision.defects.place.object;

import android.os.Parcel;
import android.os.Parcelable.Creator;
import android.util.Pair;

public class Rectangle extends Place {
   public static final Creator CREATOR = new Creator() {
      public Rectangle createFromParcel(Parcel var1) {
         return new Rectangle((Point)var1.readParcelable(Point.class.getClassLoader()), (Point)var1.readParcelable(Point.class.getClassLoader()));
      }

      public Rectangle[] newArray(int var1) {
         return new Rectangle[var1];
      }
   };
   private static final String RECTANGLE_PATTERN = "{\"type\":\"rectangle\", \"x\": \"%s\", \"y\":\"%s\", \"x2\": \"%s\", \"y2\":\"%s\"}";
   private final Point finish;
   private final Point start;

   public Rectangle(Point var1, Point var2) {
      super(Type.RECTANGLE);
      Pair var3 = this.sort(var1, var2);
      this.start = (Point)var3.first;
      this.finish = (Point)var3.second;
   }

   private Pair sort(Point var1, Point var2) {
      Point var3 = var1;
      Point var4 = var2;
      Pair var7;
      if (var1.getX() > var2.getX()) {
         if (var1.getY() > var2.getY()) {
            var7 = new Pair(var2, var1);
            return var7;
         }

         int var5 = var1.getX();
         var3 = new Point(var2.getX(), var1.getY());
         var4 = new Point(var5, var2.getY());
      }

      var2 = var3;
      var1 = var4;
      if (var3.getY() > var4.getY()) {
         char var6 = var3.getY();
         var2 = new Point(var3.getX(), var4.getY());
         var1 = new Point(var4.getX(), var6);
      }

      var7 = new Pair(var2, var1);
      return var7;
   }

   public int describeContents() {
      return 0;
   }

   public Point getFinish() {
      return this.finish;
   }

   public Point getStart() {
      return this.start;
   }

   public String toStoredValue() {
      return String.format("{\"type\":\"rectangle\", \"x\": \"%s\", \"y\":\"%s\", \"x2\": \"%s\", \"y2\":\"%s\"}", this.start.getX(), this.start.getY(), this.finish.getX(), this.finish.getY());
   }

   public String toString() {
      String var1;
      if (this.start.getX() == this.finish.getX()) {
         var1 = String.format("%1$s/%2$s-%3$s", this.start.getX(), this.start.getY(), this.finish.getY());
      } else if (this.start.getY() == this.finish.getY()) {
         var1 = String.format("%1$s-%2$s/%3$s", this.start.getX(), this.finish.getX(), this.start.getY());
      } else {
         var1 = String.format("%1$s-%2$s/%3$s-%4$s", this.start.getX(), this.finish.getX(), this.start.getY(), this.finish.getY());
      }

      return var1;
   }

   public void writeToParcel(Parcel var1, int var2) {
      var1.writeParcelable(this.start, var2);
      var1.writeParcelable(this.finish, var2);
   }
}
