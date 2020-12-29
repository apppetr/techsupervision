package ru.sviridov.techsupervision.utils.vectors.impl;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PointF;
import ru.sviridov.techsupervision.utils.vectors.ImagePatch;
import ru.sviridov.techsupervision.utils.vectors.Painting;

public class LineFImagePatch implements ImagePatch {
   private static final Paint PAINT = new Paint(1);
   private final PointF a;
   private final PointF b;

   static {
      PAINT.setColor(-65536);
   }

   public LineFImagePatch(PointF var1, PointF var2) {
      this.a = var1;
      this.b = var2;
   }

   public void draw(Painting var1, Canvas var2) {
      PAINT.setStrokeWidth(var1.getDensity() * 2.0F);
      var2.drawLine(this.a.x, this.a.y, this.b.x, this.b.y, PAINT);
   }

   public void set(PointF var1, PointF var2) {
      this.a.set(var1);
      this.b.set(var2);
   }
}
