package ru.lihachev.norm31937.defects.place.object;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Pair;

public class Rectangle extends Place {
   public static final Parcelable.Creator<Rectangle> CREATOR = new Parcelable.Creator<Rectangle>() {
      public Rectangle createFromParcel(Parcel in) {
         return new Rectangle((Point) in.readParcelable(Point.class.getClassLoader()), (Point) in.readParcelable(Point.class.getClassLoader()));
      }

      public Rectangle[] newArray(int size) {
         return new Rectangle[size];
      }
   };
   private static final String RECTANGLE_PATTERN = "{\"type\":\"rectangle\", \"x\": \"%s\", \"y\":\"%s\", \"x2\": \"%s\", \"y2\":\"%s\"}";
   private final Point finish;
   private final Point start;

   public Rectangle(Point start2, Point finish2) {
      super(Type.RECTANGLE);
      Pair<Point, Point> pair = sort(start2, finish2);
      this.start = (Point) pair.first;
      this.finish = (Point) pair.second;
   }

   private Pair<Point, Point> sort(Point start2, Point finish2) {
      if (start2.getX() > finish2.getX()) {
         if (start2.getY() > finish2.getY()) {
            return new Pair<>(finish2, start2);
         }
         int tmp = start2.getX();
         Point start3 = new Point(finish2.getX(), start2.getY());
         finish2 = new Point(tmp, finish2.getY());
         start2 = start3;
      }
      if (start2.getY() > finish2.getY()) {
         char tmp2 = start2.getY();
         Point start4 = new Point(start2.getX(), finish2.getY());
         finish2 = new Point(finish2.getX(), tmp2);
         start2 = start4;
      }
      return new Pair<>(start2, finish2);
   }

   public Point getStart() {
      return this.start;
   }

   public Point getFinish() {
      return this.finish;
   }

   public void writeToParcel(Parcel dest, int flags) {
      dest.writeParcelable(this.start, flags);
      dest.writeParcelable(this.finish, flags);
   }

   public String toStoredValue() {
      return String.format(RECTANGLE_PATTERN, new Object[]{Integer.valueOf(this.start.getX()), Character.valueOf(this.start.getY()), Integer.valueOf(this.finish.getX()), Character.valueOf(this.finish.getY())});
   }

   public int describeContents() {
      return 0;
   }

   public String toString() {
      if (this.start.getX() == this.finish.getX()) {
         return String.format("%1$s/%2$s-%3$s", new Object[]{Integer.valueOf(this.start.getX()), Character.valueOf(this.start.getY()), Character.valueOf(this.finish.getY())});
      } else if (this.start.getY() == this.finish.getY()) {
         return String.format("%1$s-%2$s/%3$s", new Object[]{Integer.valueOf(this.start.getX()), Integer.valueOf(this.finish.getX()), Character.valueOf(this.start.getY())});
      } else {
         return String.format("%1$s-%2$s/%3$s-%4$s", new Object[]{Integer.valueOf(this.start.getX()), Integer.valueOf(this.finish.getX()), Character.valueOf(this.start.getY()), Character.valueOf(this.finish.getY())});
      }
   }
}

