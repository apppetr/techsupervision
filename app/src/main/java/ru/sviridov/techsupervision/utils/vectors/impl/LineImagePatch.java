package ru.sviridov.techsupervision.utils.vectors.impl;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import ru.sviridov.techsupervision.utils.vectors.ImagePatch;
import ru.sviridov.techsupervision.utils.vectors.Painting;

public class LineImagePatch implements ImagePatch {
   private static final Paint PAINT = new Paint(1);
   private final Point a;
   private final Point b;

   static {
      PAINT.setColor(-1430519809);
   }

   public LineImagePatch(Point var1, Point var2) {
      this.a = var1;
      this.b = var2;
   }

   public void draw(Painting var1, Canvas var2) {
      PAINT.setStrokeWidth(var1.getDensity() * 3.0F);
      var2.drawLine((float)this.a.x, (float)this.a.y, (float)this.b.x, (float)this.b.y, PAINT);
   }
}
